package com.microyu.server.handler;

import com.microyu.server.http.request.HttpRequest;
import com.microyu.server.http.response.HttpResponse;
import com.microyu.server.servlet.ServletContext;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class RequestHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(RequestHandler.class);
    private SelectionKey key;
    private SocketChannel channel;
    private HttpRequest request;
    private HttpResponse response;

    public RequestHandler(SelectionKey key) {
        this.key = key;
        this.channel = (SocketChannel) key.channel();
    }

    @Override
    public void run() {
        // 解析Http请求
        request = new HttpRequest();
        HttpRequestResolver httpRequestResolver = new HttpRequestResolver();
        try {
            httpRequestResolver.resolve(key, request);
        } catch (IOException e) {
            key.cancel(); // 客户端意外断开时，取消key
            e.printStackTrace();
        }
        // 创建Http响应报文，后续填充内容
        response = new HttpResponse(channel);

        // 处理静态资源
        if ("GET".equals(request.getMethod()) && (request.getRequestURI().contains(".") || request.getRequestURI().equals("/"))) {
            if (request.getRequestURI().equals("/")) {
                request.setRequestURI("/index.html");
                FileHandler.handle(request, response);
            } else {
                FileHandler.handle(request, response);
            }
        } else {
            //处理动态资源，交由某个Servlet
            ServletHandler.handle(request, response, ServletContext.getServletContext().dispatch(request.getRequestURI()));
        }

        // 返回Http响应报文
        response.response();

        // 关闭channel
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info(String.format("Handled request: %s", request.getRequestURI()));
    }
}
