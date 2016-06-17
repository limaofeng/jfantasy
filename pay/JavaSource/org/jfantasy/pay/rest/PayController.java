package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.rest.models.PayForm;
import org.jfantasy.pay.rest.models.RefundForm;
import org.jfantasy.pay.service.PayService;
import org.jfantasy.pay.service.PaymentService;
import org.jfantasy.pay.service.RefundService;
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
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RefundService refundService;

    @JsonIgnoreProperties({@IgnoreProperty(pojo = Payment.class, name = {"order", "payConfig"})})
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ToPayment execute(@RequestBody PayForm payForm) throws PayException {
        return payService.pay(payForm.getPayconfigId(), payForm.getPayType(), payForm.getOrderType(), payForm.getOrderSn(), payForm.getPayer(), payForm.getProperties());
    }

    @JsonIgnoreProperties({@IgnoreProperty(pojo = Refund.class, name = {"order", "payment", "payConfig"})})
    @ApiOperation("支付退款")
    @RequestMapping(value = "/{sn}/refund", method = RequestMethod.POST)
    @ResponseBody
    public Refund refund(@PathVariable("sn") String sn, @RequestBody RefundForm refundForm) throws PayException {
        return payService.refund(sn, refundForm.getAmount(), refundForm.getRemark());
    }

    @JsonIgnoreProperties({
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
