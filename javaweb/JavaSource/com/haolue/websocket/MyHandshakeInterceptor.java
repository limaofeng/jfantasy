package com.haolue.websocket;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.List;
import java.util.Map;

public class MyHandshakeInterceptor extends HttpSessionHandshakeInterceptor{

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("before Handshake");
        HttpHeaders headers = request.getHeaders();
        for(Map.Entry<String, List<String>> entry : headers.entrySet()){
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

        for(Map.Entry<String, Object> entry:attributes.entrySet()){
            System.out.println(entry.getKey()+"="+entry.getValue());
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        System.out.println(" ================= after Handshake ================= ");
        super.afterHandshake(request, response, wsHandler, ex);
    }

}
