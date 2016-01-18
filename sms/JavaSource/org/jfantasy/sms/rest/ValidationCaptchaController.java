package org.jfantasy.sms.rest;

import io.swagger.annotations.Api;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.sms.service.ValidationCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void valid(@PathVariable("id") String id, @RequestParam("sessionId") String sessionId, @RequestParam("code") String code) {
        if (!validationCaptchaService.validateResponseForID(id, sessionId, code)) {
            throw new RestException("验证码错误");
        }
    }

}
