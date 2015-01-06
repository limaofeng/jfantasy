package com.fantasy.wx.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.wx.bean.Message;
import com.fantasy.wx.service.MessageWeiXinService;
import com.fantasy.wx.bean.UserInfo;
import com.fantasy.wx.user.service.IUserInfoService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzzhong on 2014/9/23.
 */
public class MessageAction extends ActionSupport {
    @Resource
    private MessageWeiXinService messageWeiXinService;
    @Resource
    private IUserInfoService iUserInfoService;


    public String index() {
        //查询用户列表
        Pager<UserInfo> pager = new Pager<UserInfo>();
        if (!StringUtil.isNotBlank(pager.getOrderBy())) {
            pager.setOrderBy("lastMessageTime");
            pager.setOrder(Pager.Order.desc);
        }
        pager.setPageSize(6);
        pager.getPageItems();
        pager = iUserInfoService.findPager(pager, new ArrayList<PropertyFilter>());

        //查询第一个用户的消息
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        if (pager.getPageItems().size() > 0)
            filters.add(new PropertyFilter("EQS_userInfo.openid", pager.getPageItems().get(0).getOpenId()));
        Pager<Message> messagePager = new Pager<Message>();
        messagePager.setPageSize(3);
        this.search(messagePager, filters);
        List<UserInfo> list = pager.getPageItems();
        if (list != null && list.size() != 0) {
            UserInfo ui = pager.getPageItems().get(0);
            ui.setLastLookTime(ui.getLastMessageTime());
        }
        //查询所有用户未读的消息数
        iUserInfoService.countUnReadSize(pager.getPageItems());
        this.attrs.put("userPager", pager);
        this.attrs.put("messagePager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 搜索用户列表
     *
     * @param pager
     * @param filters
     * @return
     */
    public String searchUserInfo(Pager<UserInfo> pager, List<PropertyFilter> filters) {
        if (StringUtil.isNull(pager.getOrderBy())) {
            pager.setOrderBy("lastMessageTime");
            pager.setOrder(Pager.Order.desc);
        }
        pager = iUserInfoService.findPager(pager, filters);
        iUserInfoService.countUnReadSize(pager.getPageItems());
        this.attrs.put(ROOT, pager);
        return JSONDATA;
    }

    /**
     * 搜索消息
     *
     * @param pager
     * @param filters
     * @return
     */
    public String search(Pager<Message> pager, List<PropertyFilter> filters) {
        if (StringUtil.isNull(pager.getOrderBy())) {
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        filters.add(new PropertyFilter("EQS_msgType", "text"));
        pager = messageWeiXinService.findPager(pager, filters);
        this.attrs.put(ROOT, pager);
        return JSONDATA;
    }

    /**
     * 发送消息
     *
     * @param m
     * @return
     */
    public String sendMessage(Message m) {
        this.attrs.put(ROOT, messageWeiXinService.save(m));
        return JSONDATA;
    }


}
