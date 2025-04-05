package com.aiivr.processingHub;

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

    //Requests in from Lus -> Java hub receives requests from Lua
    @GetMapping("/processAudio/{fileName}/{responseUrl}")
    public String processAudio(@PathVariable String fileName, @PathVariable String responseUrl) {
        //File name to be passed to the python script
        //need to generate a task id and construct post mapping based on the task id
        return pythonProcessManager.startProcess(responseUrl,fileName);
    }

    @GetMapping("/processAudio/{filename}")
    public String processAudioResponse(@PathVariable String filename){
        return pythonProcessManager.startProcess(null,filename);
    }

    //Requests in from Pythin -> Once processing complete, Python sends request to Java
    @PostMapping("/processingComplete/{processId}")
    public void processComplete(@PathVariable String processId, @RequestBody String response) {
        //process Python response and forward response to relevant url
            responseStorage.addProcess(processId,response);
    }

    //Test endpoint
    @GetMapping("/echotest/{arg}")
    public String echotest(@PathVariable String arg){
        System.out.println(arg);
        return arg;
    }



}
