package com.microyu.server.servlet;

import com.microyu.server.http.Request;
import com.microyu.server.http.Response;
import com.microyu.server.utils.HttpStatus;

public class ServletHandler implements Runnable {
    private Request request;
    private Response response;
    private HttpServlet servlet;

    public ServletHandler(Request request, Response response, HttpServlet servlet) {
        this.request = request;
        this.response = response;
        this.servlet = servlet;
    }

    @Override
    public void run() {
        try {
            servlet.service(request, response);
            response.pushResponse(HttpStatus.OK);
        } catch (Exception e){
            response.pushResponse(HttpStatus.NOT_FOUND);
            e.printStackTrace();
        }
    }
}
