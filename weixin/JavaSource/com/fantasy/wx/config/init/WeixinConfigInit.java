package com.fantasy.wx.config.init;

import com.fantasy.framework.util.concurrent.LinkedQueue;
import com.fantasy.schedule.service.ScheduleService;
import com.fantasy.wx.message.bean.Message;
import me.chanjar.weixin.mp.api.*;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.InitializingBean;
import org.xml.sax.InputSource;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.InputStream;
import java.util.List;

/**
 * Created by zzzhong on 2014/11/18.
 */
public class WeixinConfigInit implements InitializingBean {
    protected WxMpConfigStorage wxMpConfigStorage;
    /**
     * 微信service处理对象
     */
    protected WxMpService util;
    /**
     * 微信过滤规则
     */
    protected WxMpMessageRouter wxMpMessageRouter;

    /**
     * 微信消息websocket处理集合
     */
    private LinkedQueue<Message> messageQueue = new LinkedQueue<Message>();
    @Resource
    private ScheduleService scheduleService;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            List<? extends TriggerKey> triggerKeys=scheduleService.getTriggers();
            for(TriggerKey key:triggerKeys){
                if(key.getName().indexOf("accessToken")>=0){
                    scheduleService.pauseTrigger(key);
                    scheduleService.removeTrigdger(key);
                }
            }
            List<JobKey>jobKeys= scheduleService.getJobKeys();
            for(JobKey key:jobKeys){
                if(key.getName().indexOf("weixin")>=0){
                    scheduleService.interrupt(key);
                    scheduleService.deleteJob(key);
                }
            }
            InputStream is1 = this.getClass().getResourceAsStream("/xml/weixin-config.xml");
            WxXmlMpInMemoryConfigStorage config = fromXml(WxXmlMpInMemoryConfigStorage.class, is1);
            util = new WxMpServiceImpl();
            util.setWxMpConfigStorage(config);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
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

    @XmlRootElement(name = "xml")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class WxXmlMpInMemoryConfigStorage extends WxMpInMemoryConfigStorage {

        protected String openId;

        public String getOpenId() {
            return openId;
        }
        public void setOpenId(String openId) {
            this.openId = openId;
        }
        @Override
        public String toString() {
            return "SimpleWxConfigProvider [appId=" + appId + ", secret=" + secret + ", accessToken=" + accessToken
                    + ", expiresIn=" + expiresIn + ", token=" + token + ", openId=" + openId + "]";
        }

    }
    public static <T> T fromXml(Class<T> clazz, InputStream is) throws JAXBException {
        Unmarshaller um = JAXBContext.newInstance(clazz).createUnmarshaller();
        InputSource inputSource = new InputSource(is);
        inputSource.setEncoding("utf-8");
        T object = (T) um.unmarshal(inputSource);
        return object;
    }
}
