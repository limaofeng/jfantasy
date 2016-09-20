package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.rest.models.RefundForm;
import org.jfantasy.pay.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 支付操作 **/
@RestController
@RequestMapping("/pays")
public class PayController {

    private final PayService payService;

    @Autowired
    public PayController(PayService payService) {
        this.payService = payService;
    }

    @JsonResultFilter(ignore = {@IgnoreProperty(pojo = Refund.class, name = {"order", "payment", "payConfig"})})
    /** 支付退款 **/
    @RequestMapping(value = "/{sn}/refund", method = RequestMethod.POST)
    @ResponseBody
    public Refund refund(@PathVariable("sn") String sn, @RequestBody RefundForm refundForm) throws PayException {
        return payService.refund(sn, refundForm.getAmount(), refundForm.getRemark());
    }

    @JsonResultFilter(ignore = {
            @IgnoreProperty(pojo = Order.class, name = {"payConfig"}),
            @IgnoreProperty(pojo = Payment.class, name = {"order", "payConfig"}),
            @IgnoreProperty(pojo = Refund.class, name = {"order", "payment", "payConfig"})
    })
    /** 支付通知 - 用于第三方支付通知系统 **/
    @RequestMapping(value = "/{sn}/notify", method = RequestMethod.POST)
    @ResponseBody
    public Object notify(@PathVariable("sn") String sn, @RequestBody String body) throws PayException {
        if (sn.startsWith("RP")) {
            return payService.refundNotify(sn, body);
        } else if (sn.startsWith("P")) {
            return payService.paymentNotify(sn, body);
        } else {
            throw new PayException("不能处理的订单");
        }
    }

    @RequestMapping(value = "/{sn}/query", method = RequestMethod.GET)
    public boolean query(@PathVariable("sn") String sn) throws PayException {
        return payService.query(sn);
    }

}
