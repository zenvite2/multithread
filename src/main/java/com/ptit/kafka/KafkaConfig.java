package com.ptit.kafka;

import java.util.Properties;

public class KafkaConfig {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static Properties getProducerConfig() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

    public static Properties getConsumerConfig(String groupId) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", groupId);
        properties.put("auto.offset.reset", "earliest");
        return properties;
    }
}
