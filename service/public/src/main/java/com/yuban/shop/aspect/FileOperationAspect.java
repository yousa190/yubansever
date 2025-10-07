package com.yuban.shop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 文件操作切面
 * 用于监控文件上传和下载操作
 */
@Aspect
@Component
public class FileOperationAspect {

    private static final Logger logger = LoggerFactory.getLogger(FileOperationAspect.class);

    /**
     * 监控文件上传操作
     */
    @Around("execution(* com.yuban.shop.controller.UploadController.*(..))")
    public Object monitorUploadOperations(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.info("开始执行文件上传操作: {}.{}", className, methodName);
        
        long startTime = System.currentTimeMillis();
        Object result;
        
        try {
            result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("文件上传操作执行成功: {}.{} 耗时 {} ms", className, methodName, executionTime);
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("文件上传操作执行失败: {}.{} 耗时 {} ms，错误信息: {}", 
                    className, methodName, executionTime, e.getMessage(), e);
            throw e;
        }
        
        return result;
    }

    /**
     * 监控文件下载操作
     */
    @Around("execution(* com.yuban.shop.controller.DownloadController.*(..))")
    public Object monitorDownloadOperations(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.info("开始执行文件下载操作: {}.{}", className, methodName);
        
        long startTime = System.currentTimeMillis();
        Object result;
        
        try {
            result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("文件下载操作执行完成: {}.{} 耗时 {} ms", className, methodName, executionTime);
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("文件下载操作执行失败: {}.{} 耗时 {} ms，错误信息: {}", 
                    className, methodName, executionTime, e.getMessage(), e);
            throw e;
        }
        
        return result;
    }
}