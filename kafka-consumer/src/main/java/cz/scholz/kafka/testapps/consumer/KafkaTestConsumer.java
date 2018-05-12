package cz.scholz.kafka.testapps.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.kafka.client.consumer.KafkaConsumer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaTestConsumer extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(KafkaTestConsumer.class.getName());

    private final KafkaTestConsumerConfig verticleConfig;
    private KafkaConsumer<String, String> consumer;
    private long receivedMessages = 0;
    private Long messageCount;

    public KafkaTestConsumer(KafkaTestConsumerConfig verticleConfig) throws Exception {
        log.info("Creating KafkaTestConsumer");
        this.verticleConfig = verticleConfig;
        this.messageCount = verticleConfig.getMessageCount();
    }

    /*
    Start the verticle
     */
    @Override
    public void start(Future<Void> start) {
        log.info("Starting KafkaTestConsumer");

        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", verticleConfig.getBootstrapServers());
        config.put("group.id", verticleConfig.getGroupId());
        config.put("auto.offset.reset", verticleConfig.getAutoOffsetReset());
        config.put("enable.auto.commit", verticleConfig.getEnableAutoCommit());
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = KafkaConsumer.create(vertx, config, String.class, String.class);

        consumer.handler(res -> {
            log.info("Received message (topic: {}, partition: {}, offset: {}) with key {}: {}", res.topic(), res.partition(), res.offset(), res.key(), res.value());
            receivedMessages++;

            if (messageCount != null && messageCount <= receivedMessages)   {
                log.info("{} messages sent ... exiting", messageCount);
                vertx.close();
            }
        });

        consumer.exceptionHandler(res -> {
            log.error("Received exception", res);
        });

        consumer.subscribe(verticleConfig.getTopic(), res -> {
            if (res.succeeded()) {
                log.info("Subscribed to topic {}", verticleConfig.getTopic());
                start.complete();
            }
            else {
                log.error("Failed to subscribe to topic {}", verticleConfig.getTopic());
                start.fail("Failed to subscribe to topic " + verticleConfig.getTopic());
            }
        });

    }

    /*
    Stop the verticle
     */
    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        log.info("Stopping the consumer.");
        consumer.endHandler(res -> {
            stopFuture.complete();
        });
    }
}
