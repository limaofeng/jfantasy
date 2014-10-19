package com.fantasy.framework.websocket;

import com.fantasy.framework.io.Buffer;
import com.fantasy.framework.websocket.exception.WebSocketException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WebSocketConnection extends WebSocketOutbound {

	public abstract void fillBuffersFrom(Buffer paramBuffer);

	public abstract void handshake(HttpServletRequest request, HttpServletResponse response) throws WebSocketException;

}
