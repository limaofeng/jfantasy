package com.fantasy.sms.rest;

import com.fantasy.sms.SMSService;
import com.wordnik.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "sms", description = "手机短信接口")
@RestController
@RequestMapping("/sms")
public class SMSController {

    @Autowired(required = false)
    private SMSService smsService;

    @RequestMapping(value = "/{mobile}", method = RequestMethod.POST)
    public void create(@PathVariable("mobile") String mobile, @RequestBody String body) {
        smsService.send(mobile, body);
    }

}
