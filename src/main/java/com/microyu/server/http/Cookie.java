package com.microyu.server.http;

public class Cookie {
    private String key;
    private String value;
    private String path;
    private String domain;

    private int age = -1;

    public Cookie(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Cookie(String key, String value, int age) {
        this.key = key;
        this.value = value;
        this.age = age;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPath() { return path; }

    public void setPath(String path) { this.path = path; }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    // Cookie实用工具
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
