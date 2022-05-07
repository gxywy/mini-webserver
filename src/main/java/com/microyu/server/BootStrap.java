package com.microyu.server;

import com.microyu.server.MiniServer;

public class BootStrap {
    public static void main(String[] args) {
        MiniServer server = new MiniServer();
        server.startServer();
        // server.stopServer();
    }
}
