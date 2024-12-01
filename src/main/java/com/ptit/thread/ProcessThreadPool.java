package com.ptit.thread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProcessThreadPool {
    private static final Logger logger = LogManager.getLogger(ProcessThreadPool.class);
    private final ExecutorService executorService;
    private final List<ProcessThread> consumers;

    public ProcessThreadPool(int numConsumers, String topic, Properties consumerConfig) {
        this.executorService = Executors.newFixedThreadPool(numConsumers);
        this.consumers = new ArrayList<>();

        for (int i = 0; i < numConsumers; i++) {
            ProcessThread consumerThread = new ProcessThread(consumerConfig, topic);
            consumers.add(consumerThread);
        }
    }

    public void start() {
        for (ProcessThread consumerThread : consumers) {
            executorService.submit(consumerThread);

        }
        logger.info("Consumer threads started.");
    }

    public void shutdown() {
        logger.info("Shutting down consumer threads...");

        // Stop all consumers
        for (ProcessThread consumer : consumers) {
            consumer.shutdown();
        }

        // Shutdown the thread pool
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                logger.warn("Forcing shutdown of consumer threads...");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted during shutdown. Forcing shutdown...", e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        logger.info("Consumer thread pool shutdown completed.");
    }
}
