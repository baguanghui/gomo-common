package com.gmfiot.core;

/**
 * uncheck 业务异常
 * @author ThinkPad
 */
public class BusinessException extends RuntimeException {
    public BusinessException(){}
    public BusinessException(String msg){
        super(msg);
    }
}
