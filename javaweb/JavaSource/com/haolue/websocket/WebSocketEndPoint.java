package com.haolue.websocket;

import com.fantasy.framework.util.ognl.OgnlUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;


public class WebSocketEndPoint extends TextWebSocketHandler {

    public WebSocketEndPoint() {
        System.out.println("WebSocketEndPoint");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        Principal principal = (Principal)OgnlUtil.getInstance().getValue("principal",session);
        UserDetails userDetails = principal instanceof Authentication ? (UserDetails) ((Authentication) principal).getPrincipal() : null;
        TextMessage returnMessage = new TextMessage(userDetails.getUsername() + " : " + message.getPayload() + " received at server");
        session.sendMessage(returnMessage);
    }

}
