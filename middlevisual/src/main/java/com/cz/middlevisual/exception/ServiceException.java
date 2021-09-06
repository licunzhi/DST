package com.cz.middlevisual.exception;

/**
 * @program: DST
 * @description: 自定义异常
 * @author: Cai.Min
 * @create: 2021-09-06 20:17
 **/
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
