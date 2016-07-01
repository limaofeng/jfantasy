package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Account;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "accounts", description = "账户")
@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionController transactionController;

    @ApiOperation("查询账户")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Account> search(Pager<Account> pager, List<PropertyFilter> filters) {
        return accountService.findPager(pager, filters);
    }

    @ApiOperation("账户详情")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public Account view(@PathVariable("id") String id) {
        return get(id);
    }

    @ApiOperation("账户交易详情")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/transactions")
    @ResponseBody
    public Pager<ResultResourceSupport> transactions(@PathVariable("id") String sn, Pager<Transaction> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_account.sn",sn));
        return transactionController.seach(pager,filters);
    }

    private Account get(String id){
        return accountService.get(id);
    }

}
