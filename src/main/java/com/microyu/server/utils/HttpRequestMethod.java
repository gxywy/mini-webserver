package com.microyu.server.utils;

public enum HttpRequestMethod {
    OPTIONS("OPTIONS"),
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    TRACE("TRACE"),
    CONNECT("CONNECT");

    private final String method;

    HttpRequestMethod(String method) {
        this.method = method;
    }

    public String toString() {
        return method;
    }
}