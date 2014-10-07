package com.fantasy.framework.websocket;

import com.fantasy.framework.util.LinkedBlockingQueue;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.framework.websocket.data.Frame;
import com.fantasy.framework.websocket.http.WebSocketRequest;
import com.fantasy.framework.websocket.http.WebSocketResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class WebSocketClient {

    private static Log logger = LogFactory.getLog(WebSocketClient.class);

    // 握手信息-请求信息;
    private HttpServletRequest request;

    private HttpServletResponse response;

    private WebSocketListener listener;

    private SimpleWebSocketConnection connection;

    private WebSocketObservable observable = new WebSocketObservable(this);

    /**
     * 线程池
     */
    private Executor executor;
    /**
     * 定时任务
     */
    private ScheduledExecutorService scheduler;

    public WebSocketClient(Socket socket) throws TimeoutException {
        try {
            WebSocketRequest request = new WebSocketRequest(socket);
            this.response = new WebSocketResponse(socket.getOutputStream(), request, socket);
            // 构建connection
            this.connection = new SimpleWebSocketConnection();
            // WebUtil.getRealIpAddress(request);
            // load 握手数据
            request.loadData();
            // 握手
            this.connection.handshake(request, response);
            // 发送握手信息
            response.sendError(101, "Switching Protocols");
            response.flushBuffer();

            this.request = request;

            // 添加监听器
            this.listener = new TestWebSocketListener();
            this.listener.onWebSocketConnect(connection);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private PingCache pingCache = new PingCache();


    public void service() throws IOException {

        // 定时发送心跳包
        this.addObserver(new WebSocketEventListener() {

            @Override
            public void onPing(long time) {
                logger.debug("ping:" + time + "s");
            }

        });
        final ScheduledFuture future = scheduler.scheduleWithFixedDelay(new Runnable() {

            private long timeout = 0;

            @Override
            public void run() {
                if (isOpen()) {
                    try {
                        ping();
                        timeout = 0;
                    } catch (TimeoutException e) {
                        timeout++;
                        if (timeout == 3) {
                            close();
                        }
                    }
                }
            }
        }, 10, 5, TimeUnit.SECONDS);

        //关闭连接
        this.addObserver(new WebSocketEventListener() {

            @Override
            public void onClose() {
                future.cancel(true);
                connection.closed();
            }

        });

        // 监听推送消息队列
        InputStream in = request.getInputStream();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (isOpen()) {
                    Frame data;
                    try {
                        data = connection.take();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage(), e);
                        return;
                    }
                    try {
                        if (data.getHandler().getOpcode() == Frame.PING) {
                            pingCache.ping(System.currentTimeMillis());
                        } else if (data.getHandler().getOpcode() == Frame.CLOSE) {
                            listener.onWebSocketClose(Frame.CLOSE, "server close!");
                            fireEvent("close");
                        }
                        OutputStream out = response.getOutputStream();
                        out.write(Frame.toBytes(data));
                        out.flush();
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        });

        // 消息队列
        final LinkedBlockingQueue<Frame> queue = new LinkedBlockingQueue<Frame>();
        // 为websocket创建上下文访问的
        executor.execute(new Runnable() {// 监听器线程
            public void run() {
                while (connection.isOpen()) {
                    Frame data;
                    try {
                        data = queue.take();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage(), e);
                        return;
                    }
                    byte opcode = data.getHandler().getOpcode();
                    if (Frame.TXT == opcode) {
                        listener.onWebSocketText(data.getMessage());
                    } else if (Frame.BINARY == opcode) {
                        listener.onWebSocketBinary(data.getBody(), 0, (int) data.getHandler().getDataLength());
                    } else if (Frame.CLOSE == opcode) {
                        listener.onWebSocketClose(opcode, StringUtil.defaultValue(data.getMessage(), "client close!"));
                        fireEvent("close");
                    } else if (Frame.PONG == opcode) {
                        logger.debug("PONG:" + opcode + "\t" + data.getMessage());
                        pingCache.pong(System.currentTimeMillis());
                    } else {
                        logger.debug("opcode:" + opcode);
                    }
                }
            }
        });
        Frame data = new Frame();
        int intByte = in.read();
        boolean closed = false;
        while (intByte > -1) {
            data.put((byte) intByte);
            if (data.finish()) {
                try {
                    queue.put(data);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
                closed = Frame.CLOSE == data.getHandler().getOpcode();
                if (!closed) {
                    data = new Frame();
                }
            }
            if (closed) {
                intByte = -1;
            } else {
                intByte = in.read();
            }
        }
    }

    public long ping() throws TimeoutException {
        this.connection.sendMessage(Frame.PING, "");
        long _ping = pingCache.poll(3, TimeUnit.SECONDS);
        if (_ping == -1) {
            throw new TimeoutException(" Request timeout ");
        }
        fireEvent("ping", _ping);
        return _ping;
    }

    public void close() {
        if (this.connection.isOpen()) {
            connection.disconnect();
        }
    }

    public void fireEvent(String eventName, Object... args) {
        this.observable.fireEvent(eventName, args);
    }

    public void addObserver(WebSocketEventListener listener) {
        this.observable.addEvent(listener);
    }

    public static class WebSocketObservable extends Observable {

        private WebSocketClient client;

        private WebSocketObservable(WebSocketClient client) {
            this.client = client;
        }

        public void fireEvent(String eventName, Object args) {
            super.setChanged();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("eventName", eventName);
            params.put("args", args);
            this.notifyObservers(params);
        }

        public void addEvent(WebSocketEventListener listener) {
            listener.setSource(client);
            this.addObserver(listener);
        }

    }

    public static abstract class WebSocketEventListener implements Observer, EventListener {

        private WebSocketClient source;

        public void onPing(long time) {
        }

        public void onClose() {
        }

        public void update(Observable observable, Object o) {
            String eventName = (String) OgnlUtil.getInstance().getValue("eventName", o);
            Object[] args = (Object[]) OgnlUtil.getInstance().getValue("args", o);
            if ("close".equalsIgnoreCase(eventName)) {
                this.onClose();
            } else if ("ping".equalsIgnoreCase(eventName)) {
                this.onPing((Long) args[0]);
            }
        }

        public void setSource(WebSocketClient source) {
            this.source = source;
        }

        public WebSocketClient getSource() {
            return source;
        }
    }

    private static class PingCache {

        private LinkedBlockingQueue<Long> pings = new LinkedBlockingQueue<Long>();
        private long pingTime;

        public void ping(long time) {
            pingTime = time;
        }

        public void pong(long time) {
            try {
                pings.put(time - pingTime);
            } catch (InterruptedException ignored) {
            }
        }

        public long poll(long timeout, TimeUnit unit) {
            try {
                Long _ping = pings.poll(timeout, unit);
                return _ping == null ? -1 : _ping;
            } catch (InterruptedException ignored) {
                return -1;
            }
        }

    }

    public boolean isOpen() {
        return connection.isOpen();
    }

    public WebSocketConnection getConnection() {
        return connection;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public void setScheduler(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

}
