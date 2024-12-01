package com.ptit;

import com.ptit.thread.ProcessThreadPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class Start {
    private static final Logger logger = LogManager.getLogger("com.ptit");

    public static void main(String[] args) {
        logger.info("Starting...");
        String topicName = "demo-topic";
        int numConsumers = 5;

        Properties consumerConfig = new Properties();
        consumerConfig.put("bootstrap.servers", "localhost:9092");
        consumerConfig.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerConfig.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerConfig.put("group.id", "consumer-group");
        consumerConfig.put("auto.offset.reset", "earliest");

        ProcessThreadPool consumerThreadPool = new ProcessThreadPool(numConsumers, topicName, consumerConfig);
        consumerThreadPool.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down consumer thread pool...");
            consumerThreadPool.shutdown();
        }));

        logger.info("Kafka Consumer Thread Pool started. Press Ctrl+C to stop.");
    }
}
