package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.order.Order;
import org.jfantasy.pay.rest.form.PayForm;
import org.jfantasy.pay.service.PayService;
import org.jfantasy.pay.service.vo.ToPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "pay", description = "支付操作")
@RestController
@RequestMapping("/pays")
public class PayController {

    @Autowired
    private PayService payService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ToPayment execute(@RequestBody PayForm payForm) throws PayException {
        return payService.pay(payForm.getPayconfigId(), payForm.getPayType(), payForm.getOrderType(), payForm.getOrderSn(), payForm.getPayer(), payForm.getProperties());
        //payService.buildRequest(payForm.getOrderType(), payForm.getOrderSn(), payForm.getPayconfigId(), payForm.getPayer(), payForm.getParameters());
    }

    @ApiOperation("支付通知")
    @RequestMapping(value = "/{sn}/notify", method = RequestMethod.POST)
    @ResponseBody
    public Order notify(@PathVariable("sn") String sn, @RequestBody String body) throws PayException {
        return payService.notify(sn,body);
    }

}
