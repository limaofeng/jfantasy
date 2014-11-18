package com.fantasy.wx.config.init;

import com.fantasy.framework.util.concurrent.LinkedQueue;
import com.fantasy.wx.message.bean.Message;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * Created by zzzhong on 2014/11/18.
 */
public class WeixinConfigInit implements InitializingBean {
    protected WxMpConfigStorage wxMpConfigStorage;
    /**
     * 微信service处理对象
     */
    @Resource
    protected WxMpService util;
    /**
     * 微信过滤规则
     */
    protected WxMpMessageRouter wxMpMessageRouter;

    /**
     * 微信消息websocket处理集合
     */
    private LinkedQueue<Message> messageQueue = new LinkedQueue<Message>();


    @Override
    public void afterPropertiesSet() throws Exception {

        WxMpInMemoryConfigStorage config=new WxMpInMemoryConfigStorage();
    }

    public WxMpConfigStorage getWxMpConfigStorage() {
        return wxMpConfigStorage;
    }

    public void setWxMpConfigStorage(WxMpConfigStorage wxMpConfigStorage) {
        this.wxMpConfigStorage = wxMpConfigStorage;
    }

    public WxMpMessageRouter getWxMpMessageRouter() {
        return wxMpMessageRouter;
    }

    public void setWxMpMessageRouter(WxMpMessageRouter wxMpMessageRouter) {
        this.wxMpMessageRouter = wxMpMessageRouter;
    }

    public WxMpService getUtil() {
        return util;
    }

    public void setUtil(WxMpService util) {
        this.util = util;
    }

    public LinkedQueue<Message> getMessageQueue() {
        return messageQueue;
    }

    public void setMessageQueue(LinkedQueue<Message> messageQueue) {
        this.messageQueue = messageQueue;
    }
    public void addMessage(Message m) throws InterruptedException {
        messageQueue.put(m);
    }
    public Message getMessage(){
        try {
            return messageQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
