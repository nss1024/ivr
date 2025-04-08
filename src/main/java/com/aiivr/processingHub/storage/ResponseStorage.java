package com.aiivr.processingHub.storage;

import com.aiivr.processingHub.processData.ResponseData;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResponseStorage {

   private final ConcurrentHashMap<String, ResponseData> responses = new ConcurrentHashMap<String, ResponseData>();

   public void addProcess(String processId,String processResponse) {
       responses.put(processId, new ResponseData(processId,processResponse));
   }

   public void removeResponseObject(String processId) {
       responses.remove(processId);
   }

   public String getResponse(String processId){
       String resp=null;
        if(!responses.isEmpty()){
            if(responses.get(processId).getResponse()!=null) {
            resp = responses.get(processId).getResponse();
            responses.remove(processId);
        }
       }
       return resp;
   }

   public Set<String> getKeys(){
       return responses.keySet();
   }

   public ResponseData getReponseObject(String processId){
       return responses.get(processId);
   }

}
