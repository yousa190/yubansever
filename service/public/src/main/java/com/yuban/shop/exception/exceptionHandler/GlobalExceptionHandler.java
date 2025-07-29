package com.yuban.shop.exception.exceptionHandler;

import com.yuban.shop.exception.SystemException;
import com.yuban.shop.pojo.origin.Result;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Getter
@Setter
@RestControllerAdvice
//拦截全局代码异常,处理
public class GlobalExceptionHandler   {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private String code;

    private String msg;


    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        logger.error("RuntimeException occurred: {}", e.getMessage(), e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public Result handleException(Exception e) {
        logger.error("SystemException occurred: {}", e.getMessage(), e);
        return Result.error(e.getMessage());
    }
}