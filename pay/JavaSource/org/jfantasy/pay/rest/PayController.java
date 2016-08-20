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
import org.jfantasy.pay.service.PaymentService;
import org.jfantasy.pay.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "pay", description = "支付操作")
@RestController
@RequestMapping("/pays")
public class PayController {

    private final PayService payService;
    private final PaymentService paymentService;
    private final RefundService refundService;

    @Autowired
    public PayController(PayService payService, PaymentService paymentService, RefundService refundService) {
        this.payService = payService;
        this.paymentService = paymentService;
        this.refundService = refundService;
    }

    @JsonResultFilter(ignore = {@IgnoreProperty(pojo = Refund.class, name = {"order", "payment", "payConfig"})})
    @ApiOperation("支付退款")
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
    @ApiOperation(value = "支付通知", notes = "用于第三方支付通知系统")
    @RequestMapping(value = "/{sn}/notify", method = RequestMethod.POST)
    @ResponseBody
    public Object notify(@PathVariable("sn") String sn, @RequestBody String body) throws PayException {
        if (sn.startsWith("RP")) {
            return payService.notify(refundService.get(sn), body);
        } else if (sn.startsWith("P")) {
            return payService.notify(paymentService.get(sn), body);
        } else {
            throw new PayException("不能处理的订单");
        }
    }

}
