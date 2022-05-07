package com.microyu.server;

import com.microyu.server.handler.RequestHandler;
import com.microyu.server.util.ServerConfigUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MiniServer extends Thread {
    private static int port = ServerConfigUtil.getPort();
    private ServerThreadExecutor serverThreadExecutor;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private volatile boolean isRunning = true;
    private static final Logger logger = Logger.getLogger(MiniServer.class);

    public MiniServer() {
        ServerConfigUtil.config();
    }

    public void startServer() {
        // 创建线程池
        serverThreadExecutor = new ServerThreadExecutor();

        // 创建ServerSocketChannel和对于的Selector
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 切换服务器状态
        isRunning = true;

        // 启动线程
        this.start();

        logger.info(String.format("MiniServer started, listen at %s", port));
    }

    public void stopServer() {
        try {
            isRunning = false;
            serverSocketChannel.close();
            logger.info("MiniServer stoped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            listener();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.stopServer();
        }
    }

    private void listener() throws IOException {
        while (isRunning) {
            try {
                int result = selector.select();
                // 无事件情况，继续执行
                if (result <= 0) {
                    continue;
                }

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isValid() && key.isAcceptable()) {
                        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                        // 获得客户端channel
                        SocketChannel clientChannel = serverChannel.accept();
                        // 设置为非阻塞
                        clientChannel.configureBlocking(false);
                        // 注册客户端channel到Selector
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        logger.info(String.format("Accepted connetion from %s:%s",
                                clientChannel.socket().getInetAddress().getHostAddress(),
                                clientChannel.socket().getPort()));
                    } else if (key.isValid() && key.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        serverThreadExecutor.execute(new RequestHandler(key));
                        key.cancel();
                    }
                    // 处理完事件后需要remove
                    iterator.remove();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
