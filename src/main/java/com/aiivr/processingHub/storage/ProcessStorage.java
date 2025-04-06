package com.aiivr.processingHub.storage;

import com.aiivr.processingHub.processData.ProcessData;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProcessStorage {

   private final ConcurrentHashMap<String, ProcessData> processes = new ConcurrentHashMap<String, ProcessData>();

   public void addProcess(String processId,String url,String serviceId) {
       processes.put(processId, new ProcessData(processId,url,serviceId));
   }

   public ProcessData getUrl(String processId) {
       return processes.get(processId);
   }

   public void removeProcess(String processId) {
       processes.remove(processId);
   }


}
