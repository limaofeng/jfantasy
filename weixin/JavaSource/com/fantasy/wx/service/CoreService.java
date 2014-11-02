package com.fantasy.wx.service;


import com.fantasy.wx.bean.req.Message;
import com.fantasy.wx.util.MessageUtil;
import com.fantasy.wx.util.WeixinUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * http://blog.csdn.net/column/details/wechatmp.html
 * wx_url:112.124.102.109/Hello2014
 *
 * 核心服务类
 *
 * Created by zzzhong on 2014/6/17.
 */
@Service
@Transactional
public class CoreService {
    @Resource
    public IEventService eventService;
    @Resource
    private MessageService messageService;
    //微信工具类
    private WeixinUtil weixinUtil=new WeixinUtil();
    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */
    public String processRequest(HttpServletRequest request) {
        String respMessage = null;
        String urlPath=request.getScheme()+"://"+request.getServerName()+request.getContextPath();
        try {
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            requestMap=lowerJSON(requestMap);
            //保存微信触发事件
            Message baseMessage=new Message();
            baseMessage=weixinUtil.toBean(requestMap,baseMessage.getClass());
            messageService.save(baseMessage);

            Message resultMessage= WeixinUtil.toBean(baseMessage, Message.class);
            resultMessage.setMsgType("transfer_customer_service");
            respMessage = MessageUtil.objectMessageToXml(baseMessage);

            String result=null;
            // 文本消息
            if (baseMessage.getMsgType().equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                result=eventService.textMessage(baseMessage);
            }
            // 事件推送
            else if (baseMessage.getMsgType().equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 订阅
                if (baseMessage.getEvent().equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    result=eventService.focusOnEven(baseMessage);
                }
                // 自定义菜单点击事件
                else if (baseMessage.getEvent().equals(MessageUtil.EVENT_TYPE_CLICK)) {
                    result= eventService.event(baseMessage);
                }
            }
            //返回微信消息
            if(result!=null)
                return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }
    public Map<String,String> lowerJSON(Map<String,String> json){
        Map<String,String> map=new HashMap<String,String>();
        for(String key:json.keySet()){
            map.put(key.replaceFirst(key.substring(0, 1),key.substring(0, 1).toLowerCase()),json.get(key));
        }
        return map;
    }
}
