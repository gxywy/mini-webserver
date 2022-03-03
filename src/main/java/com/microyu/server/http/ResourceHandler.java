package com.microyu.server.http;

import com.microyu.server.Dispatcher;
import com.microyu.server.utils.HttpStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceHandler {

    public static Path getPathFromFile(String fileName) {
        File file = new File(Dispatcher.class.getResource("/webapp").getFile());
        Path path = Paths.get(file.getAbsolutePath(), fileName);
        return path;
    }

    public static byte[] getByteFromFile(String fileName) throws IOException {
        Path path = getPathFromFile(fileName);

        FileInputStream fis = new FileInputStream(path.toString());
        BufferedInputStream bis = new BufferedInputStream(fis);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int len;
        byte[] buffer = new byte[1024];
        while ((len = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }

        //bos.close();
        bis.close();
        fis.close();

        return bos.toByteArray();
    }

    public static void handler(Request request, Response response) {
        try {
            Path path = getPathFromFile(request.getUrl());
            byte[] byteFromFile = getByteFromFile(request.getUrl());
            response.appendContent(byteFromFile, byteFromFile.length);

            if (!path.toString().endsWith(".html")) {
                response.setContentType(Files.probeContentType(path));
            }

            response.pushResponse(HttpStatus.OK);
        } catch (IOException e) {
            response.pushResponse(HttpStatus.NOT_FOUND);
            e.printStackTrace();
        }
    }
}
