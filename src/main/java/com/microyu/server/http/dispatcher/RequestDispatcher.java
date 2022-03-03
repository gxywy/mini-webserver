package com.microyu.server.http.dispatcher;

import com.microyu.server.http.Request;
import com.microyu.server.http.Response;

import java.io.IOException;

public interface RequestDispatcher {
    void forward(Request request, Response response) throws IOException;
}
