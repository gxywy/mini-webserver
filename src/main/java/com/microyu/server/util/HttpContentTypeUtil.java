package com.microyu.server.util;

import java.util.HashMap;
import java.util.Map;

public class HttpContentTypeUtil {
    private static Map<String, String> contentTypes = new HashMap<>();

    public static final String HTML = "html";
    public static final String CSS = "css";
    public static final String JS = "js";
    public static final String JPG = "jpg";
    public static final String JPEG = "jpeg";
    public static final String PNG = "png";
    public static final String GIF = "gif";
    public static final String ICO = "ico";
    public static final String TXT = "txt";

    private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

    static {
        contentTypes.put("html", "text/html;charset=" + ServerConfigUtil.getDefaultCharset());
        contentTypes.put("css", "text/css");
        contentTypes.put("js", "application/javascript");
        contentTypes.put("jpg", "image/jpeg");
        contentTypes.put("jpeg", "image/jpeg");
        contentTypes.put("png", "image/png");
        contentTypes.put("gif", "image/gif");
        contentTypes.put("ico", "image/x-icon");
        contentTypes.put("txt", "text/plain");
    }

    public static String getContentType(String uri) {
        // 如果资源uri为空，直接返回text/html
        if (uri == null || uri.isEmpty()) {
            return contentTypes.get(HTML);
        }
        // 获取资源uri后缀
        String suffix = uri.substring(uri.lastIndexOf(".") + 1);
        // 根据后缀从contentTypes获取
        String contentType = contentTypes.get(suffix);
        // 如果没有获取到，返回application/octet-stream
        if (contentType == null) {
            return HTML;//APPLICATION_OCTET_STREAM;
        }
        return contentType;
    }
}
