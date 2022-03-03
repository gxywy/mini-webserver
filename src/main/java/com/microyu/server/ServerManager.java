package com.microyu.server;

public class ServerManager {
    public static void main(String[] args) {
        new Thread(new Dispatcher()).start();
    }
}
