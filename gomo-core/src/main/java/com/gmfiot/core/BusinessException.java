package com.gmfiot.core;

/**
 * uncheck 业务异常
 * @author BaGuangHui
 */
public class BusinessException extends RuntimeException {
    private int code;
    public BusinessException(){}

    public BusinessException(String message){
        super(message);
    }

    public BusinessException(int code,String message){
        this(message);
        this.code = code;
    }

    public BusinessException(StatusCodeEnum statusCodeEnum ,String message){
        this(message);
        this.code = statusCodeEnum.getCode();
    }

    public int getCode() {
        return code;
    }
}
