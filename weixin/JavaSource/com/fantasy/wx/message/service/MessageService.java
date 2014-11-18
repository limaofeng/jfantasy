package com.fantasy.wx.message.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.user.bean.UserInfo;
import com.fantasy.wx.message.bean.Message;
import com.fantasy.wx.message.dao.MessageDao;
import com.fantasy.wx.user.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Service
@Transactional
public class MessageService {
    @Resource
    private MessageDao messageDao;
    @Resource
    private UserInfoService userInfoService;

    /**
     * 列表查询
     *
     * @param pager
     *            分页
     * @param filters
     *            查询条件
     * @return
     */
    public Pager<Message> findPager(Pager<Message> pager, List<PropertyFilter> filters) {
        for(PropertyFilter pf:filters){
            if(pf.getFilterName().equals("EQS_userInfo.openid")){
                 userInfoService.refreshMessage(pf.getPropertyValue().toString());
            }
        }
        return this.messageDao.findPager(pager, filters);
    }

    public Message save(Message message){
        UserInfo ui=userInfoService.getUserInfo(message.getUserInfo().getOpenid());
        long createTime=new Date().getTime();
        message.setCreateTime(createTime);
        if(ui!=null){
            ui.setLastMessageTime(createTime);
            if(message.getType().equals("send")){
                ui.setLastLookTime(createTime);
            }
            userInfoService.save(ui);
        }
        this.messageDao.save(message);
        return message;
    }

}
