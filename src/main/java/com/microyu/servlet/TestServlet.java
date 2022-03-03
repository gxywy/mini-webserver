package com.microyu.servlet;

import com.microyu.server.http.Request;
import com.microyu.server.http.ResourceHandler;
import com.microyu.server.http.Response;
import com.microyu.server.servlet.HttpServlet;

import java.io.IOException;

public class TestServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) {
        try {
            request.getServletContext().setAttribute("hello", "Welcome");
            request.getRequestDispatcher("/hello.html").forward(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
