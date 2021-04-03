package cz.scholz.kafka.testapps.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class KafkaTestConsumer extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(KafkaTestConsumer.class.getName());

    private final KafkaTestConsumerConfig verticleConfig;
    private KafkaConsumer<String, String> consumer;
    private long receivedMessages = 0;
    private final Long messageCount;
    private final boolean commit;

    public KafkaTestConsumer(KafkaTestConsumerConfig verticleConfig) {
        log.info("Creating KafkaTestConsumer");
        this.verticleConfig = verticleConfig;
        this.messageCount = verticleConfig.getMessageCount();
        commit = !Boolean.parseBoolean(verticleConfig.getEnableAutoCommit());
    }

    /*
    Start the verticle
     */
    @Override
    public void start(Promise<Void> start) {
        log.info("Starting KafkaTestConsumer");

        if (System.getenv("JAVAX_NET_DEBUG") != null)   {
            System.setProperty("javax.net.debug", System.getenv("JAVAX_NET_DEBUG"));
        }

        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", verticleConfig.getBootstrapServers());
        config.put("group.id", verticleConfig.getGroupId());
        config.put("auto.offset.reset", verticleConfig.getAutoOffsetReset());
        config.put("enable.auto.commit", verticleConfig.getEnableAutoCommit());
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        if (verticleConfig.isHostnameVerification())   {
            config.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "HTTPS");
        } else {
            config.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
        }

        if (verticleConfig.getSslTruststoreCertificates() != null)   {
            log.info("Configuring truststore");
            config.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
            config.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "PEM");
            config.put(SslConfigs.SSL_TRUSTSTORE_CERTIFICATES_CONFIG, verticleConfig.getSslTruststoreCertificates());
        }

        if (verticleConfig.getSslKeystoreCertificateChain() != null && verticleConfig.getSslKeystoreKey() != null)   {
            log.info("Configuring keystore");
            config.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
            config.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "PEM");
            config.put(SslConfigs.SSL_KEYSTORE_CERTIFICATE_CHAIN_CONFIG, verticleConfig.getSslKeystoreCertificateChain());
            config.put(SslConfigs.SSL_KEYSTORE_KEY_CONFIG, verticleConfig.getSslKeystoreKey());
        }

        if ("scram-sha-512".equals(verticleConfig.getAuth()) && verticleConfig.getUsername() != null && verticleConfig.getPassword() != null)   {
            config.put("sasl.mechanism","SCRAM-SHA-512");
            config.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + verticleConfig.getUsername() + "\" password=\"" + verticleConfig.getPassword() + "\";");

            if (config.get("security.protocol") != null && config.get("security.protocol").equals("SSL"))  {
                config.put("security.protocol","SASL_SSL");
            } else {
                config.put("security.protocol","SASL_PLAINTEXT");
            }
        }

        if ("kubernetes".equals(verticleConfig.getAuth()))   {
            config.put("sasl.mechanism","OAUTHBEARER");
            config.put("sasl.login.callback.handler.class","io.strimzi.kafka.kubernetes.authenticator.KubernetesTokenLoginCallbackHandler");

            if (verticleConfig.getToken() != null) {
                config.put("sasl.jaas.config", "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required token=" + verticleConfig.getToken() + ";");
            } else {
                config.put("sasl.jaas.config", "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required;");
            }

            if (config.get("security.protocol") != null && config.get("security.protocol").equals("SSL"))  {
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

        consumer.exceptionHandler(res -> log.error("Received exception", res));

        if (verticleConfig.getPatttern() != null) {
            consumer.subscribe(Pattern.compile(verticleConfig.getPatttern()), res -> {
                if (res.succeeded()) {
                    log.info("Subscribed to pattern {}", verticleConfig.getPatttern());
                    start.complete();
                } else {
                    log.error("Failed to subscribe to pattern {}", verticleConfig.getPatttern());
                    start.fail("Failed to subscribe to pattern " + verticleConfig.getPatttern());
                }
            });
        } else {
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
    }

    /*
    Stop the verticle
     */
    @Override
    public void stop(Promise<Void> stop) {
        log.info("Stopping the consumer.");
        consumer.endHandler(res -> stop.complete());
    }
}
