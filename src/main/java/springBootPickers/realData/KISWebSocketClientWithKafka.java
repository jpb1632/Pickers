package springBootPickers.realData;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import springBootPickers.mapper.StockMapper;

public class KISWebSocketClientWithKafka extends WebSocketClient {

    private static final Logger log = LoggerFactory.getLogger(KISWebSocketClientWithKafka.class);

    private static final String DEFAULT_STOCK_CODE = "005930";
    private static final long DEFAULT_SEED_PRICE = 100000L;
    private static final String DEFAULT_KAFKA_HOST = "localhost";
    private static final int DEFAULT_KAFKA_PORT = 9092;
    private static final String DEFAULT_KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String KAFKA_TOPIC = "stock";
    private static final String REALTIME_TR_ID = "H0STCNT0";
    private static final String MODE_SIMULATION = "SIMULATION_MODE";
    private static final String MODE_SIMULATION_WITH_HEALTHCHECK = "SIMULATION_WITH_HEALTHCHECK";
    private static final int MAX_KAFKA_FAILURES = 3;
    private static final int KAFKA_TIMEOUT_SECONDS = 2;
    private static final int HEALTH_CHECK_INTERVAL_SECONDS = 30;
    private static final int DATA_TIMEOUT_SECONDS = 30;
    private static final int REALTIME_FIELD_COUNT = 46;
    private static final long DB_PERSIST_INTERVAL_MS = 5000L;
    private static final long SIMULATION_INTERVAL_MS = 1000L;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final String approvalKey;
    private final List<String> stockCodes;
    private final Map<String, Long> seedPrices;
    private final String kafkaBootstrapServers;
    private final StockMapper stockMapper;
    private final KafkaProducer<String, String> kafkaProducer;
    private final boolean simulationMode;
    private final Map<String, Long> latestPrices = new ConcurrentHashMap<>();
    private final Map<String, Long> lastPersistedAt = new ConcurrentHashMap<>();

    private volatile boolean running = true;
    private volatile boolean simulationStarted;
    private volatile boolean initialSimulation;
    private volatile boolean kafkaAvailable = true;
    private volatile int kafkaFailureCount;
    private volatile long lastDataTime = System.currentTimeMillis();

    private KafkaWebSocketServer wsServer;
    private ScheduledExecutorService healthCheckScheduler;
    private Runnable kafkaRecoveryHandler;
    private volatile boolean recoveryRequested;

    public KISWebSocketClientWithKafka(String wsUrl, String approvalKey) throws Exception {
        this(
                wsUrl,
                approvalKey,
                List.of(DEFAULT_STOCK_CODE),
                Map.of(DEFAULT_STOCK_CODE, DEFAULT_SEED_PRICE),
                DEFAULT_KAFKA_BOOTSTRAP_SERVERS,
                null
        );
    }

    public KISWebSocketClientWithKafka(
            String wsUrl,
            String approvalKey,
            List<String> stockCodes,
            Map<String, Long> seedPrices,
            String kafkaBootstrapServers,
            StockMapper stockMapper
    ) throws Exception {
        super(new URI(wsUrl));
        this.approvalKey = approvalKey;
        this.stockCodes = normalizeStockCodes(stockCodes);
        this.seedPrices = seedPrices == null ? Map.of() : Map.copyOf(seedPrices);
        this.kafkaBootstrapServers = normalizeKafkaBootstrapServers(kafkaBootstrapServers);
        this.stockMapper = stockMapper;
        this.simulationMode = MODE_SIMULATION.equals(approvalKey) || MODE_SIMULATION_WITH_HEALTHCHECK.equals(approvalKey);
        this.kafkaProducer = createKafkaProducer();

        initializeLatestPrices();
        initializeMode();
    }

    public void setWebSocketServer(KafkaWebSocketServer wsServer) {
        this.wsServer = wsServer;
    }

    public void setKafkaRecoveryHandler(Runnable kafkaRecoveryHandler) {
        this.kafkaRecoveryHandler = kafkaRecoveryHandler;
    }

