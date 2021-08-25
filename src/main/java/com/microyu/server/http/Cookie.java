package com.microyu.server.http;

public class Cookie {
    private String key;
    private String value;
    private String path;
    private int age = -1;

    public Cookie(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public int getMaxAge() {
        return age;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setMaxAge(int age) {
        this.age = age;
    }

    public void setPath(String path) { this.path = path; }

    // Cookie使用工具
    public static Cookie findCookie(String key, Cookie[] cookies) {
        if (key == null || cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (key.equals(cookie.getKey())) {
                return cookie;
            }
        }
        return null;
    }
}
