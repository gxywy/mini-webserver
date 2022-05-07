package com.microyu.server.servlet;

import com.microyu.server.http.request.HttpRequest;
import com.microyu.server.http.response.HttpResponse;
import com.microyu.server.util.HttpRequestMethodUtil;

public abstract class HttpServlet implements Servlet {
    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        if ("GET".equals(request.getMethod())) {
            doGet(request, response);
        } else if ("POST".equals(request.getMethod())) {
            doPost(request, response);
        } else if ("PUT".equals(request.getMethod())) {
            doPut(request, response);
        } else if ("DELTE".equals(request.getMethod())) {
            doDelete(request, response);
        }
    }

    public void doGet(HttpRequest request, HttpResponse response) {
    }

    public void doPost(HttpRequest request, HttpResponse response) {
    }

    public void doPut(HttpRequest request, HttpResponse response) {
    }

    public void doDelete(HttpRequest request, HttpResponse response) {
    }
}
