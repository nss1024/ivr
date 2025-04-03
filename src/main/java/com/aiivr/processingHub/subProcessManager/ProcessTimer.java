package com.aiivr.processingHub.subProcessManager;

import com.aiivr.processingHub.storage.ProcessStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class ProcessTimer extends TimerTask {
    String processId;
    PythonProcessManager processManager;

    public ProcessTimer() {}

    public ProcessTimer(PythonProcessManager pythonProcessManager, String processUUID) {
        this.processManager = pythonProcessManager;
        this.processId = processUUID;
    }

    @Override
    public void run() {
        System.out.println("Timer started");
        if(!processManager.isProcessStarted()) {
            System.out.println("Process has not started");
            System.out.println("Stopping process ID: " + processId);
            processManager.removeProcess(processId);
        }
    }
}
