package com.fantasy.framework.websocket.http;

import com.fantasy.framework.mock.MockRequest;
import com.fantasy.framework.util.common.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WebSocketRequest extends MockRequest {

    private final static Log logger = LogFactory.getLog(WebSocketRequest.class);

    private ServletInputStream inputStream;

    public WebSocketRequest(Socket socket) throws IOException {
        this.inputStream = new WebSocketInputStream(socket.getInputStream());
        this.setRemoteAddr(socket.getInetAddress().getHostAddress());
        SocketAddress socketAddress = socket.getRemoteSocketAddress();
        if (socketAddress instanceof InetSocketAddress) {
            this.setRemoteHost(((InetSocketAddress) socketAddress).getHostName());
        }
    }

    public void loadData() throws IOException,TimeoutException {
        BufferedReader in = new BufferedReader(new InputStreamReader(this.inputStream));
        boolean isOver = false;
        long timeout = 1000 * 10,loadstart=System.currentTimeMillis();
        do {
            if (in.ready()) {
                String line = in.readLine();
                if(logger.isDebugEnabled()){
                    logger.debug("line:"+line);
                }
                if ("".equals(line)) {
                    isOver = true;
                    continue;
                }
                if (line.indexOf(':') == -1) {
                    String[] treatys = line.split(" ");
                    this.setMethod(treatys[0]);
                    this.setRequestURI(treatys[1]);
                    this.setProtocol(treatys[2]);
                } else {
                    this.addHeader(StringUtil.trim(line.substring(0, line.indexOf(':'))), StringUtil.trim(line.substring(line.indexOf(':') + 1)));
                }
            } else {// 如果没有新内容,等待200毫秒
                try {
                    long loadtime = System.currentTimeMillis() - loadstart;
                    if (loadtime > timeout) {
                        throw new TimeoutException("加载握手数据超时!");
                    }
                    TimeUnit.NANOSECONDS.sleep(100);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        } while (!isOver);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return inputStream;
    }

    public static class WebSocketInputStream extends ServletInputStream {

        private InputStream inputStream;

        private WebSocketInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public int read() throws IOException {
            return this.inputStream.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return inputStream.read(b, off, len);
        }

        @Override
        public int read(byte[] b) throws IOException {
            return inputStream.read(b);
        }

        @Override
        public int available() throws IOException {
            return inputStream.available();
        }

        @Override
        public void close() throws IOException {
            inputStream.close();
        }

        @Override
        public synchronized void mark(int readlimit) {
            inputStream.mark(readlimit);
        }

        @Override
        public boolean markSupported() {
            return inputStream.markSupported();
        }

        @Override
        public synchronized void reset() throws IOException {
            inputStream.reset();
        }

        @Override
        public long skip(long n) throws IOException {
            return inputStream.skip(n);
        }

    }

}
