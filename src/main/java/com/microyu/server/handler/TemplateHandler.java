package com.microyu.server.handler;

import com.microyu.server.http.request.HttpRequest;
import com.microyu.server.http.response.HttpResponse;
import com.microyu.server.template.TemplateResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TemplateHandler {
    public static void handle(String url, HttpRequest request, HttpResponse response) throws IOException {
        String content = TemplateResolver.resolve(new String(FileHandler.getByteFromFile(url)));
        Path path = FileHandler.getPathFromFile(url);
        response.setContentType(Files.probeContentType(path));
        response.addContent(content);
    }
}
