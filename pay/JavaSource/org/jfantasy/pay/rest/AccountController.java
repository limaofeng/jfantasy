package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Account;
import org.jfantasy.pay.bean.Point;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.rest.models.AccountForm;
import org.jfantasy.pay.rest.models.ActivateForm;
import org.jfantasy.pay.rest.models.assembler.AccountResourceAssembler;
import org.jfantasy.pay.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 账户 **/
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private AccountResourceAssembler assembler = new AccountResourceAssembler();

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionController transactionController;
    @Autowired
    private PointController pointController;

    /** 查询账户 **/
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Account> pager, List<PropertyFilter> filters) {
        return assembler.toResources(accountService.findPager(pager, filters));
    }

    /** 账户详情 **/
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") String id) {
        return assembler.toResource(get(id));
    }

    /** 添加账户 **/
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResultResourceSupport save(@RequestBody AccountForm form) {
        return assembler.toResource(this.accountService.save(form.getType(),form.getOwner(),form.getPassword()));
    }

    /** 激活账户 **/
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/activate")
    @ResponseBody
    public ResultResourceSupport activate(@PathVariable("id") String sn, @RequestBody ActivateForm form) {
        return assembler.toResource(this.accountService.activate(sn, form.getPassword()));
    }

    /** 账户交易详情 **/
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/transactions")
    @ResponseBody
    public Pager<ResultResourceSupport> transactions(@PathVariable("id") String sn, Pager<Transaction> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_account.sn", sn));
        return transactionController.seach(pager, filters);
    }

    /** 积分记录 **/
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/points")
    @ResponseBody
    public Pager<ResultResourceSupport> points(@PathVariable("id") String sn, Pager<Point> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_account.sn", sn));
        return pointController.search(pager, filters);
    }



    private Account get(String id) {
        return accountService.get(id);
    }

}
