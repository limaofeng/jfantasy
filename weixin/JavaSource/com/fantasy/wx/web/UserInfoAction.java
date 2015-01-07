package com.fantasy.wx.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.bean.UserInfo;
import com.fantasy.wx.service.UserInfoWeiXinService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class UserInfoAction extends ActionSupport {

    @Resource
    private UserInfoWeiXinService userInfoWeiXinService;

    public String index() {
        this.search(new Pager<UserInfo>(), new ArrayList<PropertyFilter>());
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    public String search(Pager<UserInfo> pager, List<PropertyFilter> filters) {
        if (StringUtil.isNotBlank(pager.getOrderBy())) {
            pager.setOrderBy("subscribe_time");
            pager.setOrder(Pager.Order.desc);
        }
        pager = userInfoWeiXinService.findPager(pager, filters);
        this.attrs.put(ROOT, pager);
        return JSONDATA;
    }

    public String refresh() throws WeiXinException {
        userInfoWeiXinService.refresh();
        return JSONDATA;
    }

}
