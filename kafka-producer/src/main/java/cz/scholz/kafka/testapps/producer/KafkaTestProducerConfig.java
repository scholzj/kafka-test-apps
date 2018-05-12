package cz.scholz.kafka.testapps.producer;

public class KafkaTestProducerConfig {
    private final String bootstrapServers;
    private final String topic;
    private final int timer;
    private final int numberOfKeys;
    private final Long messageCount;
    private String acks = "1";

    public KafkaTestProducerConfig(String bootstrapServers, String topic, int timer, int numberOfKeys, Long messageCount) {
        this.bootstrapServers = bootstrapServers;
        this.topic = topic;
        this.timer = timer;
        this.numberOfKeys = numberOfKeys;
        this.messageCount = messageCount;
    }

    public static KafkaTestProducerConfig fromEnv() {
        String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
        String topic = System.getenv("TOPIC");
        int timer = Integer.valueOf(System.getenv("TIMER"));
        int numberOfKeys = Integer.parseInt(System.getenv("NUMBER_OF_KEYS") == null ? "1" : System.getenv("NUMBER_OF_KEYS"));
        Long messageCount = System.getenv("MESSAGE_COUNT") == null ? null : Long.valueOf(System.getenv("MESSAGE_COUNT"));

        return new KafkaTestProducerConfig(bootstrapServers, topic, timer, numberOfKeys, messageCount);
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
}