    public void completeRecoveryAttempt(boolean recovered) {
        recoveryRequested = false;

        if (recovered) {
            simulationStarted = false;
            initialSimulation = false;
        }
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        log.info("KIS WebSocket 연결 성공");
        stopSimulationMode();

        for (String stockCode : stockCodes) {
            subscribeStockPrice(stockCode);
        }

        log.info("실시간 시세 구독 시작 - {}개 종목", stockCodes.size());
    }

    @Override
    public void onMessage(String message) {
        try {
            if (message == null || message.isBlank()) {
                return;
            }

            if (message.startsWith("{")) {
                checkDataTimeout();
                return;
            }

            lastDataTime = System.currentTimeMillis();
            stopSimulationMode();

            String[] frame = splitRealtimeFrame(message);
            if (frame == null) {
                return;
            }

            int dataCount = parseFrameCount(frame[2]);
            publishRealtimeTicks(frame[3], dataCount);
        } catch (Exception e) {
            log.error("실시간 메시지 처리 실패", e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.warn("KIS WebSocket 연결 종료 - code={}, reason={}", code, reason);

        if (running && !simulationMode) {
            new Thread(() -> {
                try {
                    Thread.sleep(5000L);
                    reconnect();
                } catch (Exception e) {
                    log.error("KIS WebSocket 재연결 실패", e);
                }
            }, "WebSocket-Reconnect").start();
        }
    }

    @Override
    public void onError(Exception ex) {
        log.error("KIS WebSocket 오류", ex);
    }

    public void startLocalSimulation() {
        new Thread(() -> {
            Map<String, Long> referencePrices = new HashMap<>();
            Map<String, Long> currentPrices = new HashMap<>();
            Map<String, Long> cumulativeVolumes = new HashMap<>();

            for (String stockCode : stockCodes) {
                long seedPrice = resolveSeedPrice(stockCode);
                referencePrices.put(stockCode, seedPrice);
                currentPrices.put(stockCode, seedPrice);
                cumulativeVolumes.put(stockCode, 10000000L + Math.abs(stockCode.hashCode() % 100000));
            }

            log.info("시뮬레이션 시작 - {}개 종목", stockCodes.size());

            while (running && simulationStarted) {
                try {
                    for (String stockCode : stockCodes) {
                        long currentPrice = currentPrices.getOrDefault(stockCode, resolveSeedPrice(stockCode));
                        long referencePrice = referencePrices.getOrDefault(stockCode, currentPrice);
                        long swingRange = Math.max(100L, currentPrice / 200);
                        long nextPrice = Math.max(
                                100L,
                                currentPrice + ThreadLocalRandom.current().nextLong(-swingRange, swingRange + 1)
                        );
                        int volume = ThreadLocalRandom.current().nextInt(100, 1100);
                        long cumulativeVolume = cumulativeVolumes.getOrDefault(stockCode, 10000000L) + volume;

                        currentPrices.put(stockCode, nextPrice);
                        cumulativeVolumes.put(stockCode, cumulativeVolume);
                        latestPrices.put(stockCode, nextPrice);

                        JSONObject stockData = new JSONObject();
                        stockData.put("symbol", stockCode);
                        stockData.put("timestamp", currentTime());
                        stockData.put("price", nextPrice);
                        stockData.put("volume", volume);
                        stockData.put("cumulativeVolume", cumulativeVolume);
                        stockData.put("change", nextPrice - referencePrice);

                        sendData(stockCode, stockData.toString());
                    }

                    Thread.sleep(SIMULATION_INTERVAL_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("시뮬레이션 처리 실패", e);
                    break;
                }
            }

            log.info("시뮬레이션 종료");
        }, "Simulation-Thread").start();
    }

    public void stopSimulationMode() {
        if (simulationStarted) {
            simulationStarted = false;
            log.info("시뮬레이션 종료, 실시간 데이터로 복귀");
        }
    }

    public void shutdown() {
        running = false;

        if (healthCheckScheduler != null) {
            healthCheckScheduler.shutdownNow();
        }

        try {
            kafkaProducer.close(Duration.ofSeconds(5));
        } catch (Exception e) {
            log.warn("Kafka producer 종료 중 경고", e);
        }

        close();
    }

    private List<String> normalizeStockCodes(List<String> requestedStockCodes) {
        if (requestedStockCodes == null || requestedStockCodes.isEmpty()) {
            return List.of(DEFAULT_STOCK_CODE);
        }

        return List.copyOf(requestedStockCodes);
    }

    private void initializeLatestPrices() {
        for (String stockCode : stockCodes) {
            long seedPrice = resolveSeedPrice(stockCode);
            if (seedPrice > 0) {
                latestPrices.put(stockCode, seedPrice);
            }
        }
    }

    private void initializeMode() {
        if (MODE_SIMULATION.equals(approvalKey)) {
            simulationStarted = true;
            startLocalSimulation();
            return;
        }

        if (MODE_SIMULATION_WITH_HEALTHCHECK.equals(approvalKey)) {
            simulationStarted = true;
            initialSimulation = true;
        }

        startHealthCheckScheduler();
    }

    private String normalizeKafkaBootstrapServers(String bootstrapServers) {
        if (bootstrapServers == null || bootstrapServers.isBlank()) {
            return DEFAULT_KAFKA_BOOTSTRAP_SERVERS;
        }

        return bootstrapServers;
    }

    private KafkaProducer<String, String> createKafkaProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, KAFKA_TIMEOUT_SECONDS * 1000);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, KAFKA_TIMEOUT_SECONDS * 1000);

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        log.info("Kafka producer 준비 완료 - {}", kafkaBootstrapServers);
        return producer;
    }

