package com.microyu.testservlet;

import com.microyu.server.http.request.HttpRequest;
import com.microyu.server.http.response.HttpResponse;
import com.microyu.server.servlet.HttpServlet;

public class UserServlet extends HttpServlet {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        super.doGet(request, response);
        String content = "UserServlet";
        response.addContent(content);
    }
}