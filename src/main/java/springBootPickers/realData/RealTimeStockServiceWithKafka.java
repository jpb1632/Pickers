package springBootPickers.realData;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spring Boot 시작 시 실시간 주식 데이터 서비스 자동 시작
 * 
 * 아키텍처:
 * 한국투자증권 API → KISWebSocketClient → Kafka → KafkaWebSocketServer → 프론트엔드
 * 
 * Kafka 장애 시 WebSocket 직접 전송으로 우회
 * Health Check (30초 간격) + 자동 복구
 */
@Component
public class RealTimeStockServiceWithKafka implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(RealTimeStockServiceWithKafka.class);
    
    @Value("${kis.app-key:YOUR_APP_KEY}")
    private String appKey;
    
    @Value("${kis.app-secret:YOUR_APP_SECRET}")
    private String appSecret;
    
    @Value("${kis.websocket.url:ws://ops.koreainvestment.com:21000}")
    private String websocketUrl;
    
    @Value("${kis.realtime.enabled:false}")
    private boolean realtimeEnabled;
    
    @Value("${kafka.enabled:false}")
    private boolean kafkaEnabled;
    
    private final KISApiService apiService;
    
    private KISWebSocketClientWithKafka kisClient;
    
    // WebSocket 서버 참조 (공유)
    private KafkaWebSocketServer wsServer;
    
    public RealTimeStockServiceWithKafka(KISApiService apiService) {
        this.apiService = apiService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        
        if (!realtimeEnabled) {
            log.debug("-------------------------------------------");
            log.warn("실시간 데이터 서비스가 비활성화되어 있습니다.");
            log.debug("-------------------------------------------");
            log.debug("활성화 설정 (application.properties):");
            log.debug("kis.realtime.enabled=true");
            log.debug("kafka.enabled=true");
            log.debug("설정 후 재시작하면 적용됩니다.");
            log.debug("-------------------------------------------");
            return;
        }
        
        log.debug("\n");
        log.info("===========================================");
        log.info("실시간 주식 서비스 시작");
        log.info("연동: 한국투자증권 WebSocket + Kafka");
        log.info("내부 WebSocket 서버: localhost:9000");
        log.info("===========================================");
        log.info("-------------------------------------------");
        
        // 1. WebSocket 서버 시작 (포트 9000)
        InetSocketAddress address = new InetSocketAddress("localhost", 9000);
        wsServer = new KafkaWebSocketServer(address);
        wsServer.start();
        
        log.debug("-------------------------------------------");
        log.info("WebSocket 서버 시작 (포트: 9000)");
        log.debug("-------------------------------------------");
        
        // Kafka 활성화 확인
        if (!kafkaEnabled) {
            log.warn("Kafka 비활성화 - 시뮬레이션 모드로 실행");
            log.debug("실제 API 연동 방법:");
            log.debug("1. Zookeeper 실행:");
            log.debug("cd c:\\src\\kafka_2.13-3.8.0\\bin\\windows");
            log.debug("zookeeper-server-start.bat ..\\..\\config\\zookeeper.properties");
            log.debug("2. Kafka 실행:");
            log.debug("kafka-server-start.bat ..\\..\\config\\server.properties");
            log.debug("3. application.properties 설정:");
            log.debug("kafka.enabled=true");
            log.debug("-------------------------------------------");
            
            startSimulationWithHealthCheck();  // 시뮬레이션 모드(health check 포함)
            printAccessInfo();
            return;
        }
        
        // 2. Kafka 연결 테스트
        if (!testKafkaConnection()) {
            log.error("Kafka 연결 실패");
            log.error("Zookeeper와 Kafka가 실행 중인지 확인하세요:");
            log.error("1. Zookeeper: localhost:2181");
            log.error("2. Kafka: localhost:9092");
            log.warn("시뮬레이션 모드로 전환합니다.");
            startSimulationWithHealthCheck();  // 시뮬레이션 모드(health check 포함)
            printAccessInfo();
            return;
        }
        
        log.info("Kafka 연결 성공 (localhost:9092)");
        
        // 3. Kafka Consumer 시작
        wsServer.startKafkaConsumer();
        
        // 4. API 키 확인
        if ("YOUR_APP_KEY".equals(appKey) || "YOUR_APP_SECRET".equals(appSecret)) {
            log.error("API 키 미설정 - 시뮬레이션 모드");
            log.error("실제 데이터 연동 방법:");
            log.error("1. https://apiportal.koreainvestment.com");
            log.error("2. 모의투자 신청 후 APP KEY 발급");
            log.error("3. application.properties에 추가");
            log.error("-------------------------------------------");
            
            startSimulationWithHealthCheck();  // 시뮬레이션 모드(health check 포함)
            printAccessInfo();
            return;
        }
        
        // 5. 접속키 발급
        log.debug("한국투자증권 API 인증 중...");
        String approvalKey = apiService.getApprovalKey(appKey, appSecret);
        
        if (approvalKey == null) {
            log.error("접속키 발급 실패");
            log.warn("시뮬레이션 모드로 전환합니다.");
            startSimulationWithHealthCheck();  // 시뮬레이션 모드(health check 포함)
            printAccessInfo();
            return;
        }
        
        log.info("접속키 발급 완료");
        
        // 6. 한국투자증권 WebSocket → Kafka Producer
        log.debug("한국투자증권 WebSocket 연결 중");
        log.debug("WebSocket URL: {}", websocketUrl);
        
        try {
        	kisClient = new KISWebSocketClientWithKafka(websocketUrl, approvalKey);
            
            // WebSocket 서버 참조 전달 (Kafka 장애 시 직접 전송용)
            kisClient.setWebSocketServer(wsServer);
            
            kisClient.connect();
            
            log.info("한국투자증권 연결 완료");
            log.info("-------------------------------------------");
            log.info("실시간 데이터 수신 시작");
            log.info("데이터 흐름: 한국투자증권 → Kafka → 프론트엔드");
            log.info("Kafka 장애 시: WebSocket 직접 전송으로 우회");
            log.info("장 종료/미수신 시: 시뮬레이션 자동 전환");
            log.info("차트 URL: http://localhost:8080/chart/detail?stockNum=000001");
            log.info("-------------------------------------------");
            log.debug("");
            log.debug("참고: 장 마감 이후에는 실시간 데이터가 없을 수 있습니다.");
            log.debug("");
            log.info("-------------------------------------------");
            printAccessInfo();
            
        } catch (Exception e) {
            log.error("WebSocket 연결 실패: {}", e.getMessage());
            log.warn("시뮬레이션 모드로 전환합니다.");
            startSimulationWithHealthCheck();  // 시뮬레이션 모드(health check 포함)
            printAccessInfo();
        }
    }
    
    /**
     * Kafka 연결 테스트
     */
    private boolean testKafkaConnection() {
        try (java.net.Socket socket = new java.net.Socket()) {
            socket.connect(new java.net.InetSocketAddress("localhost", 9092), 1000);
            log.debug("Kafka 포트(9092) 연결 확인");
            return true;
        } catch (Exception e) {
            log.debug("Kafka 포트(9092) 연결 실패: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Health Check와 함께 시뮬레이션 시작
     * Kafka 없이 시작해도 Health Check가 작동하여 자동 복구 가능
     */
    private void startSimulationWithHealthCheck() {
        log.info("시뮬레이션 모드로 시작합니다. (health check 활성)");
        log.info("Kafka가 복구되면 자동으로 Kafka 모드로 전환됩니다.");
        
        try {
            // 시뮬레이션 모드지만 health check는 활성화
            kisClient = new KISWebSocketClientWithKafka(
                websocketUrl, 
                "SIMULATION_WITH_HEALTHCHECK"  // 시뮬레이션 + health check 모드
            );
            
            // WebSocket 서버 참조 전달
            kisClient.setWebSocketServer(wsServer);
            
            // 시뮬레이션 시작
            kisClient.startLocalSimulation();
            
            log.info("시뮬레이션 클라이언트 생성 완료");
            log.info("Health check 스케줄러 시작 (30초 간격)");
            log.info("Kafka 복구 감지 시 자동 전환");
            
        } catch (Exception e) {
            log.error("시뮬레이션 시작 실패", e);
            log.warn("Fallback: WebSocket 서버 시뮬레이션으로 전환");
            
            // Fallback: KafkaWebSocketServer의 시뮬레이션 사용
            wsServer.startSimulationMode();
        }
    }
    
    /**
     * 접속 정보 출력
     */
    private void printAccessInfo() {
        log.debug("===========================================");
        log.debug("차트 페이지 접속:");
        log.debug("http://localhost:8080/chart/detail?stockNum=000001");
        log.debug("또는 메인 페이지에서");
        log.debug("교육 → 삼성전자 클릭");
        log.debug("===========================================");
    }
    
    /**
     * Spring Boot 종료 시 자동으로 호출됨
     */
    @PreDestroy
    public void cleanup() {
        log.info("Spring Boot 종료 감지 - 리소스 정리 시작");
        
        try {
            if (kisClient != null) {
                log.info("KIS WebSocket Client 종료 중...");
                kisClient.shutdown();
            }
        } catch (Exception e) {
            log.error("KIS Client 종료 실패", e);
        }
        
        try {
            if (wsServer != null) {
                log.info("WebSocket Server 종료 중...");
                wsServer.shutdown();
            }
        } catch (Exception e) {
            log.error("WebSocket Server 종료 실패", e);
        }
        
        log.info("리소스 정리 완료");
    }
}