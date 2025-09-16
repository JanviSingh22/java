package com.example.OrderUp.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.OrderUp.service.OrderService.placeOrder(..))")
    public Object logOrderProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        logger.info("➡️ Method: {}, Args: {}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));


        Object result;
        try {
            result = joinPoint.proceed(); // proceed with the method
        } finally {
            long timeTaken = System.currentTimeMillis() - start;
            logger.info("✅ Finished order process in {} ms", timeTaken);
        }
        return result;
    }
}