    private void startHealthCheckScheduler() {
        healthCheckScheduler = Executors.newSingleThreadScheduledExecutor(task -> {
            Thread thread = new Thread(task, "Kafka-HealthCheck");
            thread.setDaemon(true);
            return thread;
        });

        healthCheckScheduler.scheduleAtFixedRate(
                this::performHealthCheck,
                HEALTH_CHECK_INTERVAL_SECONDS,
                HEALTH_CHECK_INTERVAL_SECONDS,
                TimeUnit.SECONDS
        );
    }

    private void performHealthCheck() {
        if (!running) {
            return;
        }

        checkDataTimeout();

        boolean healthy = testKafkaHealth();
        if (healthy) {
            if (!kafkaAvailable) {
                kafkaAvailable = true;
            kafkaFailureCount = 0;
            log.info("Kafka 연결 복구 감지");

            if (wsServer != null && !wsServer.isConsumerRunning()) {
                wsServer.startKafkaConsumer();
            }

            }

            if (simulationStarted && initialSimulation) {
                requestLiveRecovery();
            }
            return;
        }

        if (!healthy && kafkaAvailable) {
            kafkaAvailable = false;
            log.warn("Kafka health check 실패, 직접 전송 모드 유지");
        }
    }

    private void requestLiveRecovery() {
        if (recoveryRequested) {
            return;
        }

        if (kafkaRecoveryHandler == null) {
            log.warn("Kafka가 복구됐지만 live 복구 핸들러가 없어 시뮬레이션 모드를 유지합니다.");
            return;
        }

        recoveryRequested = true;
        log.info("Kafka 복구 감지 - live 데이터 소스 복구를 요청합니다.");

        Thread recoveryThread = new Thread(() -> {
            try {
                kafkaRecoveryHandler.run();
            } catch (Exception e) {
                recoveryRequested = false;
                log.error("live 복구 콜백 실행 실패", e);
            }
        }, "Realtime-Live-Recovery");
        recoveryThread.setDaemon(true);
        recoveryThread.start();
    }

