package com.test;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.sms.service.ShortMessagingService;

import javax.annotation.Resource;

/**
 * 短信验证码测试
 */
public class MobileCaptchaActionTest extends ActionSupport {

    @Resource
    private ShortMessagingService shortMessagingService;

    public String send(String configId, String id, String code) {
        shortMessagingService.getChallengeForID(configId,id,code);

        return null;
    }

    public String validate(String configId, String id, String code) {
        shortMessagingService.validateResponseForID(configId,id,code);

        return null;
    }
}
