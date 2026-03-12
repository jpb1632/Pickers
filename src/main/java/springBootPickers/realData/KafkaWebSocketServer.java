package springBootPickers.realData;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

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

public class KafkaWebSocketServer extends WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(KafkaWebSocketServer.class);

    private static final String DEFAULT_KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String KAFKA_GROUP_ID = "websocket-group";
    private static final String KAFKA_TOPIC = "stock";
    private static final String DEFAULT_STOCK_CODE = "005930";
    private static final long DEFAULT_SEED_PRICE = 100000L;
    private static final Duration POLL_TIMEOUT = Duration.ofMillis(100);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Set<WebSocket> connections = ConcurrentHashMap.newKeySet();
    private final String kafkaBootstrapServers;

    private KafkaConsumer<String, String> kafkaConsumer;
    private volatile boolean running = true;
    private volatile boolean consumerRunning;

    public KafkaWebSocketServer(InetSocketAddress address) {
        this(address, DEFAULT_KAFKA_BOOTSTRAP_SERVERS);
    }

    public KafkaWebSocketServer(InetSocketAddress address, String kafkaBootstrapServers) {
        super(address);
        this.kafkaBootstrapServers = normalizeKafkaBootstrapServers(kafkaBootstrapServers);
        setConnectionLostTimeout(60);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections.add(conn);
        log.info("WS 클라이언트 연결: {}", conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn);
        log.info("WS 클라이언트 종료: {} (code={}, reason={})", conn.getRemoteSocketAddress(), code, reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        log.debug("WS 메시지 수신: {}", message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        log.error("WebSocket 서버 오류", ex);
    }

    @Override
    public void onStart() {
        log.info("WebSocket 서버 시작 (port={})", getPort());
    }

    public void startKafkaConsumer() {
        if (consumerRunning) {
            return;
        }

        try {
            kafkaConsumer = new KafkaConsumer<>(createConsumerProperties());
            kafkaConsumer.subscribe(Collections.singletonList(KAFKA_TOPIC));
            consumerRunning = true;

            new Thread(this::consumeLoop, "Kafka-Consumer-Thread").start();
            log.info("Kafka consumer 시작 (topic={})", KAFKA_TOPIC);
        } catch (Exception e) {
            consumerRunning = false;
            log.error("Kafka consumer 시작 실패", e);
        }
    }

    public boolean isConsumerRunning() {
        return consumerRunning;
    }

    public void startSimulationMode() {
        startSimulationMode(
                Collections.singletonList(DEFAULT_STOCK_CODE),
                Collections.singletonMap(DEFAULT_STOCK_CODE, DEFAULT_SEED_PRICE)
        );
    }

    public void startSimulationMode(List<String> stockCodes, Map<String, Long> seedPrices) {
        List<String> symbols = (stockCodes == null || stockCodes.isEmpty())
                ? List.of(DEFAULT_STOCK_CODE)
                : List.copyOf(stockCodes);

        log.info("WebSocket 시뮬레이션 시작");

        new Thread(() -> {
            Map<String, Long> currentPrices = new HashMap<>();
            Map<String, Long> referencePrices = new HashMap<>();
            Map<String, Long> cumulativeVolumes = new HashMap<>();

            for (String stockCode : symbols) {
                long seedPrice = resolveSeedPrice(stockCode, seedPrices);
                currentPrices.put(stockCode, seedPrice);
                referencePrices.put(stockCode, seedPrice);
                cumulativeVolumes.put(stockCode, 10000000L + Math.abs(stockCode.hashCode() % 100000));
            }

            while (running) {
                try {
                    for (String stockCode : symbols) {
                        long currentPrice = currentPrices.getOrDefault(stockCode, DEFAULT_SEED_PRICE);
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

                        JSONObject stockData = new JSONObject();
                        stockData.put("symbol", stockCode);
                        stockData.put("timestamp", LocalTime.now().format(TIME_FORMATTER));
                        stockData.put("price", nextPrice);
                        stockData.put("volume", volume);
                        stockData.put("cumulativeVolume", cumulativeVolume);
                        stockData.put("change", nextPrice - referencePrice);

                        broadcast(stockData.toString());
                    }

                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("WebSocket 시뮬레이션 오류", e);
                    break;
                }
            }
        }, "Simulation-Thread").start();
    }

    public void broadcast(String message) {
        for (WebSocket conn : connections) {
            if (conn.isOpen()) {
                conn.send(message);
            }
        }
    }

    public void shutdown() {
        running = false;
        consumerRunning = false;

        if (kafkaConsumer != null) {
            try {
                kafkaConsumer.close(Duration.ofSeconds(5));
            } catch (Exception e) {
                log.warn("Kafka consumer 종료 중 경고", e);
            }
        }

        try {
            stop();
        } catch (Exception e) {
            log.error("WebSocket 서버 종료 실패", e);
        }
    }

    private Properties createConsumerProperties() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KAFKA_GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return props;
    }

    private String normalizeKafkaBootstrapServers(String bootstrapServers) {
        if (bootstrapServers == null || bootstrapServers.isBlank()) {
            return DEFAULT_KAFKA_BOOTSTRAP_SERVERS;
        }

        return bootstrapServers;
    }

    private void consumeLoop() {
        try {
            while (running && consumerRunning) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(POLL_TIMEOUT);
                for (ConsumerRecord<String, String> record : records) {
                    broadcast(record.value());
                }
            }
        } catch (Exception e) {
            log.error("Kafka consumer 루프 오류", e);
        } finally {
            consumerRunning = false;
            if (kafkaConsumer != null) {
                try {
                    kafkaConsumer.close();
                } catch (Exception e) {
                    log.warn("Kafka consumer 정리 중 경고", e);
                }
            }
        }
    }

    private long resolveSeedPrice(String stockCode, Map<String, Long> seedPrices) {
        if (seedPrices == null) {
            return DEFAULT_SEED_PRICE;
        }

        Long seedPrice = seedPrices.get(stockCode);
        return seedPrice != null && seedPrice > 0 ? seedPrice : DEFAULT_SEED_PRICE;
    }
}
