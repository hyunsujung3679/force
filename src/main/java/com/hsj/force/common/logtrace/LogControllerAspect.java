package com.hsj.force.common.logtrace;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogControllerAspect {

    private final LogTrace logTrace;

    public LogControllerAspect(ThreadLogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("execution(* com.hsj.force..*(..)) && !execution(* com.hsj.force.common.logtrace..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;

        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);
            Object result = joinPoint.proceed();
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
