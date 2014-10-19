package com.fantasy.framework.websocket.http;

import com.fantasy.framework.mock.MockResponse;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class WebSocketResponse extends MockResponse {
    private ServletOutputStream outputStream;
    private PrintWriter printWriter;
    private HttpServletRequest request;
    private Socket socket;

    public WebSocketResponse(OutputStream outputStream, HttpServletRequest request, Socket socket) {
        this.outputStream = new WebSocketOutputStream(outputStream);
        this.request = request;
        this.socket = socket;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return printWriter == null ? printWriter = new PrintWriter(this.outputStream) : printWriter;
    }

    public void flushBuffer() throws IOException {
        if (this.printWriter == null)
            this.outputStream.flush();
        else {
            this.printWriter.flush();
            this.printWriter = null;
            this.reset();
        }
    }

    public void resetBuffer() {
    }

    @Override
    public void reset() {
        try {
            this.outputStream = new WebSocketOutputStream(this.socket.getOutputStream());
        } catch (IOException ex) {
            //TODO xxxx
        }
    }

    public void sendError(int status, String error) throws IOException {
        this.setStatus(status);
        PrintWriter writer = getWriter();
        writer.println(request.getProtocol() + " " + getStatus() + " " + error);
        for (Map.Entry<String, String> header : this.responseHeaders.entrySet()) {
            writer.println(header.getKey() + ": " + header.getValue());
        }
        writer.println();
    }

    public void sendError(int status) throws IOException {
        this.sendError(status, "");
    }

    public Socket getSocket() {
        return socket;
    }

    public static class WebSocketOutputStream extends ServletOutputStream {

        private OutputStream outputStream;

        private WebSocketOutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
        }

        @Override
        public void close() throws IOException {
            outputStream.close();
        }

        @Override
        public void flush() throws IOException {
            outputStream.flush();
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            outputStream.write(b, off, len);
        }

        @Override
        public void write(byte[] b) throws IOException {
            outputStream.write(b);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
        }
    }
}
