package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "transactions", description = "交易记录")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private String x(){
        return null;
    }

}
