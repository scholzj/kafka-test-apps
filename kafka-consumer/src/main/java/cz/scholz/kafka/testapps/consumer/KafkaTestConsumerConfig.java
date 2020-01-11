package cz.scholz.kafka.testapps.consumer;

public class KafkaTestConsumerConfig {
    private final String bootstrapServers;
    private final String topic;
    private final String groupId;
    private final String autoOffsetReset = "earliest";
    private final String enableAutoCommit = "false";
    private final Long messageCount;
    private final String trustStorePassword;
    private final String trustStorePath;
    private final String keyStorePassword;
    private final String keyStorePath;
    private final boolean hostnameVerification;
    private final String username;
    private final String password;
    private final String auth;
    private final String token;

    public KafkaTestConsumerConfig(String bootstrapServers, String topic, String groupId, Long messageCount, String trustStorePassword, String trustStorePath, String keyStorePassword, String keyStorePath, boolean hostnameVerification, String username, String password, String auth, String token) {
        this.bootstrapServers = bootstrapServers;
        this.topic = topic;
        this.groupId = groupId;
        this.messageCount = messageCount;
        this.trustStorePassword = trustStorePassword;
        this.trustStorePath = trustStorePath;
        this.keyStorePassword = keyStorePassword;
        this.keyStorePath = keyStorePath;
        this.hostnameVerification = hostnameVerification;
        this.username = username;
        this.password = password;
        this.auth = auth;
        this.token = token;
    }

    public static KafkaTestConsumerConfig fromEnv() {
        String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
        String topic = System.getenv("TOPIC");
        String groupId = System.getenv("GROUP_ID");
        Long messageCount = System.getenv("MESSAGE_COUNT") == null ? null : Long.valueOf(System.getenv("MESSAGE_COUNT"));
        String trustStorePassword = System.getenv("TRUSTSTORE_PASSWORD") == null ? null : System.getenv("TRUSTSTORE_PASSWORD");
        String trustStorePath = System.getenv("TRUSTSTORE_PATH") == null ? null : System.getenv("TRUSTSTORE_PATH");
        String keyStorePassword = System.getenv("KEYSTORE_PASSWORD") == null ? null : System.getenv("KEYSTORE_PASSWORD");
        String keyStorePath = System.getenv("KEYSTORE_PATH") == null ? null : System.getenv("KEYSTORE_PATH");
        boolean hostnameVerification = System.getenv("HOSTNAME_VERIFICATION") == null ? true : Boolean.parseBoolean(System.getenv("HOSTNAME_VERIFICATION"));
        String username = System.getenv("USERNAME") == null ? null : System.getenv("USERNAME");
        String password = System.getenv("PASSWORD") == null ? null : System.getenv("PASSWORD");
        String auth = System.getenv("AUTH") == null ? null : System.getenv("AUTH");
        String token = System.getenv("TOKEN") == null ? null : System.getenv("TOKEN");

        return new KafkaTestConsumerConfig(bootstrapServers, topic, groupId, messageCount, trustStorePassword, trustStorePath, keyStorePassword, keyStorePath, hostnameVerification, username, password, auth, token);
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getTopic() {
        return topic;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public String getEnableAutoCommit() {
        return enableAutoCommit;
    }

    public Long getMessageCount() {
        return messageCount;
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
}
