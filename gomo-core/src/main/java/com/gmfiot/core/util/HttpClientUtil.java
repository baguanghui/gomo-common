package com.gmfiot.core.util;

import com.fasterxml.jackson.databind.JavaType;
import com.gmfiot.core.data.Result;

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

    public static <T> T doRequest(String url, Object data,String method,Class<T> tClass){
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
        return JsonUtil.toObject(retJson,tClass);
    }

    public static <T> Result<T> doRequestForResult(String url, Object data,String method,Class<T> tClass){
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
        return JsonUtil.toResult(retJson,tClass);
    }

    public static <T> T doGetRequest(String url,Class<T> tClass){
        return doRequest(url, null,"GET",tClass);
    }

    public static <T> T doPostRequest(String url, Object data,Class<T> tClass){
        return doRequest(url, data,"POST",tClass);
    }

    public static <T> T doPutRequest(String url, Object data,Class<T> tClass){
        return doRequest(url, data,"PUT",tClass);
    }

    public static <T> T doDeleteRequest(String url,Class<T> tClass){
        return doRequest(url,null,"PUT",tClass);
    }

    public static <T> Result<T> doGetRequestForResult(String url,Class<T> tClass){
        return doRequestForResult(url, null,"GET",tClass);
    }

    public static <T> Result<T> doPostRequestForResult(String url, Object data,Class<T> tClass){
        return doRequestForResult(url, data,"POST",tClass);
    }

    public static <T> Result<T> doPutRequestForResult(String url, Object data,Class<T> tClass){
        return doRequestForResult(url, data,"PUT",tClass);
    }

    public static <T> Result<T> doDeleteRequestForResult(String url,Class<T> tClass){
        return doRequestForResult(url,null,"PUT",tClass);
    }
}