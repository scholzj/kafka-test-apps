package cz.scholz.kafka.testapps.consumer;

public class KafkaTestConsumerConfig {
    private final String bootstrapServers;
    private final String topic;
    private final String pattern;
    private final String groupId;
    private final String autoOffsetReset = "earliest";
    private final String enableAutoCommit = "false";
    private final Long messageCount;
    private final String sslTruststoreCertificates;
    private final String sslKeystoreKey;
    private final String sslKeystoreCertificateChain;
    private final boolean hostnameVerification;
    private final String username;
    private final String password;
    private final String auth;
    private final String token;

    public KafkaTestConsumerConfig(String bootstrapServers, String topic, String pattern, String groupId,
                                   Long messageCount, String sslTruststoreCertificates, String sslKeystoreKey,
                                   String sslKeystoreCertificateChain, boolean hostnameVerification, String username,
                                   String password, String auth, String token) {
        this.bootstrapServers = bootstrapServers;
        this.topic = topic;
        this.pattern = pattern;
        this.groupId = groupId;
        this.messageCount = messageCount;
        this.sslTruststoreCertificates = sslTruststoreCertificates;
        this.sslKeystoreKey = sslKeystoreKey;
        this.sslKeystoreCertificateChain = sslKeystoreCertificateChain;
        this.hostnameVerification = hostnameVerification;
        this.username = username;
        this.password = password;
        this.auth = auth;
        this.token = token;
    }

    public static KafkaTestConsumerConfig fromEnv() {
        String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
        String topic = System.getenv("TOPIC");
        String pattern = System.getenv("PATTERN");
        String groupId = System.getenv("GROUP_ID");
        Long messageCount = System.getenv("MESSAGE_COUNT") == null ? null : Long.valueOf(System.getenv("MESSAGE_COUNT"));
        String sslTruststoreCertificates = System.getenv("CA_CRT");
        String sslKeystoreKey = System.getenv("USER_KEY");
        String sslKeystoreCertificateChain = System.getenv("USER_CRT");
        boolean hostnameVerification = System.getenv("HOSTNAME_VERIFICATION") == null || Boolean.parseBoolean(System.getenv("HOSTNAME_VERIFICATION"));
        String username = System.getenv("USERNAME") == null ? null : System.getenv("USERNAME");
        String password = System.getenv("PASSWORD") == null ? null : System.getenv("PASSWORD");
        String auth = System.getenv("AUTH") == null ? null : System.getenv("AUTH");
        String token = System.getenv("TOKEN") == null ? null : System.getenv("TOKEN");

        return new KafkaTestConsumerConfig(bootstrapServers, topic, pattern, groupId, messageCount,
                sslTruststoreCertificates, sslKeystoreKey, sslKeystoreCertificateChain, hostnameVerification, username,
                password, auth, token);
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getTopic() {
        return topic;
    }

    public String getPatttern() {
        return pattern;
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

    public String getSslTruststoreCertificates() {
        return sslTruststoreCertificates;
    }

    public String getSslKeystoreKey() {
        return sslKeystoreKey;
    }

    public String getSslKeystoreCertificateChain() {
        return sslKeystoreCertificateChain;
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
