package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.rest.models.TransactionForm;
import org.jfantasy.pay.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "transactions", description = "交易记录")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @ApiOperation("转账交易")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Transaction save(TransactionForm tx) {
        return transactionService.transfer(tx.getFrom(), tx.getTo(), tx.getAmount(), tx.getNotes());
    }

}
