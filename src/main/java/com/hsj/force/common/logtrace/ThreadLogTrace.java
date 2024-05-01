package com.hsj.force.common.logtrace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ThreadLogTrace implements LogTrace {

    private final static String START_PREFIX = "-->";
    private final static String COMPLETE_PREFIX = "<--";
    private final static String ERROR_PREFIX = "<X-";

    public static ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    @Override
    public TraceStatus begin(String message) {

        long startTimeMs = System.currentTimeMillis();

        syncTraceId();

        TraceId traceId = traceIdHolder.get();

        log.info("[{}] {}{}", traceId.getId(), addSpacer(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, message, startTimeMs);
    }

    private void syncTraceId() {

        TraceId traceId = traceIdHolder.get();

        if (traceId == null) {
            traceIdHolder.set(new TraceId());
        } else {
            traceIdHolder.set(traceId.createNextId());
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    public void complete(TraceStatus status, Exception e) {

        Long lastTimeMs = System.currentTimeMillis();
        Long startTimeMs = status.getStartTimeMs();
        Long resultTime = lastTimeMs - startTimeMs;

        TraceId traceId = status.getTraceId();

        if (e == null) {
            log.info("[{}] {}{} time = {}", traceId.getId(), addSpacer(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTime);
        }else{
            log.info("[{}] {}{} time = {} ex = {}", traceId.getId(), addSpacer(ERROR_PREFIX, traceId.getLevel()), status.getMessage(), resultTime, e.toString());
        }

        releaseTraceId();
    }

    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();

        if (traceId.isFirstLevel()) {
            traceIdHolder.remove();
        } else {
            traceIdHolder.set(traceId.createPreviousId());
        }
    }

    private String addSpacer(String prefix, int level) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1 ? "|" + prefix : "|  "));
        }
        return sb.toString();
    }
}
