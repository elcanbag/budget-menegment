package com.example.budgetmanager.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.example.budgetmanager.service.CompositeAuditLogService;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private final CompositeAuditLogService compositeAuditLogService;

    public LoggingAspect(CompositeAuditLogService compositeAuditLogService) {
        this.compositeAuditLogService = compositeAuditLogService;
    }

    @Pointcut("within(com.example.budgetmanager.controller..*)")
    public void controllerMethods() {}

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.getName() != null) ? auth.getName() : "ANONYMOUS";
        String methodName = joinPoint.getSignature().toShortString();
        String details = "Args: " + Arrays.toString(joinPoint.getArgs());
        compositeAuditLogService.saveLog(username, methodName, details);
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.getName() != null) ? auth.getName() : "ANONYMOUS";
        String methodName = joinPoint.getSignature().toShortString();
        String details = "Exception: " + ex.getMessage() + " | Args: " + Arrays.toString(joinPoint.getArgs());
        compositeAuditLogService.saveLog(username, methodName, details);
    }
}
