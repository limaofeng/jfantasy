package com.fantasy.wx.handler;

import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageInterceptor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * 消息处理
 * Created by zzzhong on 2014/12/8.
 */
@Component
public class WeixinMessageHandler implements WxMpMessageHandler {

    private List<WxMpMessageInterceptor> interceptors = new ArrayList<WxMpMessageInterceptor>();

    private List<WeixinMessageRouter> list=new ArrayList<WeixinMessageRouter>();

    private ExecutorService es = Executors.newCachedThreadPool();
    /**
     * 处理微信推送过来的消息
     * @param wxMessage
     * @return true 代表继续执行别的router，false 代表停止执行别的router
     */
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,WxMpService service) {
        // 先进行拦截器验证
        for (WxMpMessageInterceptor interceptor : this.interceptors) {
            if (!interceptor.intercept(wxMessage, context,service)) {
                return null;
            }
        }

        WxMpXmlOutMessage message=null;
        /**
         * 循环所有处理器的集合
         */
        for(WeixinMessageRouter router:list){
            //判断是否匹配
            if(this.test(wxMessage, router)){
                if(router.getHandler()!=null){
                    message=service(router, wxMessage, context,service);
                }else if(router.getHandlers().size()>0){
                    /**
                     * 循环触发list处理器集合
                     */
                    for(WxMpMessageHandler handler:router.getHandlers()){
                        router.setHandler(handler);
                        message=service(router, wxMessage, context,service);
                    }
                }
                if(router.isReEnter()){
                    break;
                }
            }
        }
        return message==null?null:message;
    }

    /**
     * 触发处理器业务方法
     * @param router
     * @param wxMessage
     * @param context
     * @return
     */
    public WxMpXmlOutMessage service(WeixinMessageRouter router,WxMpXmlMessage wxMessage, Map<String, Object> context,WxMpService service){
        try{
            if(router.isAsync()){
                final WxMpMessageHandler asyncHandler=router.getHandler();
                final WxMpXmlMessage asyncWxMessage=wxMessage;
                final Map<String, Object> asyncContext=context;
                final WxMpService asyncService=service;
                // 在另一个线程里执行
                es.submit(new Runnable() {
                    public void run() {
                        asyncHandler.handle(asyncWxMessage,asyncContext,asyncService);
                    }
                });
                return null;
            }
            return router.getHandler().handle(wxMessage, context,service);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean test(WxMpXmlMessage wxMessage,WeixinMessageRouter router) {
        return
                (router.getMsgType() == null || router.getMsgType().equals(wxMessage.getMsgType()))
                        &&
                        (router.getEvent() == null || router.getEvent().equals(wxMessage.getEvent()))
                        &&
                        (router.getEventKey() == null || router.getEventKey().equals(wxMessage.getEventKey()))
                        &&
                        (router.getContent() == null || router.getContent().equals(wxMessage.getContent() == null ? null : wxMessage.getContent().trim()))
                        &&
                        (router.getrContent() == null || Pattern.matches(router.getrContent(), wxMessage.getContent() == null ? "" : wxMessage.getContent().trim()))
                ;
    }

    public List<WxMpMessageInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<WxMpMessageInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public List<WeixinMessageRouter> getList() {
        return list;
    }

    public void setList(List<WeixinMessageRouter> list) {
        this.list = list;
    }
}
