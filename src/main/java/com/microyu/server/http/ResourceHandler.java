package com.microyu.server.http;

import com.microyu.server.Dispacher;
import com.microyu.server.utils.HttpStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceHandler {
    public static void handler(Request request, Response response) {
        try {
            File file = new File(Dispacher.class.getResource("/webapp").getFile());
            Path path = Paths.get(file.getAbsolutePath(), request.getUrl());

            FileInputStream fis = new FileInputStream(path.toString());
            BufferedInputStream bis = new BufferedInputStream(fis);

            int len;
            byte[] buffer = new byte[1024];
            while ((len = bis.read(buffer)) != -1) {
                response.appendContent(buffer, len);
            }

            if (!path.getFileName().toString().endsWith(".html")) {
                response.setContentType(Files.probeContentType(path));
            }

            response.pushResponse(HttpStatus.OK);
        } catch (IOException e) {
            response.pushResponse(HttpStatus.NOT_FOUND);
            e.printStackTrace();
        }
    }
}
