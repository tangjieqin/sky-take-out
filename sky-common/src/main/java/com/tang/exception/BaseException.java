package com.tang.exception;


/**
 * 基础异常类
 */
public class BaseException extends RuntimeException {
    public BaseException() {}

    public BaseException(String msg) {
        super(msg);
    }

}
