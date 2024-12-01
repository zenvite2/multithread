package com.ptit.thread;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ProcessThread extends Thread {
    private static final Logger logger = LogManager.getLogger(ProcessThread.class);
    private final KafkaConsumer<String, String> consumer;
    private volatile boolean running = true;

    public ProcessThread(Properties consumerConfig, String topic) {
        this.consumer = new KafkaConsumer<>(consumerConfig);
        this.consumer.subscribe(Collections.singletonList(topic));
    }

    @Override
    public void run() {
        try {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    logger.info("[{}] Consumed message: key={}, value={}, partition={}, offset={}", Thread.currentThread().getName(), record.key(), record.value(), record.partition(), record.offset());
                }
            }
        } catch (Exception e) {
            logger.error("[{}] Error in consumer: {}", Thread.currentThread().getName(), e.getMessage(), e);
        } finally {
            consumer.close();
            logger.info("[{}] Consumer closed.", Thread.currentThread().getName());
        }
    }

    public void shutdown() {
        running = false;
    }
}
