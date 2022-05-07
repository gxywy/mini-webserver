package com.microyu.server.util;

import java.util.HashMap;
import java.util.Map;

public class HttpVersionUtil {
    private static Map<String, String> map = new HashMap<>();

    static {
        map.put("HTTP/1.0", "HTTP/1.0");
        map.put("HTTP/1.1", "HTTP/1.1");
    }

    public static String getHttpVersion(String version) {
        return map.get(version.toUpperCase());
    }
}
