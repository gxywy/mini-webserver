package com.microyu.server.http.request;

import com.microyu.server.http.Cookie;
import com.microyu.server.http.Session;

import java.util.List;
import java.util.Map;

public interface Request {

    String getMethod();

    void setMethod(String method);

    String getRequestURI();

    void setRequestURI(String uri);

    String getProtocol();

    void setProtocol(String protocol);

    String getHost();

    void setHost(String host);

    int getPort();

    void setPort(int port);

    String getContentType();

    void setContentType(String contentType);

    Map<String, String> getParams();

    void addParam(String key, String value);

    Map<String, String> getHeaders();

    void addHeader(String key, String value);

    List<Cookie> getCookies();

    void addCookie(Cookie cookie);

    Session getSession();

    void setSession(Session session);

    int getContentLength();

    void setContentLength(int contentLength);

    String getBody();

    void setBody(String body);
}
