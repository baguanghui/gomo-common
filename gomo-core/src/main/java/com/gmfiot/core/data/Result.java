package com.gmfiot.core.data;

import com.gmfiot.core.StatusCodeEnum;

import java.io.Serializable;

/**
 * @author ThinkPad
 */
public class Result<T> implements Serializable {
    private int statusCode;
    private String message;
    private T data;
    private Boolean success;

    public Result(){}

    public Result(int statusCode,String message){
        this(statusCode,message,null);
    }

    public Result(int statusCode,String message,T data){
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        if(statusCode == StatusCodeEnum.SUCCESS.getCode()){
            this.success = true;
        }else {
            this.success = false;
        }
    }

    public static <T> Result<T> success(T data){
        var result = new Result<T>(StatusCodeEnum.SUCCESS.getCode(),"Ok",data);
        return result;
    }

    public static Result fail(String msg){
        return fail(400,msg);
    }

    public static Result fail(int code, String msg){
        Result ret = new Result(code,msg);
        return ret;
    }

    /**
     * 错误码，对应{@link StatusCodeEnum}，表示一种错误类型
     * 如果是成功，则code为200
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * 对错误的具体解释
     */
    public String getMessage() {
        return message;
    }

    /**
     * 返回的结果包装在value中，value可以是单个对象
     */
    public T getData() {
        return data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "Result{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", success=" + success +
                '}';
    }
}


