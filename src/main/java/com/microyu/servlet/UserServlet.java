package com.microyu.servlet;

import com.microyu.server.http.Request;
import com.microyu.server.http.Response;
import com.microyu.server.servlet.HttpServlet;

public class UserServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) {
        super.doGet(request, response);
        String content = "UserServlet";
        response.appendContent(content.getBytes(), content.getBytes().length);
    }
}