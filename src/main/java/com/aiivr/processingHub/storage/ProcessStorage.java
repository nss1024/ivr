package com.aiivr.processingHub.storage;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProcessStorage {

   private final ConcurrentHashMap<String, String> processes = new ConcurrentHashMap<String, String>();

   public void addProcess(String processId,String url) {
       processes.put(processId, url);
   }

   public String getUrl(String processId) {
       return processes.get(processId);
   }

   public void removeProcess(String processId) {
       processes.remove(processId);
   }


}
