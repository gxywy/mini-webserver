package com.microyu.server;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerThreadExecutor extends ThreadPoolExecutor {
    public static final int cpuCores = Runtime.getRuntime().availableProcessors();

    public ServerThreadExecutor() {
        super(cpuCores, cpuCores * 2, 2, TimeUnit.MINUTES, new LinkedBlockingDeque<>(), new ServerThreadFactory());
    }

    public static class ServerThreadFactory implements ThreadFactory {
        private AtomicInteger number = new AtomicInteger(1);
        private ServerThreadFactory(){
        }
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("MiniServer.ServerThread-" + number.getAndIncrement());
            thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        }
    }

}
