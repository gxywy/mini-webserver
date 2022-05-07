package com.microyu.testservlet;

import com.microyu.server.handler.TemplateHandler;
import com.microyu.server.http.request.HttpRequest;
import com.microyu.server.http.response.HttpResponse;
import com.microyu.server.servlet.HttpServlet;
import com.microyu.server.servlet.ServletContext;

import java.io.IOException;

public class TestServlet extends HttpServlet {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        try {
            ServletContext.getServletContext().setAttribute("hello", "Welcome");
            TemplateHandler.handle("/hello.html", request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
