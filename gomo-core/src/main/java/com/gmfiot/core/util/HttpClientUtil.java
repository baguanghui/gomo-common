package com.gmfiot.core.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * http工具类
 * @author BaGuangHui
 */
public final class HttpClientUtil {

    private static final HttpClient httpClient  = HttpClient.newHttpClient();

//    static {
//        //httpClient.connectTimeout();
//        var http = HttpClient.newBuilder()
//                .connectTimeout(Duration.ofMillis(5000))
//                .followRedirects(HttpClient.Redirect.NORMAL)
//                .build();
//
//    }

    /**
     * http数据请求
     * @param url 请求地址
     * @param data 发送数据
     * @param method 请求访求
     * @param resultClass 返回结果数据类型
     * @param parametricClass 返回结果泛型类型
     * @param <T> 泛型符号
     * @return
     */
    public static <T> T doRequest(String url, Object data, String method, Class<?> resultClass, Class<?> parametricClass){
        var postJson = JsonUtil.toJson(data);
        if(postJson == null){
            postJson = "";
        }
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type","application/json")
                .timeout(Duration.ofMillis(10000))
                .method(method,HttpRequest.BodyPublishers.ofString(postJson))
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        var retJson = response.body();
        return JsonUtil.toObject(retJson, resultClass,parametricClass);
    }

    /**
     * 发送http GET请求
     * @param url
     * @param resultClass
     * @param <T>
     * @return
     */
    public static <T> T getRequest(String url,Class<T> resultClass){
        return doRequest(url, null,"GET", resultClass,null);
    }


    /**
     * 发送http POST请求
     * @param url
     * @param data
     * @param resultClass
     * @param <T>
     * @return
     */
    public static <T> T postRequest(String url, Object data,Class<T> resultClass){
        return doRequest(url, data,"POST", resultClass,null);
    }

    /**
     * 发送http PUT请求
     * @param url
     * @param data
     * @param resultClass
     * @param <T>
     * @return
     */
    public static <T> T putRequest(String url, Object data,Class<T> resultClass){
        return doRequest(url, data,"PUT", resultClass,null);
    }

    /**
     * 发送http DELETE请求
     * @param url
     * @param resultClass
     * @param <T>
     * @return
     */
    public static <T> T deleteRequest(String url,Class<T> resultClass){
        return doRequest(url,null,"PUT", resultClass,null);
    }

    /**
     * 发送http GET请求
     * @param url
     * @param resultClass
     * @param parametricClass
     * @param <T>
     * @return
     */
    public static <T> T getRequest(String url, Class<?> resultClass, Class<?> parametricClass){
        return doRequest(url, null,"GET", resultClass,parametricClass);
    }

    /**
     * 发送http POST请求
     * @param url
     * @param data
     * @param resultClass
     * @param parametricClass
     * @param <T>
     * @return
     */
    public static <T> T postRequest(String url, Object data, Class<?> resultClass, Class<?> parametricClass){
        return doRequest(url, data,"POST", resultClass,parametricClass);
    }

    /**
     * 发送http PUT请求
     * @param url
     * @param data
     * @param resultClass
     * @param parametricClass
     * @param <T>
     * @return
     */
    public static <T> T putRequest(String url, Object data, Class<?> resultClass, Class<?> parametricClass){
        return doRequest(url, data,"PUT", resultClass,parametricClass);
    }

    /**
     * 发送http DELETE请求
     * @param url
     * @param resultClass
     * @param parametricClass
     * @param <T>
     * @return
     */
    public static <T> T deleteRequest(String url, Class<?> resultClass, Class<?> parametricClass){
        return doRequest(url,null,"PUT", resultClass,parametricClass);
    }
}