package com.microyu.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.microyu.server.http.Cookie;
import com.microyu.server.http.request.HttpRequest;
import com.microyu.server.util.ServerConfigUtil;
import com.microyu.server.util.HttpContentTypeUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestResolver {
    private String encoding = ServerConfigUtil.getDefaultCharset();
    private String lineSeparator = System.getProperty("line.separator"); // 获取当前平台分隔符
    private String requestMessage = null;
    private HttpRequest request;

    public void resolve(SelectionKey key, HttpRequest request) throws IOException {
        this.request = request;
        // 从channel中读取请求报文
        readRequestMessage(key);
        // 解析请求报文
        resolveRequestMessage(requestMessage);
    }

    private void readRequestMessage(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        // 申请缓冲区
        ByteBuffer buf = ByteBuffer.allocate(8 * 1024);

        while (true) {
            int len = channel.read(buf);
            if (len > 0) {
                break;
            } if (len == 0) {
                continue;
            } else if (len == -1) {
                break;
            }
        }
        buf.flip(); // 切换为读模式
        requestMessage = new String(buf.array(), 0, buf.limit());
        buf.clear(); // 切换为写模式
    }

    private void resolveRequestMessage(String requestMessage) {
        // 解码请求消息
        String message = null;
        try {
            message = URLDecoder.decode(requestMessage, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 获取请求第一行
        String firstLine = message.substring(0, message.indexOf(lineSeparator));
        resolveFistLine(firstLine);

        // 截取请求头和请求体
        message = message.substring(message.indexOf(lineSeparator) + 2);

        // 解析请求头
        String[] lines = message.split(lineSeparator);
        int index = 0;
        for (; index < lines.length; index++) {
            String headerLine = lines[index];
            // 遍历到请求体之前的空白行就停止
            if (headerLine == null || headerLine.isEmpty()) {
                break;
            }
            // 解析请求头中的行
            resolveHeader(headerLine);
        }

        // 获取请求体
        index++;
        StringBuilder bodyBuilder = new StringBuilder();
        for (; index < lines.length; index++) {
            bodyBuilder.append(lines[index]);
        }
        String body = bodyBuilder.toString();
        request.setBody(body);

        //解析请求体
        resolveBody(body);
    }

    private void resolveFistLine(String firstLine) {
        // 获取请求方法和Http协议版本
        String[] requestLines = firstLine.split("\\s+");
        request.setMethod(requestLines[0]);
        request.setProtocol(requestLines[2]);

        // 解析请求uri
        String[] uri = requestLines[1].split("\\?");
        request.setRequestURI(uri[0]);

        // 解析content-type
        resolveContentType(request.getRequestURI());

        // 解析uri后面的请求参数
        if (uri.length > 1) {
            resolveRequestParams(uri[1]);
        }
    }

    private void resolveHeader(String headerLine) {
        // 获取第一个“:”的下标
        int index = headerLine.indexOf(":");
        if (index == -1) {
            return;
        }

        String headerKey = headerLine.substring(0, index).trim();
        String headerValue = headerLine.substring(index + 1).trim();
        request.addHeader(headerKey, headerValue);

        if (headerKey.equals("Cookie")) {
            // 解析Cookie
            resolveCookies(headerValue);
        } else if (headerKey.equals("")) {
            // 解析host和port
            resolveHostAndPort(headerValue);
        }
    }

    private void resolveHostAndPort(String hostLine) {
        if (hostLine == null || hostLine.isEmpty()) {
            return;
        }

        // 解析主机名
        String host = hostLine.split(":")[0];
        request.setHost(host);

        // 解析端口号
        if (hostLine.contains(":")) {
            int port = Integer.parseInt(hostLine.split(":")[1]);
            request.setPort(port);
        }
    }

    private void resolveContentType(String requestURI) {
        String contentType = null;
        if (!requestURI.contains(".")) {
            contentType = HttpContentTypeUtil.HTML;
        } else {
            contentType = HttpContentTypeUtil.getContentType(requestURI);
        }
        request.setContentType(contentType);
    }

    private void resolveCookies(String cookieLine) {
        if (cookieLine == null || cookieLine.isEmpty()) {
            return;
        }

        String[] cookies = cookieLine.split(";\\s*");
        for (int i = 0; i < cookies.length; i++) {
            String cookie = cookies[i];
            if (!(cookie == null || cookie.isEmpty())) {
                String[] cookieKV = cookie.split("=");
                if (cookieKV.length == 2) {
                    request.addCookie(new Cookie(cookieKV[0], cookieKV[1]));
                }
            }
        }
    }

    private void resolveBody(String body) {
        if (!(body == null || body.isEmpty())) {
            return;
        }

        if (body.contains("{")) {
            // 解析JSON格式的请求体
            Map<String, String> map = JSON.parseObject(body, new TypeReference<Map<String, String>>(){});
            request.addAllParams(map);
        } else if (body.contains("&")) {
            // 解析&格式的请求体
            resolveRequestParams(body);
        }
    }

    private void resolveRequestParams(String parameterLine) {
        Map<String, String> map = new HashMap<String, String>();
        if (!(parameterLine == null || parameterLine.isEmpty())) {
            return;
        }

        String[] parameters = parameterLine.split("&+");
        for (int i = 0; i < parameters.length; i++) {
            String parameter = parameters[i];
            String[] parameterKV = parameter.split("\\s*=\\s*");
            if (parameterKV.length == 2) {
                request.addParam(parameterKV[0], parameterKV[1]);
            } else if (parameterKV.length == 1) {
                request.addParam(parameterKV[0], "");
            }
        }
    }

    public void setCharEncoding(String encoding) {
        try {
            Charset.forName(encoding);
            this.encoding = encoding;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
