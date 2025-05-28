package org.baklansbalkan.HeadacheChecker.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

@Component
@Aspect
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("@annotation(org.baklansbalkan.HeadacheChecker.aspect.annotation.CustomLogging)")
    public void loggingBefore(JoinPoint joinPoint) {
        logger.info("Log before executing method {}", joinPoint.getSignature().getName());
    }

    @Around("@annotation(org.baklansbalkan.HeadacheChecker.aspect.annotation.CustomLogging)")
    public Object trackingAdvice(ProceedingJoinPoint joinPoint) {
        logger.info("Tracking method {}", joinPoint.getSignature().getName());
        long start = System.currentTimeMillis();
        Object proceeded;
        try {
            proceeded = joinPoint.proceed();
            long finish = System.currentTimeMillis();
            logger.info("Execution time for method {}, time = {}", joinPoint.getSignature().getName(), finish - start);
            return proceeded;
        } catch (Throwable e) {
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    @AfterThrowing(
            pointcut = "@annotation(org.baklansbalkan.HeadacheChecker.aspect.annotation.CustomLogging)",
            throwing = "exception")
    public void loggingAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception caught at {}", joinPoint.getSignature().getName());
        logger.error("Exception type is {}", exception.getClass().getName());
    }

    @AfterReturning(
            pointcut = "@annotation(org.baklansbalkan.HeadacheChecker.aspect.annotation.CustomLogging)",
            returning = "result")
    public void handlingResult(JoinPoint joinPoint, Object result) {
        logger.info("Calling method {}", joinPoint.getSignature().getName());
        logger.info("Result saved: {}", result.toString());
    }
}
