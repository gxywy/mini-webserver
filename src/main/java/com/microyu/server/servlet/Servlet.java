package com.microyu.server.servlet;

import com.microyu.server.http.Request;
import com.microyu.server.http.Response;

public interface Servlet {
    void init();

    void destroy();

    void service(Request request, Response response);
}
