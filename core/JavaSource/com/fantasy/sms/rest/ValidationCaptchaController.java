package com.fantasy.sms.rest;

import com.fantasy.sms.service.ValidationCaptchaService;
import com.wordnik.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "sms-captcha-valid", description = "手机验证码")
@RestController
@RequestMapping("/sms/configs")
public class ValidationCaptchaController {

    @Autowired(required = false)
    private ValidationCaptchaService validationCaptchaService;

    @RequestMapping(value = "/{id}/captchas", method = RequestMethod.POST)
    @ResponseBody
    public String captcha(@PathVariable("id") String id, @RequestBody ValidationCaptcha captcha) {
        return validationCaptchaService.getChallengeForID(id, captcha.getSessionId(), captcha.getMobile());
    }

    @RequestMapping(value = "/{id}/valid", method = RequestMethod.GET)
    @ResponseBody
    public boolean valid(@PathVariable("id") String id, @RequestParam("sessionId") String sessionId, @RequestParam("code") String code) {
        return validationCaptchaService.validateResponseForID(id, sessionId, code);
    }

}
