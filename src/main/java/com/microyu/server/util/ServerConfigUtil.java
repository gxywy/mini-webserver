package com.microyu.server.util;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class ServerConfigUtil {
    private static String defaultCharset = "UTF-8";
    private static int port = 9090;

    public static String getDefaultCharset() {
        return defaultCharset;
    }

    public static void setDefaultCharset(String defaultCharset) {
        ServerConfigUtil.defaultCharset = defaultCharset;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        ServerConfigUtil.port = port;
    }

    // 读取配置文件（暂时不读取）
    public static void config() {
        Logger root = Logger.getRootLogger();
        root.addAppender(new ConsoleAppender(new PatternLayout("[%t] %p %c %x - %m%n")));
    }
}
