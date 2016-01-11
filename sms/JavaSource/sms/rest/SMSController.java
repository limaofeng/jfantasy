package org.jfantasy.sms.rest;

import io.swagger.annotations.Api;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.sms.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Api(value = "sms", description = "手机短信接口")
@RestController
@RequestMapping("/sms")
public class SMSController {

    @Autowired(required = false)
    private SMSService smsService;

    @RequestMapping(value = "/{mobile}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@PathVariable("mobile") String mobile, @RequestBody String body) {
        if (!smsService.send(mobile, body)) {
            throw new RestException("短信发送失败!错误详情请联系相关技术人员");
        }
    }

}
