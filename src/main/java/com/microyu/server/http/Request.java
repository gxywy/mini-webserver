package com.microyu.server.http;

import com.microyu.server.servlet.ServletContext;
import com.microyu.server.utils.HttpRequestMethod;

import java.io.*;
import java.util.*;

public class Request {
    public static final String BLANK = " ";
    public static final String CRLF = "\r\n";

    private String url;
    private HttpRequestMethod method;
    private StringBuilder request;
    private StringBuilder body;
    private Map<String, List<String>> params;
    private Map<String, List<String>> headers;
    private Session session;
    private ArrayList<Cookie> cookies;

    private BufferedReader bufferedReader;

    public Request(InputStream inputStream) {
        request = new StringBuilder();
        body = new StringBuilder();
        headers = new HashMap<>();

        try {
            //使用字符流进行解析，注意不能直接使用InputStream或BufferedInputStream
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            //解析请求第一行：请求方法、URL
            String firstLine = bufferedReader.readLine();
            parseFirstLine(firstLine);
            request.append(firstLine).append(CRLF);

            //解析请求头
            while (bufferedReader.ready()) {
                String headerLine = bufferedReader.readLine();
                if (headerLine.length() == 0) {
                    break;
                }
                parseHeader(headerLine);
                request.append(headerLine).append(CRLF);
            }

            //解析请求体，此处不能再次使用bufferedReader.ready()，需要根据请求头中的长度手动读入body
            StringBuilder bodyLine = new StringBuilder();
            if (headers.containsKey("Content-Length") && !headers.get("Content-Length").get(0).equals("0")) {
                List<String> lengths = this.headers.get("Content-Length");
                if (lengths != null) {
                    int length = Integer.parseInt(lengths.get(0));

                    for (int i = 0; i < length; i++) {
                        bodyLine.append((char)bufferedReader.read());
                    }

                    body.append(bodyLine);
                    request.append(bodyLine);
                    byte[] bytesBodyLine = bodyLine.toString().getBytes();
                    parseParams(new String(bytesBodyLine, 0, Math.min(length,bytesBodyLine.length)));
                }
            }

            //读入完毕后不能关流，会导致socket关闭
        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println(url);
//        System.out.println(method);
//        System.out.println(body);
//        System.out.println(params);
//        System.out.println(headers);
//        System.out.println(cookies);
    }

    private void parseFirstLine(String firstLine) {
        //解析请求方法
        String[] firstLineSlices = firstLine.split(BLANK);
        this.method = HttpRequestMethod.valueOf(firstLineSlices[0]);

        //解析URL
        String rawURL = firstLineSlices[1];
        String[] urlSlices = rawURL.split("\\?");
        this.url = urlSlices[0];

        //解析URL参数
        if (urlSlices.length > 1) {
            parseParams(urlSlices[1]);
        }
    }


    private void parseHeader(String headerLine) {
        if (headerLine.equals("")) {
            return;
        }

        //解析请求头
        int colonIndex = headerLine.indexOf(':');
        String key = headerLine.substring(0, colonIndex);
        String[] values = headerLine.substring(colonIndex + 2).split(",");
        headers.put(key, Arrays.asList(values));

        //解析Cookie
        if (key.equals("Cookie")) {
            String[] rawCookies = values[0].split(";");
            cookies = new ArrayList<>(rawCookies.length);
            for (String rawCookie : rawCookies) {
                String[] kv = rawCookie.split("=");
                cookies.add(new Cookie(kv[0], kv[1]));
            }
            // headers.remove("Cookie");
        } else {
            cookies = new ArrayList<>();
        }
    }

    private void parseBody(String bodyLine) {
        byte[] bytesBodyLine = bodyLine.getBytes();
        if (headers.containsKey("Content-Length") && !headers.get("Content-Length").get(0).equals("0")) {
            List<String> lengths = this.headers.get("Content-Length");
            if (lengths != null) {
                int length = Integer.parseInt(lengths.get(0));
                parseParams(new String(bytesBodyLine, 0, Math.min(length,bytesBodyLine.length)).trim());
            }
        }
    }

    private void parseParams(String params) {
        String[] urlParams = params.split("&");
        if (this.params == null) {
            this.params = new HashMap<>();
        }
        for (String param : urlParams) {
            String[] kv = param.split("=");
            if (kv.length == 2) {
                String[] values = kv[1].split(",");
                this.params.put(kv[0], Arrays.asList(values));
            }
        }
    }

    public void close() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Session getSession() {
        if (session != null) {
            return session;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getKey().equals("JSESSIONID")) {
                Session currentSession = ServletContext.getServletContext().getSession(cookie.getValue());
                if (currentSession != null) {
                    session = currentSession;
                    return session;
                }
            }
        }
        return new Session();
    }



    public String getRequestMessage() {
        return request.toString();
    }

    public HttpRequestMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, List<String>> getParams() {
        return params;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public ArrayList<Cookie> getCookies() {
        return cookies;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
