package com.microyu.server.http;

import com.microyu.server.http.dispatcher.ApplicationRequestDispatcher;
import com.microyu.server.http.dispatcher.RequestDispatcher;
import com.microyu.server.servlet.ServletContext;
import com.microyu.server.utils.HttpRequestMethod;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;

public class Request {
    public static final String BLANK = " ";
    public static final String CRLF = "\r\n";

    private String url;
    private HttpRequestMethod method;
    private String request;
    private Map<String, List<String>> params;
    private Map<String, List<String>> headers;
    private Session session;
    private ArrayList<Cookie> cookies;
    private ServletContext servletContext;

    private BufferedReader bufferedReader;

    public Request(InputStream inputStream) throws Exception {
        headers = new HashMap<>();
        servletContext = ServletContext.getServletContext();

//        bin = new BufferedInputStream(inputStream);
//        byte[] buf = new byte[bin.available()];
//        int len = bin.read(buf);
//        if (len <= 0) {
//            throw new IOException();
//        }

        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String content = "";
        String line = null;
        while((line = bufferedReader.readLine()).length() != 0) {
            content += line;
        }

        request = URLDecoder.decode(content, Charset.forName("UTF-8"));
        String[] lines = request.split(CRLF);
        parseFirstLine(lines[0]);
        for (int i = 1; i <lines.length; i++) {
            if (lines[i].equals("")) {
                break;
            }
            parseHeader(lines[i]);
        }

        if (headers.containsKey("Content-Length") && !headers.get("Content-Length").get(0).equals("0")) {
            parseBody(lines[lines.length - 1]);
        }

        //读入完毕后不能关流，会导致socket关闭
//        System.out.println(url);
//        System.out.println(method);
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
        parseParams(bodyLine);
        if (this.params == null) {
            this.params = new HashMap<>();
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

    public RequestDispatcher getRequestDispatcher(String url) {
        return new ApplicationRequestDispatcher(url);
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

    public ServletContext getServletContext() {
        return this.servletContext;
    }
}
