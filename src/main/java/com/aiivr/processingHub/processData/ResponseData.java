package com.aiivr.processingHub.processData;

public class ResponseData {

    String processId;
    String response;
    Long receivedTimestamp;

    public ResponseData(String processId, String response) {
        this.processId = processId;
        this.response = response;
        this.receivedTimestamp = System.currentTimeMillis();
    }

    public String getProcessId() {
        return processId;
    }

    public String getResponse() {
        return response;
    }

    public Long getReceivedTimestamp() {
        return receivedTimestamp;
    }
}
