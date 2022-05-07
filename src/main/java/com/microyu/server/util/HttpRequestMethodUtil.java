package com.microyu.server.util;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestMethodUtil {
    private static Map<String, String> map = new HashMap<>();

    static {
        map.put("OPTIONS", "OPTIONS");
        map.put("GET", "GET");
        map.put("HEAD", "OK");
        map.put("POST", "POST");
        map.put("PUT", "PUT");
        map.put("DELETE", "DELETE");
        map.put("TRACE", "No Content");
        map.put("CONNECT", "CONNECT");
    }

    public static String getRequestMethod(String method) {
        return map.get(method.toUpperCase());
    }
}