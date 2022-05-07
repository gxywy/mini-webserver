package com.microyu.server.http.response;

import com.microyu.server.http.Cookie;
import com.microyu.server.util.HttpContentTypeUtil;
import com.microyu.server.util.HttpStatusUtil;
import com.microyu.server.util.ServerConfigUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

public class HttpResponse implements Response {
    public static final String blank = " ";
    public static final String lineSeparator = System.getProperty("line.separator");
    private SocketChannel channel;

    private int responseCode;
    private String contentType = HttpContentTypeUtil.HTML;;
    private Map<String, String> headers = new HashMap<>();
    private List<Cookie> cookies = new ArrayList<>();
    private StringBuilder responseHeaderBuilder = new StringBuilder();
    private byte[] responseBodyBytes;

    public HttpResponse() {
        initDefaultValue();
    }

    public HttpResponse(SocketChannel channel) {
        this.channel = channel;
        initDefaultValue();
    }

    public void initDefaultValue() {
        addHeader("Date", String.valueOf(new Date()));
        addHeader("Server", "MiniServer");
        addHeader("Connection", "keep-alive");
        addHeader("Content-Type", contentType);
    }

    @Override
    public void write(String content) {
        ByteBuffer buffer = Charset.forName(ServerConfigUtil.getDefaultCharset()).encode(content);
        try {
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(byte[] bytes) {
        if (bytes == null) {
            return;
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try {
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void response() {
        // 写入响应首行
        responseHeaderBuilder.append("HTTP/1.1").append(blank);
        responseHeaderBuilder.append(responseCode).append(blank).append(HttpStatusUtil.getStatus(responseCode));
        responseHeaderBuilder.append(lineSeparator);

        // 写入全部header
        //addHeader("Content-Length", String.valueOf(contentBuilder.toString().getBytes().length));
        for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
            responseHeaderBuilder.append(headerEntry.getKey()).append(":").append(blank).append(headerEntry.getValue());
            responseHeaderBuilder.append(lineSeparator);
        }

        // 写入全部cookie
        for(Cookie cookie : cookies) {
            String cookieContent = buildCookies(cookie);
            responseHeaderBuilder.append(cookieContent);
            responseHeaderBuilder.append(lineSeparator);
        }

        // 写入一个空白行
        responseHeaderBuilder.append(lineSeparator);

        // 写出请求头
        write(responseHeaderBuilder.toString());

        // 写入响应体
        write(responseBodyBytes);
    }

    private String buildCookies(Cookie cookie) {
        StringBuilder cookieBuilder = new StringBuilder("Set-Cookie:").append(blank);
        // 设置cookie的key和value
        cookieBuilder.append(cookie.getKey());
        cookieBuilder.append("=");
        cookieBuilder.append(cookie.getValue());
        cookieBuilder.append("; ");
        // 设置cookie的过期时间
        long age = cookie.getAge();
        if (age > -1) {
            long expiresTimeStamp = System.currentTimeMillis() + age;
            cookieBuilder.append("Expires=");
            cookieBuilder.append(new Date(expiresTimeStamp));
            cookieBuilder.append("; ");
        }
        // 设置path
        String path = cookie.getPath();
        if (!(path != null || path.isEmpty())) {
            cookieBuilder.append("Path=");
            cookieBuilder.append(path);
            cookieBuilder.append("; ");
        }
        // 设置domain
        String domain = cookie.getDomain();
        if (!(domain != null || domain.isEmpty())) {
            cookieBuilder.append("Domain=");
            cookieBuilder.append(domain);
            cookieBuilder.append("; ");
        }
        // http only
        cookieBuilder.append("HttpOnly");

        return cookieBuilder.toString();
    }

    @Override
    public void setResponseCode(int status) {
        responseCode = status;
    }

    @Override
    public void setContentType(String contentType) {
        addHeader("Content-Type", contentType);
    }

    @Override
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    @Override
    public void addContent(byte[] content) {
        int len = content.length;
        if(responseBodyBytes == null) {
            responseBodyBytes = new byte[len];
            System.arraycopy(content, 0, responseBodyBytes, 0, len);
        } else {
            byte[] newByteBody = new byte[responseBodyBytes.length + len];
            System.arraycopy(responseBodyBytes, 0, newByteBody, 0, responseBodyBytes.length);
            System.arraycopy(content, 0, newByteBody, responseBodyBytes.length, len);
            responseBodyBytes = newByteBody;
        }
    }

    public void addContent(String content) {
        addContent(content.getBytes());
    }
}
