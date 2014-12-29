package com.fantasy.wx.websocket;

import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.account.init.WeixinConfigInit;
import com.fantasy.wx.bean.Message;
import com.fantasy.wx.bean.UserInfo;
import com.fantasy.wx.service.UserInfoService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzzhong on 2014/10/31.
 */
@Component
public class WeixinMessageWebSocket extends TextWebSocketHandler {

    @Resource
    private WeixinConfigInit config;
    @Resource
    private UserInfoService userInfoService;

    public WeixinMessageWebSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        if (config == null) return;
                        Message message = config.getMessage();
                        userInfoService.setUnReadSize(message.getUserInfo());
                        if (message != null) {
                            for (WebSocketSession ws : sessions) {
                                if (!ws.isOpen()) {
                                    sessions.remove(ws);
                                    continue;
                                }
                                try {
                                    String messageStr = JSON.serialize(message);
                                    System.out.println("messageStr" + messageStr);
                                    ws.sendMessage(new TextMessage(messageStr));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    private List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);

        userInfoService.refreshMessage(new UserInfo(message.getPayload()));
        /*Principal principal = (Principal) OgnlUtil.getInstance().getValue("principal",session);
        UserDetails userDetails = principal instanceof Authentication ? (UserDetails) ((Authentication) principal).getPrincipal() : null;
        TextMessage returnMessage = new TextMessage(userDetails.getUsername() + " : " + message.getPayload() + " received at server");
        for(WebSocketSession _s : sessions) {
            if(_s != session) {
                _s.sendMessage(returnMessage);
            }
        }*/
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }
}
