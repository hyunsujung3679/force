package com.hsj.force.config.logtrace;

public class TraceStatus {

    private TraceId traceId;
    private String message;
    private Long startTimeMs;

    public TraceStatus(TraceId traceId, String message, Long startTimeMs) {
        this.traceId = traceId;
        this.message = message;
        this.startTimeMs = startTimeMs;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public void setTraceId(TraceId traceId) {
        this.traceId = traceId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public void setStartTimeMs(Long startTimeMs) {
        this.startTimeMs = startTimeMs;
    }

}
