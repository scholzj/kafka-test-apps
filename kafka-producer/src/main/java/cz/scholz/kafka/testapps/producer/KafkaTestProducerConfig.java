package cz.scholz.kafka.testapps.producer;

public class KafkaTestProducerConfig {
    private final String bootstrapServers;
    private final String topic;
    private final int timer;
    private String acks = "1";

    public KafkaTestProducerConfig(String bootstrapServers, String topic, int timer) {
        this.bootstrapServers = bootstrapServers;
        this.topic = topic;
        this.timer = timer;
    }

    public static KafkaTestProducerConfig fromEnv() {
        String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
        String topic = System.getenv("TOPIC");
        int timer = Integer.valueOf(System.getenv("TIMER"));

        return new KafkaTestProducerConfig(bootstrapServers, topic, timer);
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

    public String getAcks() {
        return acks;
    }
}
