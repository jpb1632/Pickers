package springBootPickers.realData;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 한국투자증권 API → Kafka Producer
 * 실시간 주식 데이터를 수신해서 Kafka에 전송
 *
 * Kafka 장애 시 WebSocket 직접 전송으로 Fallback
 * Health Check (30초 간격) + 자동 복구
 * Kafka 없이 시작해도 Health Check 작동 (SIMULATION_WITH_HEALTHCHECK 모드)
 */
public class KISWebSocketClientWithKafka extends WebSocketClient {

	private static final Logger log = LoggerFactory.getLogger(KISWebSocketClientWithKafka.class);
    
    private final String approvalKey;
    private KafkaProducer<String, String> kafkaProducer;
    private volatile boolean isRunning = true;
    
    // 장마감 타임아웃 체크용
    private long lastDataTime = System.currentTimeMillis();
    private volatile boolean simulationStarted = false;
    
    // 시뮬레이션 모드 여부
    private final boolean isSimulationMode;
    
    // 초기 시뮬레이션 vs 장마감 시뮬레이션 구분
    private volatile boolean isInitialSimulation = false;
    
    // Kafka 상태 관리
    private volatile boolean kafkaAvailable = true;
    private volatile int kafkaFailureCount = 0;
    private static final int MAX_KAFKA_FAILURES = 3;
    
    // WebSocket 서버 참조 (Fallback용)
    private KafkaWebSocketServer wsServer;
    
    // Health Check 스케줄러 (30초 간격)
    private ScheduledExecutorService healthCheckScheduler;
    private static final int HEALTH_CHECK_INTERVAL_SECONDS = 30;
    
