package com.aiivr.processingHub.logger;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class Logger {
    private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private final String logFilePath = "/pyscripts/logs/";
    private final String fileName = System.currentTimeMillis() + ".txt";
    private Thread logWriterThread;
    private volatile boolean running = false;

    public Logger() {
        // No thread is started here!
    }

    @PostConstruct
    public void init() {
        File logFile = new File(logFilePath + fileName);
        running = true;

        logWriterThread = new Thread(() -> {
            try (PrintWriter pw = new PrintWriter(new FileWriter(logFile, true))) {
                while (running || !logQueue.isEmpty()) {
                    String message = logQueue.poll(1, TimeUnit.SECONDS);
                    if (message != null) {
                        pw.println(message);
                        pw.flush();
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }, "LoggerThread");

        logWriterThread.start();
    }

    public void logData(String message) {
        logQueue.offer(message);
    }

    @PreDestroy
    public void stopLogger() {
        running = false;
        try {
            if (logWriterThread != null) {
                logWriterThread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
