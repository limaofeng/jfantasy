package com.fantasy.wx.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.schedule.service.ScheduleService;
import com.fantasy.wx.bean.pojo.AccessToken;
import com.fantasy.wx.job.AccessTokenJob;
import com.fantasy.wx.job.StartWeiXin;
import com.fantasy.wx.service.AccessTokenService;
import org.quartz.JobDetail;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 微信app配置
 *
 * @功能描述 管理配置
 * @author 钟振振
 * @since 2014-09-19 上午17:36:52
 * @version 1.0
 */
public class AccessTokenAction extends ActionSupport {


    @Resource
    private AccessTokenService tokenService;
    @Resource
    private ScheduleService scheduleService;

    public String index() {
        this.search(new Pager<AccessToken>(), new ArrayList<PropertyFilter>());
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }
    public String search(Pager<AccessToken> pager, List<PropertyFilter> filters) {
        if(StringUtil.isNotBlank(pager.getOrderBy())){
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        pager = tokenService.findPager(pager, filters);
        this.attrs.put(ROOT, pager);
        return JSONDATA;
    }

    public String save(final AccessToken at) throws JobExecutionException {
        JobDetail jobDetail = this.scheduleService.addJob(JobKey.jobKey("weixin", "accessToken"), AccessTokenJob.class);
        this.scheduleService.removeTrigdger(TriggerKey.triggerKey("accessToken-" + at.getAppid()));
        // 整点触发
        this.scheduleService.addTrigger(jobDetail.getKey(), TriggerKey.triggerKey("accessToken-" + at.getAppid()), 1000*60*60*2,1000000000, new HashMap<String, Object>() {
            {
                this.put("accessToken", JSON.serialize(at));
            }
        });
        this.attrs.put(ROOT, tokenService.save(at));
        return JSONDATA;
    }
    public String edit(Long id) {
        AccessToken at = tokenService.get(id);
        this.attrs.put("at", at);
        return SUCCESS;
    }
    public String delete(Long[] ids) {
        this.tokenService.delete(ids);
        return JSONDATA;
    }

}
