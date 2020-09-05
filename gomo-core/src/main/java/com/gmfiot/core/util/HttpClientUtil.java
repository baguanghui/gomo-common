package com.gmfiot.core.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpClientUtil {

    private static final HttpClient httpClient  = HttpClient.newHttpClient();

//    static {
//        //httpClient.connectTimeout();
//        var http = HttpClient.newBuilder()
//                .connectTimeout(Duration.ofMillis(5000))
//                .followRedirects(HttpClient.Redirect.NORMAL)
//                .build();
//
//    }

    public static <T> T doRequest(String url, Object data,String method,Class<?> clazz,Class<?> parametricClass){
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
        return JsonUtil.toObject(retJson,clazz,parametricClass);
    }

    public static <T> T getRequest(String url,Class<T> clazz){
        return doRequest(url, null,"GET", clazz,null);
    }

    public static <T> T postRequest(String url, Object data,Class<T> clazz){
        return doRequest(url, data,"POST", clazz,null);
    }

    public static <T> T putRequest(String url, Object data,Class<T> clazz){
        return doRequest(url, data,"PUT", clazz,null);
    }

    public static <T> T deleteRequest(String url,Class<T> clazz){
        return doRequest(url,null,"PUT", clazz,null);
    }

    public static <T> T getRequest(String url,Class<?> clazz,Class<?> parametricClass){
        return doRequest(url, null,"GET", clazz,parametricClass);
    }

    public static <T> T postRequest(String url, Object data,Class<?> clazz,Class<?> parametricClass){
        return doRequest(url, data,"POST", clazz,parametricClass);
    }

    public static <T> T putRequest(String url, Object data,Class<?> clazz,Class<?> parametricClass){
        return doRequest(url, data,"PUT", clazz,parametricClass);
    }

    public static <T> T deleteRequest(String url,Class<?> clazz,Class<?> parametricClass){
        return doRequest(url,null,"PUT", clazz,parametricClass);
    }
}