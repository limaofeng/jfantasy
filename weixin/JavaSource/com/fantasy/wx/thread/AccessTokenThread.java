package com.fantasy.wx.thread;

import com.fantasy.wx.bean.pojo.AccessToken;
import com.fantasy.wx.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zzzhong on 2014/6/19.
 */
@Component
public class AccessTokenThread {
    private static Logger log = LoggerFactory.getLogger(AccessTokenThread.class);
    @Scheduled(fixedDelay = 1000)
    public void getToken(){
        try {
            //参数对象
            //token值对象
            AccessToken  at= new WeixinUtil().getAccessToken(new AccessToken());
            if (null != at) {
                WeixinUtil.accessToken.put(at.getAppid(),at);
                System.out.println("当前时间"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                // 休眠7000秒
                Thread.sleep((7000) * 1000);
            } else {
                // 如果access_token为null，60秒后再获取
                Thread.sleep(60 * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
