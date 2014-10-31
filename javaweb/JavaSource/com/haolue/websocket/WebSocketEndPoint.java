package com.haolue.websocket;

import com.fantasy.framework.util.ognl.OgnlUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


public class WebSocketEndPoint extends TextWebSocketHandler {

    public WebSocketEndPoint() {
        System.out.println("WebSocketEndPoint");
    }

    private List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        Principal principal = (Principal)OgnlUtil.getInstance().getValue("principal",session);
        UserDetails userDetails = principal instanceof Authentication ? (UserDetails) ((Authentication) principal).getPrincipal() : null;
        TextMessage returnMessage = new TextMessage(userDetails.getUsername() + " : " + message.getPayload() + " received at server");
        for(WebSocketSession _s : sessions) {
            if(_s != session) {
                _s.sendMessage(returnMessage);
            }
        }
    }

}
