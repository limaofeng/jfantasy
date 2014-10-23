package com.fantasy.wx.job;


import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.bean.pojo.AccessToken;
import com.fantasy.wx.service.AccessTokenService;
import com.fantasy.wx.util.WeixinUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccessTokenJob  implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        for(int i=0;i<3;i++){
            //参数对象
            JobDataMap map = jobExecutionContext.getMergedJobDataMap();
            AccessToken accessToken= JSON.deserialize(map.get("accessToken").toString(),AccessToken.class);
            //token值对象
            accessToken= new WeixinUtil().getAccessToken(accessToken);
            if (null != accessToken) {
                WeixinUtil.accessToken.put(accessToken.getAppid(),accessToken);
                AccessTokenService tokenService= SpringContextUtil.getBean("accessTokenService",AccessTokenService.class);
                tokenService.save(accessToken);
                System.out.println("当前时间"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                return;
            }else{
                try {
                    Thread.sleep(1000*30);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }


    }
}
