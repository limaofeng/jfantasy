package com.fantasy.wx.job;


import com.fantasy.wx.bean.pojo.AccessToken;
import com.fantasy.wx.util.WeixinUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AccessTokenJob  implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //参数对象
        JobDataMap map = jobExecutionContext.getJobDetail().getJobDataMap();
        AccessToken accessToken=(AccessToken)map.get("accessToken");
        //token值对象
        accessToken= new WeixinUtil().getAccessToken(accessToken);
        if (null != accessToken) {
            WeixinUtil.accessToken.put(accessToken.getAppid(),accessToken);
            System.out.println("当前时间"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        } else {
            execute(jobExecutionContext);
        }
    }
}
