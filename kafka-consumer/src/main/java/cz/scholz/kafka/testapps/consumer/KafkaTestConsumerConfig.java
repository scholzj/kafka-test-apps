package cz.scholz.kafka.testapps.consumer;

public class KafkaTestConsumerConfig {
    private final String bootstrapServers;
    private final String topic;
    private final String groupId;
    private String autoOffsetReset = "earliest";
    private String enableAutoCommit = "false";

    public KafkaTestConsumerConfig(String bootstrapServers, String topic, String groupId) {
        this.bootstrapServers = bootstrapServers;
        this.topic = topic;
        this.groupId = groupId;
    }

    public static KafkaTestConsumerConfig fromEnv() {
        String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
        String topic = System.getenv("TOPIC");
        String groupId = System.getenv("GROUP_ID");

        return new KafkaTestConsumerConfig(bootstrapServers, topic, groupId);
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
}
