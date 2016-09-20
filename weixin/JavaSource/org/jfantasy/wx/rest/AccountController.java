package org.jfantasy.wx.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.wx.bean.Account;
import org.jfantasy.wx.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 微信公众账号 **/
@RestController("weixin.accountController")
@RequestMapping("/weixin/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /** 查询微信公众账号 - 筛选微信公众账号，返回通用分页对象 **/
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Account> search(@ApiParam(value = "分页对象", name = "pager") Pager<Account> pager, @ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.accountService.findPager(pager, filters);
    }

    @ApiOperation(value = "获取微信公众账号", notes = "通过该接口, 微信公众账号", response = Account.class)
    @RequestMapping(value = "/{appid}", method = RequestMethod.GET)
    @ResponseBody
    public Account view(@PathVariable("appid") String appid) {
        return this.accountService.get(appid);
    }

    @ApiOperation(value = "添加微信公众账号", notes = "通过该接口, 添加微信公众账号", response = Account.class)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Account create(@RequestBody Account article) {
        return this.accountService.save(article);
    }

    @ApiOperation(value = "更新微信公众账号", notes = "通过该接口, 更新微信公众账号")
    @RequestMapping(value = "/{appid}", method = RequestMethod.PATCH)
    @ResponseBody
    public Account update(@PathVariable("appid") String appid, @RequestBody Account account) {
        account.setAppId(appid);
        return this.accountService.save(account);
    }

    @ApiOperation(value = "删除微信公众账号", notes = "通过该接口, 删除微信公众账号")
    @RequestMapping(value = "/{appid}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("appid") String appid) {
        this.accountService.delete(appid);
    }

    @ApiOperation(value = "批量删除微信公众账号", notes = "通过该接口, 批量删除微信公众账号")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String... appids) {
        this.accountService.delete(appids);
    }

}
