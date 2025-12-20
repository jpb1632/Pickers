package springBootPickers.realData;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kafka(stock) → WebSocket 브리지
 * Kafka에서 읽은 JSON 메시지를 연결된 클라이언트로 전달.
 *
 * 중복 start 방지: consumerRunning
 */
public class KafkaWebSocketServer extends WebSocketServer {

	private static final Logger log = LoggerFactory.getLogger(KafkaWebSocketServer.class);
    
    private Set<WebSocket> connections = ConcurrentHashMap.newKeySet();
    private KafkaConsumer<String, String> kafkaConsumer;
    private volatile boolean isRunning = true;
    
    // consumer 실행 플래그(중복 start 방지)
    private volatile boolean consumerRunning = false;
    
    public KafkaWebSocketServer(InetSocketAddress address) {
        super(address);
        setConnectionLostTimeout(60);
    }
    
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections.add(conn);       
        log.info("WS 클라이언트 연결: {}", conn.getRemoteSocketAddress());
        log.debug("연결 수: {}", connections.size());
    }
    
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn);
        log.info("WS 클라이언트 해제: {} (code={}, reason={})", conn.getRemoteSocketAddress(), code, reason);
        log.debug("연결 수: {}", connections.size());
    }
    
    @Override
    public void onMessage(WebSocket conn, String message) {
        log.debug("WS 메시지 수신: {}", message);
    }
    
    @Override
    public void onError(WebSocket conn, Exception ex) {
        log.error("WebSocket 오류", ex);
    }
    
    @Override
    public void onStart() {
        log.info("WebSocket 서버 시작 (port=9000)");
    }
    
    /**
     * Kafka consumer 시작
     * 이미 실행 중이면 다시 시작하지 않음.
     */
    public void startKafkaConsumer() {
        // 중복 실행 방지
        if (consumerRunning) {
            log.warn("Kafka consumer가 이미 실행 중입니다.");
            return;
        }
        
        try {
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "websocket-group");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // latest부터
            
            kafkaConsumer = new KafkaConsumer<>(props);
            kafkaConsumer.subscribe(Collections.singletonList("stock"));
            
            consumerRunning = true;  // 실행 상태 표시
            
            log.info("Kafka consumer 시작 (topic=stock)");
            
            new Thread(() -> {
                try {
                    while (isRunning && consumerRunning) {
                        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
                        
                        for (ConsumerRecord<String, String> record : records) {
                            String jsonData = record.value();
                            
                            log.debug("Kafka 수신 데이터: {}", jsonData);
                            
                            // 연결된 클라이언트로 브로드캐스트
                            broadcast(jsonData);
                        }
                    }
                } catch (Exception e) {
                    log.error("Kafka consumer 오류", e);
                } finally {
                    consumerRunning = false;
                    if (kafkaConsumer != null) {
                        try {
                            kafkaConsumer.close();
                        } catch (Exception ex) {
                            log.error("Kafka consumer 종료 실패", ex);
                        }
                    }
                }
            }, "Kafka-Consumer-Thread").start();  // 스레드 이름 지정
            
        } catch (Exception e) {
            log.error("Kafka consumer 초기화 실패", e);
            consumerRunning = false;
        }
    }
    
    /**
     * Consumer 실행 여부
     */
    public boolean isConsumerRunning() {
        return consumerRunning;
    }
    
    /**
     * 로컬 테스트용 시뮬레이션 모드
     * Kafka 없이 샘플 데이터를 주기적으로 브로드캐스트.
     */
    public void startSimulationMode() {
        log.info("시뮬레이션 모드 시작 (Kafka 미사용)");
        
        new Thread(() -> {
            int basePrice = 97000;
            long cumVolume = 10000000L;
            
            while (isRunning) {
                try {
                    int price = basePrice + (int)(Math.random() * 1000) - 500;
                    int volume = (int)(Math.random() * 1000) + 100;
                    cumVolume += volume;
                    
                    JSONObject stockData = new JSONObject();
                    stockData.put("symbol", "005930");
                    stockData.put("timestamp", new java.text.SimpleDateFormat("HH:mm:ss")
                        .format(new java.util.Date()));
                    stockData.put("price", price);
                    stockData.put("volume", volume);
                    stockData.put("cumulativeVolume", cumVolume);
                    
                    String jsonData = stockData.toString();
                    broadcast(jsonData);
                    
                    log.debug("시뮬레이션 데이터 - 가격: {}원, 거래량: {}주", price, volume);
                    
                    Thread.sleep(1000);
                    
                } catch (Exception e) {
                    log.error("시뮬레이션 모드 오류", e);
                    break;
                }
            }
        }, "Simulation-Thread").start();  // 스레드 이름 지정
    }
    
    /**
     * 연결된 클라이언트에 일괄 전송
     */
    public void broadcast(String message) {
        for (WebSocket conn : connections) {
            if (conn.isOpen()) {
                conn.send(message);
            }
        }
    }
    
    /**
     * 서버 종료
     */
    public void shutdown() {
        isRunning = false;
        consumerRunning = false;
        
        // consumer 스레드가 빠져나올 시간만 조금 주기.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // consumer close (timeout)
        if (kafkaConsumer != null) {
            try {
                kafkaConsumer.close(Duration.ofSeconds(5));
                log.info("Kafka consumer 종료");
            } catch (Exception e) {
                log.warn("Kafka consumer 종료 대기 시간 초과", e);
            }
        }
        
        try {
            stop();
        } catch (Exception e) {
            log.error("WebSocket 서버 stop 실패", e);
        }
    }
}