package com.aiivr.processingHub;

import com.aiivr.processingHub.storage.ProcessStorage;
import com.aiivr.processingHub.subProcessManager.PythonProcessManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/")
public class MainController {


    @Autowired
    PythonProcessManager pythonProcessManager;

    @Autowired
    ProcessStorage processStorage;

    @GetMapping("/processAudio/{fileName}/{responseUrl}")
    public String processAudio(@PathVariable String fileName, @PathVariable String responseUrl) {
        //File name to be passed to the python script
        //need to generate a task id and construct post mapping based on the task id
        pythonProcessManager.startProcess(responseUrl,fileName);
        return "Hello World";
    }

    @PostMapping("/processingComplete/{processId}")
    public void processComplete(@PathVariable String processId, @RequestBody String response) {
        //process Python response and forward response to relevant url
        System.out.println(response);
    }

    @GetMapping("/echotest/{arg}")
    public String echotest(@PathVariable String arg){
        System.out.println(arg);
        return arg;
    }



}
