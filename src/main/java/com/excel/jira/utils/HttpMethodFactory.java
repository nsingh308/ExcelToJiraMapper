package com.excel.jira.utils;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpMessage;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import com.excel.jira.application.JiraRestClientService;


public class HttpMethodFactory {

    public static HttpGet createGetMethod(URI uri){
        if(uri == null) return null;
        HttpGet method = new HttpGet(uri);
        setHeader(method);
        configProxy(method);
        return method;
    }



    public static HttpGet createHttpGetForFile(URI uri){
        if(uri == null) return null;
        HttpGet method = new HttpGet(uri);
        configProxy(method);
        method.addHeader (HttpHeaders.ACCEPT,
                MediaType.APPLICATION_OCTET_STREAM);
        return method;
    }

    public static HttpPost createPostMethod(URI uri, String body) {
        if(uri == null) return null;
        HttpPost method = new HttpPost(uri);
        setHeader(method);
        configProxy(method);
        StringEntity entity = new StringEntity(body, CharEncoding.UTF_8);
        method.setEntity(entity);
        return method;
    }

    public static HttpPut createPutMethod(URI uri, String body) {
        if(uri == null) return null;
        HttpPut method = new HttpPut(uri);
        setHeader(method);
        configProxy(method);
        StringEntity entity = new StringEntity(body, CharEncoding.UTF_8);
        method.setEntity(entity);
        return method;
    }

    private static void setHeader(HttpMessage httpMessage){
        httpMessage.addHeader(new BasicHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
        httpMessage.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
    }

    private static void configProxy(HttpRequestBase method) {
        RequestConfig requestConfig = JiraRestClientService.getRequestConfig();
        if(requestConfig != null){
            method.setConfig(requestConfig);
        }
    }
}

