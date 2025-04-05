package com.aiivr.processingHub.storage;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResponseStorage {

   private final ConcurrentHashMap<String, String> responses = new ConcurrentHashMap<String, String>();

   public void addProcess(String processId,String processResponse) {
       responses.put(processId, processResponse);
   }

   public String getUrl(String processId) {
       return responses.get(processId);
   }

   public void removeProcess(String processId) {
       responses.remove(processId);
   }


}
