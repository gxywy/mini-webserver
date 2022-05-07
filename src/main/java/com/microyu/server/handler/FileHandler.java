package com.microyu.server.handler;

import com.microyu.server.MiniServer;
import com.microyu.server.http.request.HttpRequest;
import com.microyu.server.http.response.HttpResponse;
import com.microyu.server.util.HttpContentTypeUtil;
import com.microyu.server.util.HttpStatusUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileHandler {

    public static Path getPathFromFile(String fileName) {
        File file = new File(MiniServer.class.getResource("/webapp").getFile());
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

    public static void handle(HttpRequest request, HttpResponse response) {
        try {
            Path path = getPathFromFile(request.getRequestURI());
            byte[] byteFromFile = getByteFromFile(request.getRequestURI());
            response.addHeader("Content-Length", String.valueOf(byteFromFile.length));
            response.addContent(byteFromFile);
            response.setContentType(Files.probeContentType(path));
            response.setResponseCode(200);
        } catch (IOException e) {
            response.setResponseCode(404);
            e.printStackTrace();
        }
    }
}
