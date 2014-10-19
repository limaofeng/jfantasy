package com.fantasy.wx.job;

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
@Lazy
public class StartWeiXin implements InitializingBean {

    @Resource
    private AccessTokenService accessTokenService;
    @Resource
    private ScheduleService scheduleService;

    @Override
    public void afterPropertiesSet() throws Exception {
        JobDetail jobDetail = this.scheduleService.addJob(JobKey.jobKey("weixing", "accessToken"), AccessTokenJob.class);
        List<AccessToken> list = accessTokenService.getAll();
        for (final AccessToken accessToken : list) {
            // 每天中午12点触发
            this.scheduleService.addTrigger(jobDetail.getKey(), TriggerKey.triggerKey("accessToken-" + accessToken.getAppid()), "0 0 12 * * ?", new HashMap<String, Object>() {
                {
                    this.put("accessToken", accessToken);
                }
            });
        }
    }

}
