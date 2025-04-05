package com.aiivr.processingHub.subProcessManager;

import com.aiivr.processingHub.storage.ProcessStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PythonProcessManager {
    @Autowired
    private ProcessStorage processStorage;
    private ProcessTimer processTimer;
    boolean processStarted=false;

    public String startProcess(String url, String fileName) {
        String processUUID= UUID.randomUUID().toString().replace("-", "");//generate a unique process id, it will be used in a url so need to remove "-"

        if(url.equals(null)){
            url="local";
        }

        Timer timer = new Timer();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python3", "/pyscripts/local_vosk_mic.py",processUUID, fileName);
            Process process= processBuilder.start();
            timer.schedule(new ProcessTimer(this,processUUID), 5000);
            System.out.println("Process added to storage");
            processStorage.addProcess(processUUID,url);

            // Capture confirmation message
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String confirmationMessage = reader.readLine();

            if (confirmationMessage != null && confirmationMessage.contains("Processing started")) {
                System.out.println("Python script confirmed startup.");
                processStarted=true;
                timer.cancel();
            } else {
                System.err.println("Python script did not confirm startup!");
            }
        } catch (IOException e) {
            System.out.println("Python script did not confirm startup!");
            System.out.println(e.getMessage());
            try {
                processStorage.removeProcess(processUUID);
                System.out.println("Failed Python script removed from process storage: " + processUUID);
            }catch (NullPointerException npe){
                System.out.println("Python script did not confirm startup! Process not store!");
            }

        }
        if(processStarted){ return processUUID;}
        else{return "process_error";}
    }

    public void removeProcess(String processUUID){
        processStorage.removeProcess(processUUID);
    }

    public boolean isProcessStarted() {
        return processStarted;
    }

}
