package springBootPickers.realData;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import springBootPickers.domain.StockDTO;
import springBootPickers.mapper.StockMapper;

@Component
public class RealTimeStockServiceWithKafka implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RealTimeStockServiceWithKafka.class);

    private static final String DEFAULT_STOCK_CODE = "005930";
    private static final long DEFAULT_SEED_PRICE = 100000L;
    private static final String DEFAULT_KAFKA_HOST = "localhost";
    private static final int DEFAULT_KAFKA_PORT = 9092;
    private static final int REALTIME_SYMBOL_LIMIT = 10;
    private static final int LIVE_RECOVERY_CONNECT_TIMEOUT_SECONDS = 5;

    @Value("${kis.app-key:YOUR_APP_KEY}")
    private String appKey;

    @Value("${kis.app-secret:YOUR_APP_SECRET}")
    private String appSecret;

    @Value("${kis.websocket.url:ws://ops.koreainvestment.com:21000}")
    private String websocketUrl;

    @Value("${kis.realtime.symbols:}")
    private String configuredRealtimeSymbols;

    @Value("${kis.realtime.enabled:false}")
    private boolean realtimeEnabled;

    @Value("${kafka.enabled:false}")
    private boolean kafkaEnabled;

    @Value("${kafka.bootstrap-servers:localhost:9092}")
    private String kafkaBootstrapServers;

    @Value("${realtime.internal-ws.host:localhost}")
    private String internalWsHost;

    @Value("${realtime.internal-ws.port:9000}")
    private int internalWsPort;

    private final KISApiService apiService;
    private final StockMapper stockMapper;

    private KISWebSocketClientWithKafka kisClient;
    private KafkaWebSocketServer wsServer;
    private volatile boolean liveRecoveryInProgress;

    private List<String> realtimeSymbols = List.of(DEFAULT_STOCK_CODE);
    private Map<String, Long> seedPrices = Map.of(DEFAULT_STOCK_CODE, DEFAULT_SEED_PRICE);

    public RealTimeStockServiceWithKafka(KISApiService apiService, StockMapper stockMapper) {
        this.apiService = apiService;
        this.stockMapper = stockMapper;
    }

    @Override
    public void run(String... args) {
        if (!realtimeEnabled) {
            log.warn("실시간 주식 서비스가 비활성화되어 있습니다.");
            return;
        }

        logStartup();
        startInternalWebSocketServer();
        loadRealtimeSymbols();

        if (!kafkaEnabled) {
            startSimulationOnly("Kafka가 비활성화되어 있습니다.");
            return;
        }

        if (!isApiCredentialConfigured()) {
            startSimulationOnly("KIS API 인증 정보가 설정되지 않았습니다.");
            return;
        }

        String approvalKey = apiService.getApprovalKey(appKey, appSecret);
        if (approvalKey == null) {
            startSimulationOnly("KIS approval key 발급에 실패했습니다.");
            return;
        }

        if (!testKafkaConnection()) {
            startRecoverableSimulation("Kafka(" + kafkaBootstrapServers + ")에 연결할 수 없습니다.");
            return;
        }

        wsServer.startKafkaConsumer();
        connectRealtimeClient(approvalKey);
    }

    private void logStartup() {
        log.info("실시간 주식 서비스 시작");
        log.info("Kafka bootstrap servers: {}", kafkaBootstrapServers);
        log.info("내부 WebSocket 서버: {}:{}", internalWsHost, internalWsPort);
    }

    private void startInternalWebSocketServer() {
        wsServer = new KafkaWebSocketServer(
                new InetSocketAddress(internalWsHost, internalWsPort),
                kafkaBootstrapServers
        );
        wsServer.start();
        log.info("내부 WebSocket 서버 시작 완료");
    }

    private void connectRealtimeClient(String approvalKey) {
        shutdownCurrentClient();

        try {
            kisClient = new KISWebSocketClientWithKafka(
                    websocketUrl,
                    approvalKey,
                    realtimeSymbols,
                    seedPrices,
                    kafkaBootstrapServers,
                    stockMapper
            );
            kisClient.setWebSocketServer(wsServer);
            kisClient.connect();

            log.info("KIS WebSocket 연결 시작");
            log.info("실시간 구독 종목 {}개: {}", realtimeSymbols.size(), String.join(", ", realtimeSymbols));
            log.info("차트 URL: http://localhost:8080/chart/detail?stockNum={}", getSampleStockNum());
        } catch (Exception e) {
            startSimulationOnly("KIS WebSocket 연결에 실패했습니다: " + e.getMessage());
        }
    }

    private void startSimulationOnly(String reason) {
        startSimulationMode(reason, false);
    }

    private void startRecoverableSimulation(String reason) {
        startSimulationMode(reason, true);
    }

    private void startSimulationMode(String reason, boolean recoverable) {
        log.warn("{} 시뮬레이션 모드로 전환합니다.", reason);
        shutdownCurrentClient();

        try {
            KISWebSocketClientWithKafka simulationClient = new KISWebSocketClientWithKafka(
                    websocketUrl,
                    recoverable ? "SIMULATION_WITH_HEALTHCHECK" : "SIMULATION_MODE",
                    realtimeSymbols,
                    seedPrices,
                    kafkaBootstrapServers,
                    stockMapper
            );
            simulationClient.setWebSocketServer(wsServer);

            if (recoverable) {
                simulationClient.setKafkaRecoveryHandler(() -> recoverLiveClient(simulationClient));
                simulationClient.startLocalSimulation();
                log.info("Kafka health check 복구가 가능한 시뮬레이션을 시작합니다.");
            } else {
                log.info("자동 복구 없는 시뮬레이션을 시작합니다.");
            }

            kisClient = simulationClient;
        } catch (Exception e) {
            log.error("시뮬레이션 클라이언트 시작 실패", e);
            wsServer.startSimulationMode(realtimeSymbols, seedPrices);
        }

        printAccessInfo();
    }

    private void recoverLiveClient(KISWebSocketClientWithKafka simulationClient) {
        synchronized (this) {
            if (liveRecoveryInProgress || kisClient != simulationClient) {
                return;
            }
            liveRecoveryInProgress = true;
        }

        KISWebSocketClientWithKafka liveClient = null;
        boolean recovered = false;

        try {
            if (!isApiCredentialConfigured()) {
                log.warn("KIS 인증 정보가 없어 live 복구를 건너뜁니다.");
                return;
            }

            String approvalKey = apiService.getApprovalKey(appKey, appSecret);
            if (approvalKey == null) {
                log.warn("KIS approval key 재발급에 실패해 live 복구를 건너뜁니다.");
                return;
            }

            liveClient = new KISWebSocketClientWithKafka(
                    websocketUrl,
                    approvalKey,
                    realtimeSymbols,
                    seedPrices,
                    kafkaBootstrapServers,
                    stockMapper
            );
            liveClient.setWebSocketServer(wsServer);

            log.info("Kafka 복구 감지 - KIS live 스트림 재연결을 시도합니다.");
            boolean connected = liveClient.connectBlocking(LIVE_RECOVERY_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!connected || !liveClient.isOpen()) {
                log.warn("live 복구에 실패해 시뮬레이션 모드를 유지합니다.");
                return;
            }

            kisClient = liveClient;
            recovered = true;
            log.info("KIS live 스트림 복구에 성공했습니다.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("live 복구가 중단되었습니다.");
        } catch (Exception e) {
            log.warn("live 복구에 실패했습니다: {}", e.getMessage());
        } finally {
            if (recovered) {
                simulationClient.completeRecoveryAttempt(true);
                simulationClient.shutdown();
            } else {
                if (liveClient != null) {
                    liveClient.shutdown();
                }
                simulationClient.completeRecoveryAttempt(false);
            }

            liveRecoveryInProgress = false;
        }
    }

    private boolean testKafkaConnection() {
        InetSocketAddress probeAddress = resolveKafkaProbeAddress();

        try (Socket socket = new Socket()) {
            socket.connect(probeAddress, 1000);
            return true;
        } catch (Exception e) {
            log.debug(
                    "Kafka 연결 확인 실패 ({}:{}): {}",
                    probeAddress.getHostString(),
                    probeAddress.getPort(),
                    e.getMessage()
            );
            return false;
        }
    }

    private InetSocketAddress resolveKafkaProbeAddress() {
        if (kafkaBootstrapServers == null || kafkaBootstrapServers.isBlank()) {
            return new InetSocketAddress(DEFAULT_KAFKA_HOST, DEFAULT_KAFKA_PORT);
        }

        String firstBroker = kafkaBootstrapServers.split(",")[0].trim();
        int separatorIndex = firstBroker.lastIndexOf(':');

        if (separatorIndex <= 0 || separatorIndex == firstBroker.length() - 1) {
            return new InetSocketAddress(firstBroker, DEFAULT_KAFKA_PORT);
        }

        String host = firstBroker.substring(0, separatorIndex);
        String portValue = firstBroker.substring(separatorIndex + 1);

        try {
            return new InetSocketAddress(host, Integer.parseInt(portValue));
        } catch (NumberFormatException e) {
            log.warn("Kafka 포트 파싱 실패 - 기본 포트 {} 사용: {}", DEFAULT_KAFKA_PORT, firstBroker);
            return new InetSocketAddress(host, DEFAULT_KAFKA_PORT);
        }
    }

    private void loadRealtimeSymbols() {
        try {
            LinkedHashMap<String, Long> loadedSeedPrices = loadConfiguredSeedPrices();
            if (loadedSeedPrices.isEmpty()) {
                loadedSeedPrices = loadSeedPricesFromDatabase();
            }

            if (loadedSeedPrices.isEmpty()) {
                applyDefaultRealtimeSymbol();
                return;
            }

            refreshSeedPricesFromRest(loadedSeedPrices);
            realtimeSymbols = List.copyOf(loadedSeedPrices.keySet());
            seedPrices = Map.copyOf(loadedSeedPrices);

            log.info("실시간 대상 {}개 종목 로드: {}", realtimeSymbols.size(), String.join(", ", realtimeSymbols));
        } catch (Exception e) {
            log.warn("실시간 종목 로드 실패 - 기본값으로 전환", e);
            applyDefaultRealtimeSymbol();
        }
    }

    private LinkedHashMap<String, Long> loadConfiguredSeedPrices() {
        LinkedHashMap<String, Long> loadedSeedPrices = new LinkedHashMap<>();
        if (configuredRealtimeSymbols == null || configuredRealtimeSymbols.isBlank()) {
            return loadedSeedPrices;
        }

        for (String rawSymbol : configuredRealtimeSymbols.split(",")) {
            String stockNum = rawSymbol.trim();
            if (stockNum.isEmpty()) {
                continue;
            }

            StockDTO stock = stockMapper.stockSelectOne(stockNum);
            if (stock == null) {
                log.warn("설정 종목이 DB에 없습니다. 종목코드={}", stockNum);
                continue;
            }

            loadedSeedPrices.put(stockNum, resolveCurrentPrice(stock));
        }

        return loadedSeedPrices;
    }

    private LinkedHashMap<String, Long> loadSeedPricesFromDatabase() {
        LinkedHashMap<String, Long> loadedSeedPrices = new LinkedHashMap<>();
        List<StockDTO> stocks = stockMapper.stockSelectForChart(0, REALTIME_SYMBOL_LIMIT, "currentPrice", "desc");

        for (StockDTO stock : stocks) {
            if (stock == null || stock.getStockNum() == null || stock.getStockNum().isBlank()) {
                continue;
            }

            loadedSeedPrices.put(stock.getStockNum(), resolveCurrentPrice(stock));
        }

        return loadedSeedPrices;
    }

    private void refreshSeedPricesFromRest(LinkedHashMap<String, Long> loadedSeedPrices) {
        if (loadedSeedPrices.isEmpty() || !isApiCredentialConfigured()) {
            return;
        }

        String accessToken = apiService.getAccessToken(appKey, appSecret);
        if (accessToken == null || accessToken.isBlank()) {
            log.warn("REST 기준가 조회에 실패해 DB current_price를 사용합니다.");
            return;
        }

        int refreshedCount = 0;

        for (Map.Entry<String, Long> entry : loadedSeedPrices.entrySet()) {
            String stockNum = entry.getKey();
            Long restPrice = apiService.getDomesticCurrentPrice(accessToken, appKey, appSecret, stockNum);
            if (restPrice == null || restPrice <= 0) {
                continue;
            }

            entry.setValue(restPrice);
            refreshedCount++;

            try {
                stockMapper.updateRealtimeCurrentPrice(stockNum, restPrice);
            } catch (Exception e) {
                log.warn("REST 기준가 DB 반영 실패 - 종목 {}: {}", stockNum, e.getMessage());
            }
        }

        if (refreshedCount > 0) {
            log.info("REST 기준가 {}개 종목 갱신", refreshedCount);
        } else {
            log.warn("REST 기준가를 가져오지 못해 DB current_price를 그대로 사용합니다.");
        }
    }

    private void applyDefaultRealtimeSymbol() {
        realtimeSymbols = List.of(DEFAULT_STOCK_CODE);
        seedPrices = Map.of(DEFAULT_STOCK_CODE, DEFAULT_SEED_PRICE);
        log.warn("실시간 종목을 찾지 못해 기본값 {}을 사용합니다.", DEFAULT_STOCK_CODE);
    }

    private long resolveCurrentPrice(StockDTO stock) {
        return stock.getCurrentPrice() != null ? stock.getCurrentPrice() : DEFAULT_SEED_PRICE;
    }

    private boolean isApiCredentialConfigured() {
        return appKey != null
                && appSecret != null
                && !appKey.isBlank()
                && !appSecret.isBlank()
                && !"YOUR_APP_KEY".equals(appKey)
                && !"YOUR_APP_SECRET".equals(appSecret);
    }

    private String getSampleStockNum() {
        if (realtimeSymbols.isEmpty()) {
            return DEFAULT_STOCK_CODE;
        }

        return realtimeSymbols.contains(DEFAULT_STOCK_CODE)
                ? DEFAULT_STOCK_CODE
                : realtimeSymbols.get(0);
    }

    private void printAccessInfo() {
        log.info("차트 URL: http://localhost:8080/chart/detail?stockNum={}", getSampleStockNum());
    }

    private void shutdownCurrentClient() {
        if (kisClient == null) {
            return;
        }

        try {
            kisClient.shutdown();
        } catch (Exception e) {
            log.warn("기존 실시간 클라이언트 종료 실패: {}", e.getMessage());
        } finally {
            kisClient = null;
        }
    }

    @PreDestroy
    public void cleanup() {
        log.info("실시간 리소스 정리 시작");

        try {
            shutdownCurrentClient();
        } catch (Exception e) {
            log.error("KIS 실시간 클라이언트 종료 실패", e);
        }

        try {
            if (wsServer != null) {
                wsServer.shutdown();
            }
        } catch (Exception e) {
            log.error("내부 WebSocket 서버 종료 실패", e);
        }

        log.info("실시간 리소스 정리 완료");
    }
}
