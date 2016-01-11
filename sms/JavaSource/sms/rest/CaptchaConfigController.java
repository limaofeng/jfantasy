package org.jfantasy.sms.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.sms.bean.CaptchaConfig;
import org.jfantasy.sms.service.CaptchaConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

@Api(value = "sms-captcha-configs", description = "手机验证码配置")
@RestController
@RequestMapping("/sms/configs")
public class CaptchaConfigController {

    @Autowired
    private CaptchaConfigService captchaConfigService;

    @ApiOperation(value = "手机验证码配置", notes = "筛选文章，返回全部的手机验证码配置",response = CaptchaConfig[].class)
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<CaptchaConfig> search(Pager<CaptchaConfig> pager, List<PropertyFilter> filters) {
        return this.captchaConfigService.findPager(pager, filters);
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
