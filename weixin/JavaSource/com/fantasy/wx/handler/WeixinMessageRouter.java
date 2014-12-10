package com.fantasy.wx.handler;

import me.chanjar.weixin.mp.api.WxMpMessageHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 匹配规则
 * Created by zzzhong on 2014/12/8.
 */
public class WeixinMessageRouter {

    /**
     * 设置是否异步执行，默认是false
     * @param async
     * @return
     */
    private boolean async = false;

    /**
     * 如果msgType等于某值
     * @param msgType
     * @return
     */
    private String msgType;

    /**
     * 如果event等于某值
     * @param event
     * @return
     */
    private String event;

    /**
     * 如果eventKey等于某值
     * @param eventKey
     * @return
     */
    private String eventKey;

    /**
     * 如果content等于某值
     * @param content
     * @return
     */
    private String content;

    /**
     * 如果content匹配该正则表达式
     * @param regex
     * @return
     */
    private String rContent;

    /**
     * 是否返回
     */
    private boolean reEnter = false;

    /**
     * 设置微信消息处理器
     * @param handler
     * @return
     */
    private WxMpMessageHandler handler;

    private List<WxMpMessageHandler> handlers=new ArrayList<WxMpMessageHandler>();

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getrContent() {
        return rContent;
    }

    public void setrContent(String rContent) {
        this.rContent = rContent;
    }

    public boolean isReEnter() {
        return reEnter;
    }

    public void setReEnter(boolean reEnter) {
        this.reEnter = reEnter;
    }

    public WxMpMessageHandler getHandler() {
        return handler;
    }

    public void setHandler(WxMpMessageHandler handler) {
        this.handler = handler;
    }

    public List<WxMpMessageHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<WxMpMessageHandler> handlers) {
        this.handlers = handlers;
    }
}
