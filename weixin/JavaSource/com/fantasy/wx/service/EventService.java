package com.fantasy.wx.service;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.wx.bean.req.EventMessage;
import com.fantasy.wx.bean.req.TextMessage;
import com.fantasy.wx.dao.EventMessageDao;
import com.fantasy.wx.dao.TextMessageDao;
import com.fantasy.wx.util.WeixinUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by zzzhong on 2014/6/30.
 */
@Service
@Transactional
public class EventService {

    //微信工具类
    private WeixinUtil weixinUtil=new WeixinUtil();
    @Resource
    private TextMessageDao textMessageDao;
    @Resource
    private EventMessageDao eventMessageDao;

    public String focusOnEven(EventMessage em) throws IOException {
        return weixinUtil.textMessage(em.getFromUserName(), em.getToUserName(), "欢迎关注！");
    }
    public String textMessage(TextMessage tm){
        textMessageDao.save(tm);
        return weixinUtil.textMessage(tm.getFromUserName(), tm.getToUserName(), "输入的是文字！");
    }
    public void saveEventMessage(EventMessage em){
        eventMessageDao.save(em);
    }
}
