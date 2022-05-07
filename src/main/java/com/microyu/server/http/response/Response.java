package com.microyu.server.http.response;

import com.microyu.server.http.Cookie;
import com.microyu.server.util.HttpStatusUtil;

public interface Response {

    void setResponseCode(int status);

    void setContentType(String contentType);

    void addHeader(String key, String value);

    void addCookie(Cookie cookie);

    void addContent(byte[] content);

    void response();

    void write(String content);
}
