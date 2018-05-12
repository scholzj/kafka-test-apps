package cz.scholz.kafka.testapps.producer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaTestProducer extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(KafkaTestProducer.class.getName());

    private final KafkaTestProducerConfig verticleConfig;
    private KafkaProducer<String, String> producer;
    private long sentMessages = 0;
    private int numberOfKeys;
    private Long messageCount;

    public KafkaTestProducer(KafkaTestProducerConfig verticleConfig) throws Exception {
        log.info("Creating KafkaTestProducer");
        this.verticleConfig = verticleConfig;
        this.numberOfKeys = verticleConfig.getNumberOfKeys();
        this.messageCount = verticleConfig.getMessageCount();
    }

    /*
    Start the verticle
     */
    @Override
    public void start(Future<Void> start) {
        log.info("Starting KafkaTestProducer");

        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", verticleConfig.getBootstrapServers());
        config.put("acks", verticleConfig.getAcks());
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        producer = KafkaProducer.create(vertx, config, String.class, String.class);
        producer.exceptionHandler(res -> {
            log.error("Received exception", res);
        });

        vertx.setPeriodic(verticleConfig.getTimer(), res -> {
            sendMessage();
        });

        start.complete();
        sendMessage();
    }

    private void sendMessage() {
        KafkaProducerRecord<String, String> record = KafkaProducerRecord.create(verticleConfig.getTopic(), getKey(), "Message " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        producer.write(record, res2 -> {
            log.info("Message sent to topic {} with key {} and value {}", record.topic(), record.key(), record.value());
            sentMessages++;

            if (messageCount != null && messageCount <= sentMessages)   {
                log.info("{} messages sent ... exiting", messageCount);
                vertx.close();
            }
        });
    }

    private String getKey() {
        return "key-" + sentMessages % numberOfKeys;
    }

    /*
    Stop the verticle
     */
    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        log.info("Stopping the producer.");
        producer.close(res -> {
            stopFuture.complete();
        });
    }
}
