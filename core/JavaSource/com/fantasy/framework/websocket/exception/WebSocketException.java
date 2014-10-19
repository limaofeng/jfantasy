package com.fantasy.framework.websocket.exception;

import java.io.IOException;

public class WebSocketException extends IOException {

    public WebSocketException() {
    }

    public WebSocketException(String s) {
        super(s);
    }

    public WebSocketException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public WebSocketException(Throwable throwable) {
        super(throwable);
    }

}
