package com.microyu.server.servlet;

import com.microyu.server.http.request.HttpRequest;
import com.microyu.server.http.response.HttpResponse;

public interface Servlet {
    void init();

    void destroy();

    void service(HttpRequest request, HttpResponse response);
}
