package com.microyu.server.http;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceHandler {
    public static void handler(Request request, Response response) {
        try {
            File file = new File("webapp");
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
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
