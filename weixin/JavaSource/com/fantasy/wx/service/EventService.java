package com.fantasy.wx.service;

import com.fantasy.wx.bean.pojo.UserInfo;
import com.fantasy.wx.bean.pojo.Message;
import com.fantasy.wx.dao.MessageDao;
import com.fantasy.wx.util.WeixinUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 响应微信事件
 */
@Service
@Transactional
public class EventService implements IEventService {

    //微信工具类
    private WeixinUtil weixinUtil=new WeixinUtil();
    @Resource
    private MessageDao messageDao;
    @Resource
    private UserInfoService userInfoService;
    @Override
    public String focusOnEven(Message em) {
        UserInfo ui= weixinUtil.getUserInfo(em.getFromUserName(), WeixinUtil.firstAccessToken());
        userInfoService.save(ui);
        return weixinUtil.textMessage(em.getFromUserName(), em.getToUserName(), "欢迎关注！");
    }
    public String textMessage(Message tm){
        return "";
    }
    public String event(Message em){
        if(em.getEventKey().equals("kszx")){
            String message="请在微信对话框输入\n1.疾病症状\n2.发病原因\n3.过往病史\n4.提交等待医生答复";
            return weixinUtil.textMessage(em.getFromUserName(), em.getToUserName(),message);
        }
        return null;
    }
}
