package com.aiivr.processingHub;

import com.aiivr.processingHub.fileoperations.RemoveUsedFile;
import com.aiivr.processingHub.logger.Logger;
import com.aiivr.processingHub.monitor.StorageMonitor;
import com.aiivr.processingHub.resourceManagement.ResourceManager;
import com.aiivr.processingHub.storage.ProcessStorage;
import com.aiivr.processingHub.storage.ResponseStorage;
import com.aiivr.processingHub.subProcessManager.PythonProcessManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.Soundbank;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;

@RestController()
@RequestMapping("/api/v1/")
public class MainController {


    @Autowired
    PythonProcessManager pythonProcessManager;

    @Autowired
    ProcessStorage processStorage;

    @Autowired
    ResponseStorage responseStorage;

    @Autowired
    StorageMonitor storageMonitor;

    @Autowired
    ResourceManager resourceManager;

    @Autowired
    Logger logger;


    //Requests in from Lus -> Java hub receives requests from Lua
    @GetMapping("/processAudio/{fileName}/{responseUrl}/{serviceId}")
    public String processAudio(@PathVariable String fileName, @PathVariable String responseUrl, @PathVariable String serviceId) {
        //File name to be passed to the python script
        //need to generate a task id and construct post mapping based on the task id
        return pythonProcessManager.startProcess(responseUrl,fileName,serviceId);
    }

    @GetMapping("/processAudio/{filename}/{serviceId}")
    public String processAudioResponse(@PathVariable String filename, @PathVariable String serviceId){
        return pythonProcessManager.startProcess("",filename,serviceId);
    }

    //Requests in from Python -> Once processing complete, Python sends request to Java
    @PostMapping("/processingComplete/{processId}")
    public void processComplete(@PathVariable String processId, @RequestBody String response) {
        //process Python response and forward response to relevant url
           responseStorage.addProcess(processId,response);
    }

    //Return available response to Lua
    @GetMapping("/responseReady/{processId}")
    public String responseReady(@PathVariable String processId){

        if(responseStorage.getResponse(processId)!=null){
            String result =responseStorage.getResponse(processId);
                try {//try to clear up resources
                    resourceManager.clearStorageAndFile(processId);
                }catch(Exception e){
                    System.out.println("Failed to clear uo resources!");
                    logger.logData("Failed to clear uo resources!");
                    logger.logData(e.toString());
                }
            return result;

        }else{
            System.out.println("Response not ready ");
            return "no_response";
        }
    }


    //Test endpoint
    @GetMapping("/echotest/{arg}")
    public String echotest(@PathVariable String arg){
        System.out.println(arg);
        return arg;
    }



}
