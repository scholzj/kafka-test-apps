package cz.scholz.kafka.testapps.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.kafka.client.consumer.KafkaConsumer;

import java.security.KeyStore;
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
    private final boolean commit;

    public KafkaTestConsumer(KafkaTestConsumerConfig verticleConfig) throws Exception {
        log.info("Creating KafkaTestConsumer");
        this.verticleConfig = verticleConfig;
        this.messageCount = verticleConfig.getMessageCount();
        commit = !Boolean.parseBoolean(verticleConfig.getEnableAutoCommit());
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

        if (verticleConfig.getTrustStorePassword() != null && verticleConfig.getTrustStorePath() != null)   {
            log.info("Configuring truststore");
            config.put("security.protocol", "SSL");
            config.put("ssl.truststore.type", "PKCS12");
            config.put("ssl.truststore.password", verticleConfig.getTrustStorePassword());
            config.put("ssl.truststore.location", verticleConfig.getTrustStorePath());
        }

        if (verticleConfig.getKeyStorePassword() != null && verticleConfig.getKeyStorePath() != null)   {
            log.info("Configuring keystore");
            config.put("security.protocol", "SSL");
            config.put("ssl.keystore.type", "PKCS12");
            config.put("ssl.keystore.password", verticleConfig.getKeyStorePassword());
            config.put("ssl.keystore.location", verticleConfig.getKeyStorePath());
        }

        if (verticleConfig.getUsername() != null && verticleConfig.getPassword() != null)   {
            config.put("sasl.mechanism","SCRAM-SHA-512");
            config.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + verticleConfig.getUsername() + "\" password=\"" + verticleConfig.getPassword() + "\";");

            if (config.get("security.protocol").equals("SSL"))  {
                config.put("security.protocol","SASL_SSL");
            } else {
                config.put("security.protocol","SASL_PLAINTEXT");
            }
        }

        consumer = KafkaConsumer.create(vertx, config, String.class, String.class);

        consumer.handler(res -> {
            log.info("Received message (topic: {}, partition: {}, offset: {}) with key {}: {}", res.topic(), res.partition(), res.offset(), res.key(), res.value());

            if (commit) {
                consumer.commit();
            }

            receivedMessages++;

            if (messageCount != null && messageCount <= receivedMessages)   {
                log.info("{} messages received ... exiting", messageCount);
                consumer.close();
                vertx.close();
                System.exit(0);
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
