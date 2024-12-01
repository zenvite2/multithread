package com.ptit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class Test {
    private static Logger logger = LogManager.getLogger(Test.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka broker address
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Key Serializer
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // Value Serializer
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // Ensure all replicas acknowledge the message (strong durability)

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        try {
            String topic = "demo-topic";
            String key = "key1";
            String value = "Hello, Kafka!";

            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

            // Send the message asynchronously
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    System.out.println("Message sent successfully to " + metadata.topic() + " at offset " + metadata.offset());
                } else {
                    logger.error("Error occurred while sending message", exception);
                }
            });

        } catch (Exception e) {
            logger.error("Error occurred while sending message", e);
        } finally {
            logger.info("Producer closed");
            producer.close();
        }
    }
}
