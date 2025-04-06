package com.aiivr.processingHub.processData;

public class ProcessData {

    String url;
    String serviceId;
    String processId;
    long createTimeStamp;

    public ProcessData(String processId,String url, String serviceId ) {
        this.url = url;
        this.serviceId = serviceId;
        this.processId = processId;
        this.createTimeStamp = System.currentTimeMillis();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }
}
