package com.fantasy.wx.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.wx.bean.pojo.UserInfo;
import com.fantasy.wx.bean.req.Message;
import com.fantasy.wx.service.MessageService;
import com.fantasy.wx.service.UserInfoService;
import com.fantasy.wx.util.WeixinUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzzhong on 2014/9/23.
 */
public class MessageAction extends ActionSupport {
    @Resource
    private MessageService messageService;
    @Resource
    private UserInfoService userInfoService;

    public String index(){
        //查询用户列表
        Pager<UserInfo> pager=new Pager<UserInfo>();
        if(!StringUtil.isNotBlank(pager.getOrderBy())){
            pager.setOrderBy("lastMessageTime");
            pager.setOrder(Pager.Order.desc);
        }
        pager.setPageSize(6);
        pager = userInfoService.findPager(pager, new ArrayList<PropertyFilter>());
        this.attrs.put("userPager", pager);
        //查询第一个用户的消息
        List<PropertyFilter> filters=new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_msgType","text"));
        if(pager.getPageItems().size()>0)
            filters.add(new PropertyFilter("EQS_userInfo.openid",pager.getPageItems().get(0).getOpenid()));
        Pager<Message> messagePager=new Pager<Message>();
        messagePager.setPageSize(3);
        this.search(messagePager, filters);
        this.attrs.put("messagePager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }


    public String searchUserInfo(Pager<UserInfo> pager, List<PropertyFilter> filters) {
        if(StringUtil.isNull(pager.getOrderBy())){
            pager.setOrderBy("lastMessageTime");
            pager.setOrder(Pager.Order.desc);
        }
        pager = userInfoService.findPager(pager, filters);
        this.attrs.put(ROOT, pager);
        return JSONDATA;
    }

    public String search(Pager<Message> pager, List<PropertyFilter> filters) {
        if(StringUtil.isNull(pager.getOrderBy())){
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        pager = messageService.findPager(pager, filters);
        this.attrs.put(ROOT, pager);
        return JSONDATA;
    }
    public String sendMessage(Message m){
        m.setType("send");
        WeixinUtil weixinUtil=new WeixinUtil();
        Map<String,String> map= new HashMap<String,String>();
        map.put("content",m.getContent());
        boolean result=weixinUtil.message(WeixinUtil.firstAccessToken(),m.getUserInfo().getOpenid(),m.getMsgType(),map);
        if(result){
            messageService.save(m);
        }
        this.attrs.put(ROOT, result);
        return JSONDATA;
    }


}