    /**
     * 생성자: approvalKey로 실시간 모드 여부 결정
     */
    public KISWebSocketClientWithKafka(String wsUrl, String approvalKey) throws Exception {
        super(new URI(wsUrl));
        this.approvalKey = approvalKey;
        
        // 시뮬레이션 모드 판별 (두 가지)
        this.isSimulationMode = "SIMULATION_MODE".equals(approvalKey) ||
                               "SIMULATION_WITH_HEALTHCHECK".equals(approvalKey);
        
        // Kafka Producer 초기화
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        // 신뢰성 옵션 설정
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        
        // Timeout 설정 짧게 (빠른 장애 감지)
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 2000);  // 2초
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 2000);  // 2초
        
        this.kafkaProducer = new KafkaProducer<>(props);
        
        log.info("Kafka producer 초기화 완료 (acks=all, retries=3, timeout=2s)");
        
        // 모드별 처리
        if ("SIMULATION_MODE".equals(approvalKey)) {          
            log.info("시뮬레이션 모드로 시작 (health check 비활성)");
            simulationStarted = true;
            startLocalSimulation();
            
        } else if ("SIMULATION_WITH_HEALTHCHECK".equals(approvalKey)) {
            log.info("시뮬레이션 모드로 시작 (health check 활성)");
            simulationStarted = true;
            isInitialSimulation = true;  
            
            startHealthCheckScheduler();
            
        } else {
            startHealthCheckScheduler();
        }
    }    
    
    /**
     * Health Check 스케줄러 시작 (30초마다 Kafka 상태 확인)
     */
    private void startHealthCheckScheduler() {
        healthCheckScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "Kafka-HealthCheck");
            t.setDaemon(true);  // 데몬 스레드로 설정 (메인 종료 시 자동 종료)
            return t;
        });
        
        healthCheckScheduler.scheduleAtFixedRate(
            this::performHealthCheck,
            HEALTH_CHECK_INTERVAL_SECONDS,  // 초기 지연 (30초)
            HEALTH_CHECK_INTERVAL_SECONDS,  // 주기 (30초)
            TimeUnit.SECONDS
        );
        
        log.info("Kafka health check 스케줄러 시작 ({}초 간격)", HEALTH_CHECK_INTERVAL_SECONDS);
    }
    
    /**
     * Health Check 수행 (30초마다 실행)
     */
    private void performHealthCheck() {
        if (!isRunning) return;
        
        checkDataTimeout();
        
        boolean isHealthy = testKafkaHealth();
        
        if (isHealthy && !kafkaAvailable) {
            log.info("Kafka 연결 복구 감지 - Kafka 모드로 전환");
            kafkaAvailable = true;
            kafkaFailureCount = 0;
            
            if (wsServer != null && !wsServer.isConsumerRunning()) {
                log.info("Kafka consumer 시작");
                wsServer.startKafkaConsumer();
                log.info("Kafka consumer 시작 완료");
            }
            
            if (simulationStarted && isInitialSimulation) {
                log.info("초기 시뮬레이션 중지 - Kafka 모드로 전환");
                log.info("참고: 장마감 시뮬레이션은 유지됩니다");
                simulationStarted = false;
                isInitialSimulation = false;
            }
            
        } else if (!isHealthy && kafkaAvailable) {
            log.warn("Kafka health check 실패 - fallback 모드로 전환");
            kafkaAvailable = false;
        }
    }
    
    /**
     * Kafka 상태 체크 (Socket 연결 테스트)
     */
    private boolean testKafkaHealth() {
        try (java.net.Socket socket = new java.net.Socket()) {
            socket.connect(
                new java.net.InetSocketAddress("localhost", 9092), 
                2000  // 2초 타임아웃
            );
            log.debug("Kafka health check 성공");
            return true;
        } catch (Exception e) {
            log.debug("Kafka health check 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * WebSocket 서버 참조 전달 (Kafka 장애 시 직접 전송용)
     */
    public void setWebSocketServer(KafkaWebSocketServer wsServer) {
        this.wsServer = wsServer;
        log.debug("WebSocket 서버 참조 설정 완료 (fallback용)");
    }
    
    @Override
    public void onOpen(ServerHandshake handshake) {
        log.info("한국투자증권 WebSocket 연결 성공");
        log.debug("응답 코드: {}", handshake.getHttpStatus() + " " + handshake.getHttpStatusMessage());
        
        // 실시간 연결 성공 시 시뮬레이션 중지
        stopSimulationMode(); 
        
        subscribeStockPrice("005930"); // 삼성전자
    }
    
    /**
     * 종목 시세 구독
     */
    private void subscribeStockPrice(String stockCode) {
        try {
            JSONObject request = new JSONObject();
            
            // Header
            JSONObject header = new JSONObject();
            header.put("approval_key", approvalKey);
            header.put("custtype", "P");
            header.put("tr_type", "1");
            header.put("content-type", "utf-8");
            
            // Body
            JSONObject body = new JSONObject();
            JSONObject input = new JSONObject();
            input.put("tr_id", "H0STCNT0"); // 실시간시세
            input.put("tr_key", stockCode);
            
            body.put("input", input);
            request.put("header", header);
            request.put("body", body);
            
            String message = request.toString();
            log.debug("구독 요청 전송: {}", message);
            
            send(message);
            log.info("삼성전자({}) 실시간 시세 구독 완료", stockCode);
            
        } catch (Exception e) {
            log.error("종목 구독 실패", e);
        }
    }
    
    @Override
    public void onMessage(String message) {
        try {
            log.debug("수신 [WebSocket 메시지]={}", message.substring(0, Math.min(100, message.length())));
            
            // JSON 응답 처리
            if (message.startsWith("{")) {
                log.debug("응답 메시지: {}", message);
                checkDataTimeout();
                return;
            }
            
            lastDataTime = System.currentTimeMillis();
            
            // 실시간 시세 파싱 (예: 0|H0STCNT0|001|실시간데이터)
            String[] parts = message.split("\\|");
            log.debug("split('|') 결과 길이: {}", parts.length);
            
            if (parts.length >= 4) {
                log.debug("parts[0]={}", parts[0]);
                log.debug("parts[1]={}", parts[1]);
                log.debug("parts[2]={}", parts[2]);
                log.debug("parts[3]={}", parts[3].substring(0, Math.min(100, parts[3].length())) + "...");
                
                parseAndSendToKafka(parts[3]);
            } else {
                log.warn("예상과 다른 데이터 형식: {}", parts.length);
            }
            
        } catch (Exception e) {
            log.error("메시지 처리 오류", e);
        }
    }
    
    /**
     * 실시간 데이터 파싱 후 Kafka 전송
     */
    private void parseAndSendToKafka(String data) {
        try {
            String[] fields = data.split("\\^");
            
            log.debug("split('^') 결과 길이: {}", fields.length);
            
            if (fields.length >= 15) {
                String stockCode = fields[0];
                String time = fields[1];
                String price = fields[2];
                String change = fields[4];
                String volume = fields[12];
                String cumVolume = fields[13];
                
                log.debug("체결 시간: {}", time);
                log.debug("체결 가격: {}", price);
                log.debug("체결 거래량: {}", volume);
                log.debug("누적 거래량: {}", cumVolume);
                
                // JSON 생성
                JSONObject stockData = new JSONObject();
                stockData.put("symbol", stockCode);
                stockData.put("timestamp", formatTime(time));
                stockData.put("price", parsePrice(price));
                stockData.put("change", parsePrice(change));
                stockData.put("volume", parseInt(volume));
                stockData.put("cumulativeVolume", parseLong(cumVolume));
                
                String jsonString = stockData.toString();
                
                // Kafka 전송 (장애 시 자동 Fallback)
                sendData(jsonString);
                
                log.debug("[한국투자증권] 시세 수신 - 시간 {}, 가격 {}원, 거래량 {}주, 누적 {}주",
                    time, price, volume, cumVolume);
            } else {
                log.warn("예상과 다른 필드 개수: {}", fields.length);
                for (int i = 0; i < Math.min(fields.length, 15); i++) {
                    log.debug("fields[{}]={}", i, fields[i]);
                }
            }
        } catch (Exception e) {
            log.error("데이터 파싱 오류", e);
        }
    }
    
    /**
     * 핵심 로직: Kafka 전송 시도 → 실패 시 WebSocket 직접 전송
     */
    private void sendData(String jsonData) {
        if (kafkaAvailable) {
            try {
                // Kafka 전송 시도 (2초 timeout)
                Future<?> future = kafkaProducer.send(new ProducerRecord<>("stock", "005930", jsonData));
                future.get(2, TimeUnit.SECONDS);
                
                // 성공
                kafkaFailureCount = 0;
                log.debug("Kafka 전송 성공");
                
            } catch (Exception e) {
                kafkaFailureCount++;
                
                log.warn("Kafka 전송 실패 ({}/{}): {}", 
                    kafkaFailureCount, MAX_KAFKA_FAILURES, e.getMessage());
                
                // 3회 실패 시 Kafka 비활성화
                if (kafkaFailureCount >= MAX_KAFKA_FAILURES) {
                    kafkaAvailable = false;
                    log.error("Kafka 연결 상태 불량 - WebSocket 직접 전송으로 전환");
                }
                
                // 실패 시 즉시 WebSocket 직접 전송
                sendDirectToWebSocket(jsonData);
            }
        } else {
            // Kafka 비활성화 상태면 WebSocket 직접 전송
            sendDirectToWebSocket(jsonData);
        }
    }
    
    /**
     * WebSocket 직접 전송 (Kafka Bypass)
     */
    private void sendDirectToWebSocket(String jsonData) {
        if (wsServer != null) {
            wsServer.broadcast(jsonData);
            log.debug("WebSocket 직접 전송 완료 (Kafka 우회)");
        } else {
            log.error("WebSocket 서버 참조가 없어 메시지 전송을 할 수 없습니다.");
        }
    }
    
    /**
     * 시간 포맷 변환
     */
    private String formatTime(String time) {
        try {
            if (time.length() == 6) {
                return time.substring(0, 2) + ":" + 
                       time.substring(2, 4) + ":" + 
                       time.substring(4, 6);
            }
        } catch (Exception e) {
            // 무시
        }
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
    
    /**
     * 가격 파싱
     */
    private double parsePrice(String price) {
        try {
            return Double.parseDouble(price.replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    /**
     * 정수 파싱
     */
    private int parseInt(String value) {
        try {
            return Integer.parseInt(value.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Long 파싱
     */
    private long parseLong(String value) {
        try {
            return Long.parseLong(value.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0L;
        }
    }
    
    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.warn("한국투자증권 WebSocket 연결 해제: {}", reason);
        log.debug("종료 코드: {}", code);
        
        if (isRunning && !isSimulationMode) {
            log.info("5초 후 재연결 시도...");
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    reconnect();
                } catch (Exception e) {
                    log.error("재연결 실패: {}", e.getMessage());
                }
            }, "WebSocket-Reconnect").start();  // 스레드 이름 지정
        }
    }
    
    @Override
    public void onError(Exception ex) {
        log.error("한국투자증권 WebSocket 오류", ex);
    }
    
    /**
     * 리소스 종료
     */
    public void shutdown() {
        isRunning = false;
        
        // Health Check 스케줄러 종료
        if (healthCheckScheduler != null) {
            healthCheckScheduler.shutdownNow();
            log.info("Health check 스케줄러 종료");
        }
        
        // Kafka Producer 종료 (타임아웃 추가)
        if (kafkaProducer != null) {
            try {
            	kafkaProducer.close(Duration.ofSeconds(5));
                log.info("Kafka producer 종료");
            } catch (Exception e) {
                log.warn("Kafka Producer 종료 시간 초과", e);
            }
        }
        
        close();
    }
    
    /**
     * 장마감 타임아웃 체크: 30초 동안 데이터가 없으면 시뮬레이션 전환
     */
    private void checkDataTimeout() {
        long now = System.currentTimeMillis();
        long timeSinceLastData = (now - lastDataTime) / 1000; // 초단위
        
        if (!simulationStarted && timeSinceLastData > 30) {
            log.debug("장마감/데이터 미수신 감지");
            log.warn("장이 종료되었거나 30초 이상 데이터가 수신되지 않음");
            log.debug("시뮬레이션 모드로 전환합니다");
            log.debug("실제 거래는 평일 09:00~15:30만 가능합니다");
            log.debug("시뮬레이션 모드로 전환");
            
            startLocalSimulation();
            simulationStarted = true;
            // 주의: isInitialSimulation은 false 유지
            // 이것은 장마감 시뮬레이션이므로 Kafka 복구해도 유지됨
        }
    }
    
    /**
     * 로컬 시뮬레이션 모드 시작 (랜덤 주가 생성)
     */
    public void startLocalSimulation() {
        new Thread(() -> {
            int basePrice = 97000;
            long cumVolume = 10000000L;
            
            log.info("로컬 시뮬레이션 모드 시작 (기본 가격: 97000원)");
            
            while (isRunning && simulationStarted) {
                try {
                    // 랜덤 주가 생성 (±500원)
                    int price = basePrice + (int)(Math.random() * 1000) - 500;
                    int volume = (int)(Math.random() * 1000) + 100;
                    cumVolume += volume;
                    
                    // JSON 생성
                    JSONObject stockData = new JSONObject();
                    stockData.put("symbol", "005930");
                    stockData.put("timestamp", new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    stockData.put("price", price);
                    stockData.put("volume", volume);
                    stockData.put("cumulativeVolume", cumVolume);
                    stockData.put("change", price - basePrice);
                    
                    String jsonData = stockData.toString();
                    
                    // sendData() 사용 (Kafka 장애 시 자동 Fallback)
                    sendData(jsonData);
                    
                    log.debug("시뮬레이션 데이터 - 시간: {}, 가격 {}원, 거래량 {}주",
                        new SimpleDateFormat("HH:mm:ss").format(new Date()),
                        price,
                        volume);
                    
                    Thread.sleep(1000); // 1초마다
                    
                } catch (Exception e) {
                    log.error("시뮬레이션 오류", e);
                    break;
                }
            }
            log.info("시뮬레이션 스레드 종료");
        }, "Simulation-Thread").start();  // 스레드 이름 지정
    }
    
    /**
     * 시뮬레이션 모드 중지
     */
    public void stopSimulationMode() {
        if (this.simulationStarted) {
            this.simulationStarted = false;
            log.info("시뮬레이션 모드 중지 - 실시간 데이터로 전환");
        }
    }
}
