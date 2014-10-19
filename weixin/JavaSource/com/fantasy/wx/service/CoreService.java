package com.fantasy.wx.service;


import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.wx.bean.req.BaseMessage;
import com.fantasy.wx.bean.req.EventMessage;
import com.fantasy.wx.bean.req.TextMessage;
import com.fantasy.wx.util.MessageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    public EventService eventService;
    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */
    public  String processRequest(HttpServletRequest request) {
        String respMessage = null;
        String urlPath=request.getScheme()+"://"+request.getServerName()+request.getContextPath();
        try {
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            BaseMessage baseMessage=new BaseMessage();
            // 公众帐号
            baseMessage.setToUserName(requestMap.get("ToUserName"));
            // 发送方帐号（open_id）
            baseMessage.setFromUserName(requestMap.get("FromUserName"));
            baseMessage.setCreateTime(new Date().getTime());
            baseMessage.setMsgType(requestMap.get("MsgType"));
            // 文本消息
            if (baseMessage.getMsgType().equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                TextMessage tm=new TextMessage();
                tm.setContent(requestMap.get("Content").trim());
                return eventService.textMessage(tm);
            }
            // 事件推送
            else if (baseMessage.getMsgType().equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                EventMessage eventMessage=BeanUtil.copyProperties(new EventMessage(),baseMessage);
                // 事件类型
                eventMessage.setEvnet(requestMap.get("Event"));
                eventMessage.setEvnetKey(requestMap.get("EventKey"));
                eventService.saveEventMessage(eventMessage);
                // 订阅
                if (eventMessage.getEvnet().equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                    return eventService.focusOnEven(eventMessage);
                }
            }
            // 回复多客服消息
            baseMessage.setMsgType("transfer_customer_service");
            respMessage = MessageUtil.objectMessageToXml(baseMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }
}
