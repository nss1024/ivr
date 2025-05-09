package com.aiivr.processingHub.subProcessManager;

import com.aiivr.processingHub.logger.Logger;
import com.aiivr.processingHub.processData.ProcessData;
import com.aiivr.processingHub.storage.ProcessStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class PythonProcessManager {
    @Autowired
    private ProcessStorage processStorage;
    private ProcessTimer processTimer;
    boolean processStarted=true;

    @Autowired
    Logger logger;

    public String startProcess(String url, String fileName,String serviceId) {
        String processUUID= UUID.randomUUID().toString().replace("-", "");//generate a unique process id, it will be used in a url so need to remove "-"

        if(url.isBlank()){
            url="local";
        }

        Timer timer = new Timer();
        try {
            System.out.println("File name passed to script: "+ fileName);
            ProcessBuilder processBuilder = new ProcessBuilder("python3", "/home/norbert/PycharmProjects/whisperProject/whispertest.py",processUUID, fileName,serviceId);
            //ProcessBuilder processBuilder = new ProcessBuilder("python3","--version");
            Process process= processBuilder.start();
            timer.schedule(new ProcessTimer(this,processUUID), 5000);
            System.out.println("Process added to storage");

            processStorage.addProcess(processUUID,url,serviceId,fileName);
            AtomicReference<String> confirmationMessage= new AtomicReference<>("");
            //Consume stdout of script
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("PYTHON STDOUT: " + line);
                        logger.logData(processUUID+":"+line);
                        if(line.equals("Processing started")){
                            timer.cancel();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Consume stderr
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println("PYTHON STDERR: " + line);
                        logger.logData(processUUID+":"+line);
                        timer.cancel();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            System.out.println("Python script did not confirm startup!");
            System.out.println(e.getMessage());
            try {
                processStorage.removeProcess(processUUID);
                processStarted=false;
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
