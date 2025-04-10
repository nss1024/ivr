package com.aiivr.processingHub.storage;

import com.aiivr.processingHub.processData.ProcessData;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProcessStorage {

   private final ConcurrentHashMap<String, ProcessData> processes = new ConcurrentHashMap<String, ProcessData>();

   public void addProcess(String processId,String url,String serviceId,String fileName){
       processes.put(processId, new ProcessData(processId,url,serviceId,fileName));
   }

   public ProcessData getProcess(String processId) {
       return processes.get(processId);
   }

   public void removeProcess(String processId) {
       processes.remove(processId);
   }

   public Set<String> getkeys(){
       return processes.keySet();
   }

   public boolean hasKey(String key){
       return processes.containsKey(key);
   }


}
