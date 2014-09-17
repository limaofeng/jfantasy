package com.fantasy.framework.websocket;

import com.fantasy.framework.websocket.exception.WebSocketException;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TestWebSocketListener implements WebSocketListener {

	private WebSocketConnection connection;

    private final static ConcurrentLinkedQueue<WebSocketConnection> onlineClients = new ConcurrentLinkedQueue<WebSocketConnection>();

    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println("连接关闭:" + reason);
        onlineClients.remove(this.connection);
    }

    public void onWebSocketConnect(WebSocketConnection connection) {
        System.out.println("建立新连接:" + connection);
        onlineClients.add(this.connection = connection);
    }

    public void onWebSocketException(WebSocketException error) {

    }

	public void onWebSocketText(String message) {
		System.out.println("TestWebSocketListener:" + message);
        for (WebSocketConnection connection : onlineClients) {
            if (this.connection != connection) {
                connection.sendMessage(message);
            }
        }
//        this.connection.disconnect();
	}

    public void onWebSocketBinary(byte[] payload, int offset, int len) {
    }

}
