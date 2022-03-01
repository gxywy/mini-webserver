package com.microyu.server.utils;

public enum HttpVersion {
    HTTP10("HTTP/1.0"),
    HTTP11("HTTP/1.1");

    private final String version;

    HttpVersion(String version) {
        this.version = version;
    }

    public String toString() {
        return version;
    }
}
