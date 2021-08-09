package com.microyu.server;

import com.microyu.server.utils.HttpStatus;
import com.microyu.server.utils.HttpVersion;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Response {
    public static final String BLANK = " ";
    public static final String CRLF = "\r\n";
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";

    private StringBuilder header;
    private StringBuilder body;
    private StringBuilder response;
    private Map<String, String> cookies;
    private Map<String, String> additionalHeaders;

    private BufferedWriter bufferedWriter;

    public Response(OutputStream os) {
        header = new StringBuilder();
        body = new StringBuilder();
        response = new StringBuilder();
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
    }

    private void buildHeader(HttpStatus status) {
        //构造相应头第一行
        header.append(HttpVersion.HTTP11).append(BLANK);
        header.append(status.getCode()).append(BLANK).append(status).append(CRLF);

        //构造响应头
        header.append("Date:").append(BLANK).append(new Date()).append(CRLF);
        header.append("Content-Type:").append(BLANK).append(DEFAULT_CONTENT_TYPE).append(CRLF);

        //构造额外响应头
        if(additionalHeaders != null){
            for(Map.Entry<String,String> additionHeader : additionalHeaders.entrySet()) {
                header.append(additionHeader.getKey()).append(":").append(BLANK).append(additionHeader.getValue()).append(CRLF);
            }
        }

        //构造响应头cookie部分
        if (cookies != null) {
            for(Map.Entry<String,String> cookie : cookies.entrySet()) {
                header.append("Set-Cookie:").append(BLANK).append(cookie.getKey()).append("=").append(cookie.getValue()).append(CRLF);
            }
        }

        //计算响应内容长度
        header.append("Content-Length:").append(body.toString().getBytes().length).append(CRLF);;
    }

    public void pushResponse(HttpStatus status) {
        buildHeader(status);

        response.append(header);
        response.append(CRLF);
        response.append(body);

        try {
            bufferedWriter.write(response.toString(), 0, response.toString().length());
            bufferedWriter.flush();
            //不能关流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendHeader(String key, String value) {
        if (additionalHeaders == null) {
            additionalHeaders = new HashMap<>();
        }
        additionalHeaders.put(key, value);
    }

    public void appendCookie(String key, String value) {
        if (cookies == null) {
            cookies = new HashMap<>();
        }
        cookies.put(key, value);
    }


    public void appendContent(String content) {
        body.append(content);
    }

    public void close() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResponseMessage() {
        return response.toString();
    }
}
