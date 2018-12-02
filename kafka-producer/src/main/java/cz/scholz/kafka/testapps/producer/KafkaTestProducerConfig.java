package cz.scholz.kafka.testapps.producer;

public class KafkaTestProducerConfig {
    private final String bootstrapServers;
    private final String topic;
    private final int timer;
    private final int numberOfKeys;
    private final Long messageCount;
    private String acks = "1";
    private final String trustStorePassword;
    private final String trustStorePath;
    private final String keyStorePassword;
    private final String keyStorePath;
    private final String username;
    private final String password;

    public KafkaTestProducerConfig(String bootstrapServers, String topic, int timer, int numberOfKeys, Long messageCount, String trustStorePassword, String trustStorePath, String keyStorePassword, String keyStorePath, String username, String password) {
        this.bootstrapServers = bootstrapServers;
        this.topic = topic;
        this.timer = timer;
        this.numberOfKeys = numberOfKeys;
        this.messageCount = messageCount;
        this.trustStorePassword = trustStorePassword;
        this.trustStorePath = trustStorePath;
        this.keyStorePassword = keyStorePassword;
        this.keyStorePath = keyStorePath;
        this.username = username;
        this.password = password;
    }

    public static KafkaTestProducerConfig fromEnv() {
        String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
        String topic = System.getenv("TOPIC");
        int timer = Integer.valueOf(System.getenv("TIMER"));
        int numberOfKeys = Integer.parseInt(System.getenv("NUMBER_OF_KEYS") == null ? "1" : System.getenv("NUMBER_OF_KEYS"));
        Long messageCount = System.getenv("MESSAGE_COUNT") == null ? null : Long.valueOf(System.getenv("MESSAGE_COUNT"));
        String trustStorePassword = System.getenv("TRUSTSTORE_PASSWORD") == null ? null : System.getenv("TRUSTSTORE_PASSWORD");
        String trustStorePath = System.getenv("TRUSTSTORE_PATH") == null ? null : System.getenv("TRUSTSTORE_PATH");
        String keyStorePassword = System.getenv("KEYSTORE_PASSWORD") == null ? null : System.getenv("KEYSTORE_PASSWORD");
        String keyStorePath = System.getenv("KEYSTORE_PATH") == null ? null : System.getenv("KEYSTORE_PATH");
        String username = System.getenv("USERNAME") == null ? null : System.getenv("USERNAME");
        String password = System.getenv("PASSWORD") == null ? null : System.getenv("PASSWORD");

        return new KafkaTestProducerConfig(bootstrapServers, topic, timer, numberOfKeys, messageCount, trustStorePassword, trustStorePath, keyStorePassword, keyStorePath, username, password);
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getTopic() {
        return topic;
    }

    public int getTimer() {
        return timer;
    }

    public int getNumberOfKeys() {
        return numberOfKeys;
    }

    public Long getMessageCount() {
        return messageCount;
    }

    public String getAcks() {
        return acks;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
