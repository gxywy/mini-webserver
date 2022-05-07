package com.microyu.server.http.request;

import com.microyu.server.http.Cookie;
import com.microyu.server.http.Session;
import com.microyu.server.util.HttpRequestMethodUtil;
import com.microyu.server.util.HttpVersionUtil;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest implements Request {
    private SocketChannel channel;
    private String method;
    private String uri;
    private String protocol;
    private String host;
    private int port;
    private String contentType;
    private int contentLength;
    private String body;
    private Map<String, String> parameters = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();
    private List<Cookie> cookies= new ArrayList<>();
    private Session session;

    public HttpRequest() {
        initDefaultValue();
    }

    public void initDefaultValue() {
        method = "GET";
        protocol = "HTTP/1.1";
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getRequestURI() {
        return uri;
    }

    @Override
    public void setRequestURI(String uri) {
        this.uri = uri;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }

    @Override
    public void addParam(String key, String value) {
        this.parameters.put(key, value);
    }

    public void addAllParams(Map<String, String> params) {
        this.parameters.putAll(params);
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    @Override
    public List<Cookie> getCookies() {
        return cookies;
    }

    @Override
    public void addCookie(Cookie cookie) {
        this.cookies.add(cookie);
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpRequest{");
        sb.append("socketChannel=").append(channel);
        sb.append(", uri='").append(uri).append('\'');
        sb.append(", method=").append(method);
        sb.append(", parms=").append(parameters);
        sb.append(", headers=").append(headers);
        sb.append(", host='").append(host).append('\'');
        sb.append(", port='").append(port).append('\'');
        sb.append(", protocolVersion='").append(protocol).append('\'');
        sb.append(", contentLength=").append(contentLength);
        sb.append(", contentType=").append(contentType);
        sb.append(", body='").append(body).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
