package cz.scholz.kafka.testapps.producer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class KafkaTestProducer extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(KafkaTestProducer.class.getName());

    private final KafkaTestProducerConfig verticleConfig;
    private KafkaProducer<String, String> producer;
    private long sentMessages = 0;
    private final int numberOfKeys;
    private final Long messageCount;

    public KafkaTestProducer(KafkaTestProducerConfig verticleConfig) {
        log.info("Creating KafkaTestProducer");
        this.verticleConfig = verticleConfig;
        this.numberOfKeys = verticleConfig.getNumberOfKeys();
        this.messageCount = verticleConfig.getMessageCount();
    }

    /*
    Start the verticle
     */
    @Override
    public void start(Promise<Void> start) {
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

        producer = KafkaProducer.create(vertx, config, String.class, String.class);
        producer.exceptionHandler(res -> log.error("Received exception", res));

        vertx.setPeriodic(verticleConfig.getTimer(), res -> sendMessage());

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

                vertx.close(closeRes -> System.exit(0));
            }
        });
    }

    private String getKey() {
        return "{\"key\":\"" + sentMessages % numberOfKeys + "\"}";
    }

    /*
    Stop the verticle
     */
    @Override
    public void stop(Promise<Void> stop) {
        log.info("Stopping the producer.");
        producer.close(res -> stop.complete());
    }
}
