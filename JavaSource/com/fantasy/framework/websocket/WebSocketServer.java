package com.fantasy.framework.websocket;

import com.fantasy.framework.util.LinkedBlockingQueue;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class WebSocketServer {

    private static final Logger logger = Logger.getLogger(WebSocketServer.class);

    private ServerSocket server;
    /**
     * 是否关闭
     */
    private boolean stopped = true;
    /**
     * 服务是否启动
     */
    private boolean initialized = false;
    /**
     * 线程池
     */
    private ExecutorService executor;
    /**
     * 定时任务
     */
    private ScheduledExecutorService scheduler;

    public WebSocketServer(int port) throws IOException {
        this(port, 10);
    }

    public WebSocketServer(int port, int backlogSize) throws IOException {
        this.server = new ServerSocket(port, backlogSize);
        this.executor = new ThreadPoolExecutor(backlogSize, backlogSize + 50, 5l, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(backlogSize));
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * 开启服务
     *
     * @throws IOException
     */
    public void start() throws IOException {
        this.stopped = false;
        startServer();
    }

    /**
     * 关闭服务
     *
     * @throws IOException
     */
    public void stop() throws IOException {
        this.stopped = true;
        this.executor.shutdown();
        this.scheduler.shutdown();
        this.server.close();
    }

    /**
     * 开始服务
     */
    private synchronized void startServer() {
        if (!this.initialized) {
            this.initialized = true;
            executor.execute(new Runnable() {
                public void run() {
                    while (!WebSocketServer.this.stopped)
                        try {
                            Socket cli = WebSocketServer.this.server.accept();
                            WebSocketServer.this.executor.execute(new WebSocketServer.ClientRunner(cli, WebSocketServer.this));
                        } catch (IOException e) {
                            logger.error(e.getMessage(), e);
                        }
                }
            });
        }
    }

    /**
     * 客户端守护线程
     *
     * @author 李茂峰
     * @version 1.0
     * @since 2013-7-11 上午11:37:55
     */
    protected final static class ClientRunner implements Runnable {
        private final Socket cli;
        private final WebSocketServer socketServer;

        private ClientRunner(Socket cli, WebSocketServer socketServer) {
            this.cli = cli;
            this.socketServer = socketServer;
        }

        public void run() {
            try {
                WebSocketClient client = new WebSocketClient(cli);
                client.setExecutor(socketServer.executor);
                client.setScheduler(socketServer.scheduler);
                client.service();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } catch (TimeoutException e) {
                logger.error(e.getMessage(), e);
            } finally {
                try {
                    cli.close();
                } catch (IOException io) {
                    logger.error(io.getMessage(), io);
                }
            }
        }
    }

}
