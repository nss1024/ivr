package com.aiivr.processingHub.resourceManagement;

import com.aiivr.processingHub.fileoperations.RemoveUsedFile;
import com.aiivr.processingHub.storage.ProcessStorage;
import com.aiivr.processingHub.storage.ResponseStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ResourceManager {

    @Autowired
    ProcessStorage processStorage;

    @Autowired
    ResponseStorage responseStorage;

    @Autowired
    RemoveUsedFile removeUsedFile;

    public void clearStorage(String processId){
        if(responseStorage.hasKey(processId)){
            responseStorage.removeResponseObject(processId);
        }

        if(processStorage.hasKey(processId)){
            processStorage.removeProcess(processId);
        }
    }

    public void deleteUsedFile(String fileName){
        removeUsedFile.deleteUsedFile(fileName);
    }

    public void clearStorageAndFile(String processId){
        deleteUsedFile(processStorage.getProcess(processId).getFileName());
        clearStorage(processId);
    }

}
