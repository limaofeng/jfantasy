package com.fantasy.sms.rest;

import com.fantasy.sms.bean.CaptchaConfig;
import com.fantasy.sms.service.CaptchaConfigService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "sms-captcha-configs", description = "手机验证码配置")
@RestController
@RequestMapping("/sms/configs")
public class CaptchaConfigController {

    @Autowired
    private CaptchaConfigService captchaConfigService;

    @ApiOperation(value = "手机验证码配置", notes = "筛选文章，返回全部的手机验证码配置")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<CaptchaConfig> search() {
        return this.captchaConfigService.getAll();
    }

    @ApiOperation(value = "获取手机验证码配置", notes = "通过该接口, 获取单篇手机验证码配置", response = CaptchaConfig.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CaptchaConfig view(@PathVariable("id") String id) {
        return this.captchaConfigService.get(id);
    }

    @ApiOperation(value = "添加手机验证码配置", notes = "通过该接口, 添加手机验证码配置", response = CaptchaConfig.class)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public CaptchaConfig create(@RequestBody CaptchaConfig config) {
        return this.captchaConfigService.save(config);
    }

    @ApiOperation(value = "更新手机验证码配置", notes = "通过该接口, 更新手机验证码配置")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CaptchaConfig update(@PathVariable("id") String id, @RequestBody CaptchaConfig config) {
        config.setId(id);
        return this.captchaConfigService.save(config);
    }

    @ApiOperation(value = "删除手机验证码配置", notes = "通过该接口, 删除手机验证码配置")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        this.captchaConfigService.delete(id);
    }

    @ApiOperation(value = "批量删除手机验证码配置", notes = "通过该接口, 批量删除手机验证码配置")
    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestBody String... id) {
        this.captchaConfigService.delete(id);
    }

}
