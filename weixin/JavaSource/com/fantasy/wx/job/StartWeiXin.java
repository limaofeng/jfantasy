package com.fantasy.wx.job;

import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.schedule.service.ScheduleService;
import com.fantasy.wx.bean.pojo.AccessToken;
import com.fantasy.wx.service.AccessTokenService;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 初始化微信配置
 * Created by zzzhong on 2014/9/19.
 */
@Component
@Lazy(false)
public class StartWeiXin implements InitializingBean {

    @Resource
    private AccessTokenService accessTokenService;
    @Resource
    private ScheduleService scheduleService;

    @Override
    public void afterPropertiesSet() throws Exception {

        List<AccessToken> list = accessTokenService.getAll();
        for (final AccessToken accessToken : list) {
            startAccessToken(accessToken);
        }
    }
    public void startAccessToken(final AccessToken at){
        JobDetail jobDetail = this.scheduleService.addJob(JobKey.jobKey("weixing", "accessToken"), AccessTokenJob.class);
        // 整点触发
        this.scheduleService.removeTrigdger(TriggerKey.triggerKey("accessToken-" + at.getAppid()));
        this.scheduleService.addTrigger(jobDetail.getKey(), TriggerKey.triggerKey("accessToken-" + at.getAppid()), 1000*60*60*2,1000000000, new HashMap<String, Object>() {
            {
                this.put("accessToken", JSON.serialize(at));//at
            }
        });
    }

}
