package com.fantasy.framework.websocket.exception;

public class ConnectionClosedException extends WebSocketException {

	private static final long serialVersionUID = 1858520944427197014L;

	public ConnectionClosedException() {
		super("连接已经关闭");
	}

}
