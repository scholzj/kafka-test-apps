package cz.scholz.kafka.testapps.producer;

public class KafkaTestProducerConfig {
    private final String bootstrapServers;
    private final String topic;
    private final int timer;
    private final int numberOfKeys;
    private final Long messageCount;
    private final String acks;
    private final String trustStorePassword;
    private final String trustStorePath;
    private final String keyStorePassword;
    private final String keyStorePath;
    private final boolean hostnameVerification;
    private final String username;
    private final String password;
    private final String auth;
    private final String token;
    private final String message;

    public KafkaTestProducerConfig(String bootstrapServers, String topic, int timer, int numberOfKeys, Long messageCount, String acks, String trustStorePassword, String trustStorePath, String keyStorePassword, String keyStorePath, boolean hostnameVerification, String username, String password, String auth, String token, String message) {
        this.bootstrapServers = bootstrapServers;
        this.topic = topic;
        this.timer = timer;
        this.numberOfKeys = numberOfKeys;
        this.messageCount = messageCount;
        this.acks = acks;
        this.trustStorePassword = trustStorePassword;
        this.trustStorePath = trustStorePath;
        this.keyStorePassword = keyStorePassword;
        this.keyStorePath = keyStorePath;
        this.hostnameVerification = hostnameVerification;
        this.username = username;
        this.password = password;
        this.auth = auth;
        this.token = token;
        this.message = message;
    }

    public static KafkaTestProducerConfig fromEnv() {
        String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
        String topic = System.getenv("TOPIC");
        int timer = Integer.valueOf(System.getenv("TIMER"));
        int numberOfKeys = Integer.parseInt(System.getenv("NUMBER_OF_KEYS") == null ? "1" : System.getenv("NUMBER_OF_KEYS"));
        Long messageCount = System.getenv("MESSAGE_COUNT") == null ? null : Long.valueOf(System.getenv("MESSAGE_COUNT"));
        String acks = System.getenv("ACKS") == null ? "1" : System.getenv("ACKS");
        String trustStorePassword = System.getenv("TRUSTSTORE_PASSWORD") == null ? null : System.getenv("TRUSTSTORE_PASSWORD");
        String trustStorePath = System.getenv("TRUSTSTORE_PATH") == null ? null : System.getenv("TRUSTSTORE_PATH");
        String keyStorePassword = System.getenv("KEYSTORE_PASSWORD") == null ? null : System.getenv("KEYSTORE_PASSWORD");
        String keyStorePath = System.getenv("KEYSTORE_PATH") == null ? null : System.getenv("KEYSTORE_PATH");
        boolean hostnameVerification = System.getenv("HOSTNAME_VERIFICATION") == null ? true : Boolean.parseBoolean(System.getenv("HOSTNAME_VERIFICATION"));
        String username = System.getenv("USERNAME") == null ? null : System.getenv("USERNAME");
        String password = System.getenv("PASSWORD") == null ? null : System.getenv("PASSWORD");
        String auth = System.getenv("AUTH") == null ? null : System.getenv("AUTH");
        String token = System.getenv("TOKEN") == null ? null : System.getenv("TOKEN");
        String message = System.getenv("MESSAGE") == null ? "Hello World" : System.getenv("MESSAGE");

        return new KafkaTestProducerConfig(bootstrapServers, topic, timer, numberOfKeys, messageCount, acks, trustStorePassword, trustStorePath, keyStorePassword, keyStorePath, hostnameVerification, username, password, auth, token, message);
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

    public boolean isHostnameVerification() {
        return hostnameVerification;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAuth() {
        return auth;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }
}
