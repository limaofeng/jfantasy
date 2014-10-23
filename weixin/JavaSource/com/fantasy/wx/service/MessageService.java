package com.fantasy.wx.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.bean.pojo.UserInfo;
import com.fantasy.wx.bean.req.Message;
import com.fantasy.wx.dao.MessageDao;
import com.fantasy.wx.dao.UserInfoDao;
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
    private UserInfoDao userInfoDao;

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
        return this.messageDao.findPager(pager, filters);
    }

    public Message save(Message message){
        if(message.getUserInfo()!=null&&message.getUserInfo().getOpenid()!=null){
            UserInfo ui=userInfoDao.get(message.getUserInfo().getOpenid());
            long createTime=new Date().getTime();
            message.setCreateTime(createTime);
            ui.setLastMessageTime(createTime);
            userInfoDao.save(ui);
        }
        this.messageDao.save(message);
        return message;
    }

}
