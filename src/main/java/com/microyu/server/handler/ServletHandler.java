package com.microyu.server.handler;

import com.microyu.server.http.request.HttpRequest;
import com.microyu.server.http.response.HttpResponse;
import com.microyu.server.servlet.HttpServlet;
import com.microyu.server.util.HttpStatusUtil;

public class ServletHandler {

    public static void handle(HttpRequest request, HttpResponse response, HttpServlet servlet) {
        try {
            if (servlet == null) {
                response.setResponseCode(404);
            } else {
                servlet.service(request, response);
                response.setResponseCode(200);
            }
        } catch (Exception e){
            response.setResponseCode(404);
            e.printStackTrace();
        }
    }
}
