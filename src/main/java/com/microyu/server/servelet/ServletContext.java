package com.microyu.server.servelet;

import com.microyu.server.http.Session;

public class ServletContext {
    private static Session session = null;

    private ServletContext() {
    }

    public static Session getSession(String sessionID) {
        if (session == null) {
            session = new Session();
        }
        return session;
    }
}
