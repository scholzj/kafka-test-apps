package cz.scholz.kafka.testapps.consumer;

public class KafkaTestConsumerConfig {
    private final String bootstrapServers;
    private final String topic;
    private final String groupId;
    private final String autoOffsetReset = "earliest";
    private final String enableAutoCommit = "false";
    private final Long messageCount;

    public KafkaTestConsumerConfig(String bootstrapServers, String topic, String groupId, Long messageCount) {
        this.bootstrapServers = bootstrapServers;
        this.topic = topic;
        this.groupId = groupId;
        this.messageCount = messageCount;
    }

    public static KafkaTestConsumerConfig fromEnv() {
        String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
        String topic = System.getenv("TOPIC");
        String groupId = System.getenv("GROUP_ID");
        Long messageCount = System.getenv("MESSAGE_COUNT") == null ? null : Long.valueOf(System.getenv("MESSAGE_COUNT"));

        return new KafkaTestConsumerConfig(bootstrapServers, topic, groupId, messageCount);
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
}
