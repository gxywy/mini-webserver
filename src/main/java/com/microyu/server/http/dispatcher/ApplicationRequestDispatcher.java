package com.microyu.server.http.dispatcher;

import com.microyu.server.http.Request;
import com.microyu.server.http.ResourceHandler;
import com.microyu.server.http.Response;
import com.microyu.server.template.TemplateResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ApplicationRequestDispatcher implements RequestDispatcher {
    private String url;

    public ApplicationRequestDispatcher(String url) {
        this.url = url;
    }

    @Override
    public void forward(Request request, Response response) throws IOException {
        String content = TemplateResolver.resolve(new String(ResourceHandler.getByteFromFile(url)), request);
        Path path = ResourceHandler.getPathFromFile(url);
        response.setContentType(Files.probeContentType(path));
        response.setContent(content.getBytes());
    }
}
