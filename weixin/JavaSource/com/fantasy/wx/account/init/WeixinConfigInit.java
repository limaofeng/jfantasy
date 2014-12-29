package com.fantasy.wx.account.init;

import com.fantasy.file.bean.Directory;
import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.file.service.DirectoryService;
import com.fantasy.framework.util.concurrent.LinkedQueue;
import com.fantasy.wx.handler.WeixinMessageHandler;
import com.fantasy.wx.bean.Message;
import me.chanjar.weixin.mp.api.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 原来的类
 */
@Service
@Deprecated
public class WeixinConfigInit implements InitializingBean {
    protected WxMpConfigStorage wxMpConfigStorage;
    //微信service处理对象
    protected WxMpService util;
    //微信过滤规则
    protected WxMpMessageRouter wxMpMessageRouter;
    @Resource
    private DirectoryService directoryService;
    @Resource
    private WeixinMessageHandler weixinMessageHandler;

    // 微信消息websocket处理集合
    private LinkedQueue<Message> messageQueue = new LinkedQueue<Message>();
    private Map<String, String> wxDirMap = new HashMap<String, String>() {
        {
            put("videoWx", "/static/video/weixin");
            put("imageWx", "/static/images/weixin");
            put("voiceWx", "/static/voice/weixin");
            put("mediaWx", "/static/media/weixin");

        }
    };
    /*@Resource
    private ScheduleService scheduleService;*/

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            /*
            解决原获取微信token遗留trigger 将其删除

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
            }*/
            //初始化文件目录信息
            FileManagerConfig fileManager = new FileManagerConfig();
            fileManager.setId("haolue-upload");
            for (String key : wxDirMap.keySet()) {
                if (directoryService.direcroryKeyUnique(key)) {
                    Directory directory = new Directory();
                    directory.setDirPath(wxDirMap.get(key));
                    directory.setKey(key);
                    directory.setFileManager(fileManager);
                    directoryService.save(directory);
                }
            }


            //从xml获取config对象
            InputStream is1 = this.getClass().getResourceAsStream("/xml/weixin-config.xml");
            WxXmlMpInMemoryConfigStorage config = fromXml(WxXmlMpInMemoryConfigStorage.class, is1);
            //为WeixinConfigInit 配置config对象
            wxMpConfigStorage = config;
            //为微信sdk注入微信配置信息
            util = new WxMpServiceImpl();
            util.setWxMpConfigStorage(config);

            wxMpMessageRouter = new WxMpMessageRouter(util);
            wxMpMessageRouter
                    .rule()
                    .async(false)
                    .handler(weixinMessageHandler)
                    .end();
            //配置接受规则
            /*wxMpMessageRouter = new WxMpMessageRouter()
                    //关注事件
                    .rule()
                    .async(false)
                    .msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_SUBSCRIBE)
                    .handler(new WxMpMessageHandler() {
                        @Override
                        public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context) {

                            UserInfoService service = (UserInfoService) SpringContextUtil.getBean("userInfoService");
                            try {
                                service.refresh(wxMessage.getToUserName());
                            } catch (WxException e) {
                                e.printStackTrace();
                            }
                            WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT().content("欢迎关注").fromUser(wxMessage.getToUserName())
                                    .toUser(wxMessage.getFromUserName()).build();
                            return m;
                        }
                    })
                    .end()
                    //无规则匹配默认返回null
                    .rule().async(false).handler(new WxMpMessageHandler() {
                        @Override
                        public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context) {
                            return null;
                        }
                    }).end();*/
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

    public Map<String, String> getWxDirMap() {
        return wxDirMap;
    }

    public void setWxDirMap(Map<String, String> wxDirMap) {
        this.wxDirMap = wxDirMap;
    }

    public Message getMessage() {
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
