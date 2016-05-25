package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "payments", description = "支付记录")
@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @JsonIgnoreProperties({@IgnoreProperty(pojo = Payment.class, name = {"order", "payConfig"})})
    @ApiOperation("查询支付记录")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Payment> search(Pager<Payment> pager, List<PropertyFilter> filters) {
        return paymentService.findPager(pager, filters);
    }

    @ApiOperation("获取支付记录")
    @RequestMapping(value = "/{sn}", method = RequestMethod.GET)
    @ResponseBody
    public Payment view(@PathVariable("sn") String sn) {
        return this.paymentService.get(sn);
    }

    @ApiOperation("支付记录对应的支付配置信息")
    @RequestMapping(value = "/{sn}/payconfig", method = RequestMethod.GET)
    @ResponseBody
    public PayConfig payconfig(@PathVariable("id") String sn) {
        Payment payment = this.paymentService.get(sn);
        if (payment == null) {
            throw new NotFoundException("[sn=" + sn + "]对应的支付记录未找到");
        }
        return payment.getPayConfig();
    }

    @ApiOperation(value = "支付记录对应的订单信息", notes = "支付记录对应的订单信息")
    @RequestMapping(value = "/{sn}/order", method = RequestMethod.GET)
    @ResponseBody
    public Order order(@PathVariable("sn") String sn) {
        return view(sn).getOrder();
    }

    @ApiOperation("删除支付记录")
    @RequestMapping(value = "/{sn}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("sn") String sn) {
        this.paymentService.delete(sn);
    }

    @ApiOperation("批量删除支付记录")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String... sns) {
        this.paymentService.delete(sns);
    }

}
