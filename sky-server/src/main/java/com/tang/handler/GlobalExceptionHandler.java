package com.tang.handler;


import com.tang.exception.BaseException;
import com.tang.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     * @param e 业务异常
     * @return 异常信息
     */
    @ExceptionHandler
    public Result exception(BaseException e){
        log.error("异常信息：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

}
