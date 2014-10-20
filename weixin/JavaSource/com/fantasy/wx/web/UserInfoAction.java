package com.fantasy.wx.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.wx.bean.pojo.UserInfo;
import com.fantasy.wx.job.StartWeiXin;
import com.fantasy.wx.service.UserInfoService;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 会员操作action
 *
 * @功能描述 该类方法为管理端方法
 * @author 李茂峰
 * @since 2013-12-17 上午10:22:06
 * @version 1.0
 */
public class UserInfoAction extends ActionSupport {

    @Resource
    private UserInfoService userInfoService;

    /**
     * 会员首页
     *
     * @return
     */
    public String index() {
        this.search(new Pager<UserInfo>(), new ArrayList<PropertyFilter>());
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 会员搜索列表
     *
     * @param pager
     * @param filters
     * @return
     */
    public String search(Pager<UserInfo> pager, List<PropertyFilter> filters) {
        if(StringUtil.isNotBlank(pager.getOrderBy())){
            pager.setOrderBy("subscribe_time");
            pager.setOrder(Pager.Order.desc);
        }
        pager = userInfoService.findPager(pager, filters);
        this.attrs.put(ROOT, pager);
        return JSONDATA;
    }

    public String refresh(String appid) throws JobExecutionException, InterruptedException {
        StartWeiXin startWeiXin = SpringContextUtil.getBeanByType(StartWeiXin.class);
//        startWeiXin.initWeiXin();
        userInfoService.refresh(appid);
        return JSONDATA;
    }

}
