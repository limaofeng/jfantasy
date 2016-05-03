package org.jfantasy.remind.websocket;

import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.remind.bean.Notice;
import org.jfantasy.remind.service.NoticeService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzzhong on 2014/11/12.
 */
@Component
public class NoticeWebSocket extends TextWebSocketHandler {
    @Autowired
    private NoticeService noticeService;
    private List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
    public NoticeWebSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try{
                        Notice n=noticeService.getNotices();
                        if(n!=null){
                            for (WebSocketSession ws:sessions) {
                                if(!ws.isOpen()){
                                    sessions.remove(ws);
                                    continue;
                                }
                                try {
                                    String messageStr= JSON.serialize(n);
                                    System.out.println("messageStr"+messageStr);
                                    ws.sendMessage(new TextMessage(messageStr));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);

    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }
}
