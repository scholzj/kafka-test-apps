package cz.scholz.kafka.testapps.producer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.config.SslConfigs;
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

        if (System.getenv("JAVAX_NET_DEBUG") != null)   {
            System.setProperty("javax.net.debug", System.getenv("JAVAX_NET_DEBUG"));
        }

        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", verticleConfig.getBootstrapServers());
        config.put("acks", verticleConfig.getAcks());
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        if (verticleConfig.isHostnameVerification())   {
            config.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "HTTPS");
        } else {
            config.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
        }

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
        KafkaProducerRecord<String, String> record = KafkaProducerRecord.create(verticleConfig.getTopic(), getKey(), "{ \"" + verticleConfig.getMessage() + "\": \"" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "\" }");
        producer.write(record, res2 -> {
            log.info("Message sent to topic {} with key {} and value {}", record.topic(), record.key(), record.value());
            sentMessages++;

            if (messageCount != null && messageCount <= sentMessages)   {
                log.info("{} messages sent ... exiting", messageCount);

                vertx.close(closeRes -> {
                    System.exit(0);
                });
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
