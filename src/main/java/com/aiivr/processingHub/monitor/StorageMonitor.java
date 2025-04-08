package com.aiivr.processingHub.monitor;

import com.aiivr.processingHub.logger.Logger;
import com.aiivr.processingHub.storage.ProcessStorage;
import com.aiivr.processingHub.storage.ResponseStorage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class StorageMonitor {

    @Autowired
    private ProcessStorage processStorage;

    @Autowired
    private ResponseStorage responseStorage;

    @Autowired
    Logger logger;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Value("${storage.cleanup.interval.minutes:10}")
    private int cleanupIntervalMinutes;


    @PostConstruct
    public void init() {
        scheduler.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();

            processStorage.getkeys().forEach(key -> {
                var process = processStorage.getProcess(key);
                if (process != null) {
                    if (now - process.getCreateTimeStamp() > cleanupIntervalMinutes) {
                        processStorage.removeProcess(key);
                        logger.logData("Storage monitor removed process "+key+" due to expired TTL of "+cleanupIntervalMinutes);
                        System.out.println("Storage monitor removed process "+key+"from process store due to expired TTL of "+cleanupIntervalMinutes);
                    }
                } else {
                    // Optional: log that process was already null or removed
                }
            });

            responseStorage.getKeys().forEach(key -> {
                var response = responseStorage.getReponseObject(key);
                if (response != null) {
                    if (now - response.getReceivedTimestamp() > cleanupIntervalMinutes) {
                        responseStorage.removeResponseObject(key);
                        logger.logData("Storage monitor removed process "+key+" due to expired TTL of "+cleanupIntervalMinutes);
                        System.out.println("Storage monitor removed process "+key+"from response store due to expired TTL of "+cleanupIntervalMinutes);
                    }
                } else {
                    // Optional: log that response was already null or removed
                }
            });

        }, cleanupIntervalMinutes, cleanupIntervalMinutes, TimeUnit.MINUTES);

    }

    @PreDestroy
    public void shutDownScheduler() {
        scheduler.shutdown();
    }
}
