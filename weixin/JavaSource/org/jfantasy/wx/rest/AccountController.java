package org.jfantasy.wx.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.wx.bean.Account;
import org.jfantasy.wx.service.AccountService;
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
    public Pager<Account> search(Pager<Account> pager, List<PropertyFilter> filters) {
        return this.accountService.findPager(pager, filters);
    }

    /**
     * 获取微信公众账号
     * @param appid
     * @return
     */
    @RequestMapping(value = "/{appid}", method = RequestMethod.GET)
    @ResponseBody
    public Account view(@PathVariable("appid") String appid) {
        return this.accountService.get(appid);
    }

    /**
     * 添加微信公众账号
     * @param article
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Account create(@RequestBody Account article) {
        return this.accountService.save(article);
    }

    /**
     * 更新微信公众账号
     * @param appid
     * @param account
     * @return
     */
    @RequestMapping(value = "/{appid}", method = RequestMethod.PATCH)
    @ResponseBody
    public Account update(@PathVariable("appid") String appid, @RequestBody Account account) {
        account.setAppId(appid);
        return this.accountService.save(account);
    }

    /**
     * 删除微信公众账号
     * @param appid
     */
    @RequestMapping(value = "/{appid}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("appid") String appid) {
        this.accountService.delete(appid);
    }

    /**
     * 批量删除微信公众账号
     * @param appids
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String... appids) {
        this.accountService.delete(appids);
    }

}
