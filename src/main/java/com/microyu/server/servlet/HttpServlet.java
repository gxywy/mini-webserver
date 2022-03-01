package com.microyu.server.servlet;

import com.microyu.server.http.Request;
import com.microyu.server.http.Response;
import com.microyu.server.utils.HttpRequestMethod;

public abstract class HttpServlet {
    public void service(Request request, Response response) {
        if (request.getMethod() == HttpRequestMethod.GET) {
            doGet(request, response);
        } else if (request.getMethod() == HttpRequestMethod.POST) {
            doPost(request, response);
        } else if (request.getMethod() == HttpRequestMethod.PUT) {
            doPut(request, response);
        } else if (request.getMethod() == HttpRequestMethod.DELETE) {
            doDelete(request, response);
        }
    }

    public void doGet(Request request, Response response) {
    }

    public void doPost(Request request, Response response) {
    }

    public void doPut(Request request, Response response) {
    }

    public void doDelete(Request request, Response response) {
    }
}