    private boolean testKafkaHealth() {
        InetSocketAddress probeAddress = resolveKafkaProbeAddress();

        try (Socket socket = new Socket()) {
            socket.connect(probeAddress, KAFKA_TIMEOUT_SECONDS * 1000);
            return true;
        } catch (Exception e) {
            log.debug(
                    "Kafka health check 실패 ({}:{}): {}",
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

    private void subscribeStockPrice(String stockCode) {
        try {
            JSONObject header = new JSONObject();
            header.put("approval_key", approvalKey);
            header.put("custtype", "P");
            header.put("tr_type", "1");
            header.put("content-type", "utf-8");

            JSONObject input = new JSONObject();
            input.put("tr_id", REALTIME_TR_ID);
            input.put("tr_key", stockCode);

            JSONObject body = new JSONObject();
            body.put("input", input);

            JSONObject request = new JSONObject();
            request.put("header", header);
            request.put("body", body);

            send(request.toString());
            log.info("실시간 체결 구독 요청 - 종목 {}", stockCode);
        } catch (Exception e) {
            log.error("실시간 구독 요청 실패 - 종목 {}", stockCode, e);
        }
    }

    private String[] splitRealtimeFrame(String message) {
        String[] parts = message.split("\\|");
        if (parts.length < 4) {
            log.debug("예상과 다른 KIS 메시지 형식: {}", message);
            return null;
        }

        return parts;
    }

    private int parseFrameCount(String rawCount) {
        int parsedCount = parseInt(rawCount);
        return parsedCount > 0 ? parsedCount : 1;
    }

    private void publishRealtimeTicks(String payload, int dataCount) {
        String[] fields = payload.split("\\^");
        if (fields.length < 14) {
            log.debug("예상보다 짧은 실시간 필드 길이: {}", fields.length);
            return;
        }

        int maxRecords = fields.length / REALTIME_FIELD_COUNT;
        int recordCount = maxRecords > 0 ? Math.min(dataCount, maxRecords) : 1;

        for (int recordIndex = 0; recordIndex < recordCount; recordIndex++) {
            int offset = recordIndex * REALTIME_FIELD_COUNT;
            publishRealtimeTick(fields, offset);
        }
    }

    private void publishRealtimeTick(String[] fields, int offset) {
        if (fields.length < offset + 14) {
            return;
        }

        String stockCode = fields[offset];
        String time = fields[offset + 1];
        String price = fields[offset + 2];
        String changeSign = fields[offset + 3];
        String change = fields[offset + 4];
        String volume = fields[offset + 12];
        String cumulativeVolume = fields[offset + 13];
        double parsedPrice = parseDouble(price);

        if (parsedPrice <= 0) {
            return;
        }

        JSONObject stockData = new JSONObject();
        stockData.put("symbol", stockCode);
        stockData.put("timestamp", formatTime(time));
        stockData.put("price", parsedPrice);
        stockData.put("change", applyChangeSign(changeSign, Math.abs(parseSignedDouble(change))));
        stockData.put("volume", parseInt(volume));
        stockData.put("cumulativeVolume", parseLong(cumulativeVolume));

        long roundedPrice = Math.round(parsedPrice);
        latestPrices.put(stockCode, roundedPrice);
        persistRealtimeCurrentPrice(stockCode, roundedPrice);
        sendData(stockCode, stockData.toString());
    }

    private void sendData(String stockCode, String jsonData) {
        if (!kafkaAvailable) {
            sendDirectToWebSocket(jsonData);
            return;
        }

        try {
            Future<?> future = kafkaProducer.send(new ProducerRecord<>(KAFKA_TOPIC, stockCode, jsonData));
            future.get(KAFKA_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            kafkaFailureCount = 0;
        } catch (Exception e) {
            kafkaFailureCount++;
            log.warn("Kafka 전송 실패 ({}/{}): {}", kafkaFailureCount, MAX_KAFKA_FAILURES, e.getMessage());

            if (kafkaFailureCount >= MAX_KAFKA_FAILURES) {
                kafkaAvailable = false;
                log.warn("Kafka 연결 상태가 불안정해 직접 전송으로 전환합니다.");
            }

            sendDirectToWebSocket(jsonData);
        }
    }

    private void sendDirectToWebSocket(String jsonData) {
        if (wsServer == null) {
            log.error("WebSocket 서버 참조가 없어 메시지를 전달할 수 없습니다.");
            return;
        }

        wsServer.broadcast(jsonData);
    }

    private String formatTime(String time) {
        if (time != null && time.length() == 6) {
            return time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6);
        }

        return currentTime();
    }

    private String currentTime() {
        return LocalTime.now().format(TIME_FORMATTER);
    }

    private double parseDouble(String value) {
        String digits = digitsOnly(value, true);
        return digits.isBlank() ? 0.0 : Double.parseDouble(digits);
    }

    private double parseSignedDouble(String value) {
        String digits = signedDigits(value, true);
        return digits.isBlank() ? 0.0 : Double.parseDouble(digits);
    }

    private int parseInt(String value) {
        String digits = digitsOnly(value, false);
        return digits.isBlank() ? 0 : Integer.parseInt(digits);
    }

    private long parseLong(String value) {
        String digits = digitsOnly(value, false);
        return digits.isBlank() ? 0L : Long.parseLong(digits);
    }

    private String digitsOnly(String value, boolean allowDecimalPoint) {
        if (value == null) {
            return "";
        }

        String pattern = allowDecimalPoint ? "[^0-9.]" : "[^0-9]";
        return value.replaceAll(pattern, "");
    }

    private String signedDigits(String value, boolean allowDecimalPoint) {
        if (value == null) {
            return "";
        }

        String trimmed = value.trim();
        String sign = trimmed.startsWith("-") ? "-" : "";
        String pattern = allowDecimalPoint ? "[^0-9.]" : "[^0-9]";
        String digits = trimmed.replaceAll(pattern, "");

        if (digits.isBlank()) {
            return "";
        }

        return sign + digits;
    }

    private double applyChangeSign(String signCode, double changeValue) {
        if (changeValue == 0.0) {
            return 0.0;
        }

        if ("4".equals(signCode) || "5".equals(signCode)) {
            return -changeValue;
        }

        if ("3".equals(signCode)) {
            return 0.0;
        }

        return changeValue;
    }

    private long resolveSeedPrice(String stockCode) {
        Long latestPrice = latestPrices.get(stockCode);
        if (latestPrice != null && latestPrice > 0) {
            return latestPrice;
        }

        Long seedPrice = seedPrices.get(stockCode);
        if (seedPrice != null && seedPrice > 0) {
            return seedPrice;
        }

        return DEFAULT_SEED_PRICE;
    }

    private void persistRealtimeCurrentPrice(String stockCode, long currentPrice) {
        if (stockMapper == null || stockCode == null || stockCode.isBlank() || currentPrice <= 0) {
            return;
        }

        long now = System.currentTimeMillis();
        Long lastPersistTime = lastPersistedAt.get(stockCode);
        if (lastPersistTime != null && now - lastPersistTime < DB_PERSIST_INTERVAL_MS) {
            return;
        }

        try {
            stockMapper.updateRealtimeCurrentPrice(stockCode, currentPrice);
            lastPersistedAt.put(stockCode, now);
        } catch (Exception e) {
            log.warn("DB current_price 갱신 실패 - 종목 {}: {}", stockCode, e.getMessage());
        }
    }

    private void checkDataTimeout() {
        long elapsedSeconds = (System.currentTimeMillis() - lastDataTime) / 1000;
        if (simulationStarted || elapsedSeconds <= DATA_TIMEOUT_SECONDS) {
            return;
        }

        log.warn("{}초 이상 실시간 데이터가 없어 시뮬레이션으로 전환합니다.", DATA_TIMEOUT_SECONDS);
        simulationStarted = true;
        startLocalSimulation();
    }
}
