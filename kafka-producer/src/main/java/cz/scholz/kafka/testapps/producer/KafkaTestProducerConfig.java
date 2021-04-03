package cz.scholz.kafka.testapps.producer;

public class KafkaTestProducerConfig {
    private final String bootstrapServers;
    private final String topic;
    private final int timer;
    private final int numberOfKeys;
    private final Long messageCount;
    private final String acks;
    private final String sslTruststoreCertificates;
    private final String sslKeystoreKey;
    private final String sslKeystoreCertificateChain;
    private final boolean hostnameVerification;
    private final String username;
    private final String password;
    private final String auth;
    private final String token;
    private final String message;

    public KafkaTestProducerConfig(String bootstrapServers, String topic, int timer, int numberOfKeys, Long messageCount,
                                   String acks, String sslTruststoreCertificates, String sslKeystoreKey, String sslKeystoreCertificateChain,
                                   boolean hostnameVerification, String username, String password, String auth, String token, String message) {
        this.bootstrapServers = bootstrapServers;
        this.topic = topic;
        this.timer = timer;
        this.numberOfKeys = numberOfKeys;
        this.messageCount = messageCount;
        this.acks = acks;
        this.sslTruststoreCertificates = sslTruststoreCertificates;
        this.sslKeystoreKey = sslKeystoreKey;
        this.sslKeystoreCertificateChain = sslKeystoreCertificateChain;
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
        int timer = Integer.parseInt(System.getenv("TIMER"));
        int numberOfKeys = Integer.parseInt(System.getenv("NUMBER_OF_KEYS") == null ? "1" : System.getenv("NUMBER_OF_KEYS"));
        Long messageCount = System.getenv("MESSAGE_COUNT") == null ? null : Long.valueOf(System.getenv("MESSAGE_COUNT"));
        String acks = System.getenv("ACKS") == null ? "1" : System.getenv("ACKS");
        String sslTruststoreCertificates = System.getenv("CA_CRT");
        String sslKeystoreKey = System.getenv("USER_KEY");
        String sslKeystoreCertificateChain = System.getenv("USER_CRT");
        boolean hostnameVerification = System.getenv("HOSTNAME_VERIFICATION") == null || Boolean.parseBoolean(System.getenv("HOSTNAME_VERIFICATION"));
        String username = System.getenv("USERNAME") == null ? null : System.getenv("USERNAME");
        String password = System.getenv("PASSWORD") == null ? null : System.getenv("PASSWORD");
        String auth = System.getenv("AUTH") == null ? null : System.getenv("AUTH");
        String token = System.getenv("TOKEN") == null ? null : System.getenv("TOKEN");
        String message = System.getenv("MESSAGE") == null ? "Hello World" : System.getenv("MESSAGE");

        return new KafkaTestProducerConfig(bootstrapServers, topic, timer, numberOfKeys, messageCount, acks,
                sslTruststoreCertificates, sslKeystoreKey, sslKeystoreCertificateChain, hostnameVerification, username,
                password, auth, token, message);
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

    public String getMessage() {
        return message;
    }
}
