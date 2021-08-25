package com.microyu.server.http;

import java.util.Map;
import java.util.UUID;

public class Session {
    private String id;
    private Map<String, Object> attributes;
    private boolean isValid;
    private boolean isNew;
    private int interval;

    public Session() {
        id = UUID.randomUUID().toString().replace("-","").toUpperCase(); //随机生成ID
        interval = 30 * 60; //暂时
        isNew = true;
        isValid = true;
    }

    public String getId() {
        return id;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void invalidate() {
        isValid = false;
        attributes.clear();
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    public void setMaxInactiveInterval(int interval) {
        this.interval = interval;
    }
}
