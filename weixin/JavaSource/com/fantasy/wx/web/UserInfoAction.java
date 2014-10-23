package com.fantasy.wx.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.wx.bean.pojo.UserInfo;
import com.fantasy.wx.job.StartWeiXin;
import com.fantasy.wx.service.UserInfoService;
import com.fantasy.wx.util.WeixinUtil;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class UserInfoAction extends ActionSupport {

    @Resource
    private UserInfoService userInfoService;

    public String index() {
        this.search(new Pager<UserInfo>(), new ArrayList<PropertyFilter>());
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.put("appid", WeixinUtil.firstAccessToken().getAppid());
        this.attrs.remove(ROOT);
        return SUCCESS;
    }
    public String search(Pager<UserInfo> pager, List<PropertyFilter> filters) {
        if(StringUtil.isNotBlank(pager.getOrderBy())){
            pager.setOrderBy("subscribe_time");
            pager.setOrder(Pager.Order.desc);
        }
        pager = userInfoService.findPager(pager, filters);
        this.attrs.put(ROOT, pager);
        return JSONDATA;
    }

    public String refresh(String appid){
        userInfoService.refresh(appid);
        return JSONDATA;
    }

}
