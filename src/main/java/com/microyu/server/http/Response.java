package com.microyu.server.http;

import com.microyu.server.utils.HttpStatus;
import com.microyu.server.utils.HttpVersion;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Response {
    public static final String BLANK = " ";
    public static final String CRLF = "\r\n";
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";

    private StringBuilder header;
    private byte[] byteBody;
    private ArrayList<Cookie> cookies;
    private String contentType;
    private Map<String, String> additionalHeaders;

    private BufferedOutputStream bufferedOutputStream;

    public Response(OutputStream os) {
        header = new StringBuilder();
        contentType = DEFAULT_CONTENT_TYPE;
        bufferedOutputStream = new BufferedOutputStream(os);
    }

    private void buildHeader(HttpStatus status) {
        //构造相应头第一行
        header.append(HttpVersion.HTTP11).append(BLANK);
        header.append(status.getCode()).append(BLANK).append(status).append(CRLF);

        //构造响应头
        header.append("Date:").append(BLANK).append(new Date()).append(CRLF);
        header.append("Content-Type:").append(BLANK).append(contentType).append(CRLF);

        //构造额外响应头
        if(additionalHeaders != null){
            for(Map.Entry<String,String> additionHeader : additionalHeaders.entrySet()) {
                header.append(additionHeader.getKey()).append(":").append(BLANK).append(additionHeader.getValue()).append(CRLF);
            }
        }

        //构造响应头cookie部分
        if (cookies != null) {
            for(Cookie cookie : cookies) {
                header.append("Set-Cookie:").append(BLANK).append(cookie.getKey()).append("=").append(cookie.getValue()).append(CRLF);
            }
        }

        //计算响应内容长度
        header.append("Content-Length:").append(byteBody.length).append(CRLF);
        header.append(CRLF);
    }

    public void pushResponse(HttpStatus status) {
        if (!status.equals(HttpStatus.OK))
            byteBody = new byte[0];

        buildHeader(status);

        byte[] byteHeader = header.toString().getBytes();
        byte[] byteResponse = new byte[byteHeader.length + byteBody.length];
        System.arraycopy(byteHeader, 0, byteResponse, 0, byteHeader.length);
        System.arraycopy(byteBody, 0, byteResponse, byteHeader.length, byteBody.length);

        try {
            bufferedOutputStream.write(byteResponse, 0, byteResponse.length);
            bufferedOutputStream.flush();
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

    public void appendCookie(Cookie cookie) {
        if (cookies == null) {
            cookies = new ArrayList<>();
        }
        cookies.add(cookie);
    }

    public void appendContent(byte[] content, int len) {
        if(byteBody == null) {
            byteBody = new byte[len];
            System.arraycopy(content, 0, byteBody, 0, len);
        } else {
            byte[] newByteBody = new byte[byteBody.length + len];
            System.arraycopy(byteBody, 0, newByteBody, 0, byteBody.length);
            System.arraycopy(content, 0, newByteBody, byteBody.length, len);
            byteBody = newByteBody;
        }
    }

    public void close() {
        try {
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRedirect(String url) {
        appendHeader("Location", url);
        pushResponse(HttpStatus.MOVED_TEMPORARILY);
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
