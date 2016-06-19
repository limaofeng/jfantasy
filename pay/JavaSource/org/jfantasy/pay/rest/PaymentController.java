package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.rest.models.assembler.PaymentResourceAssembler;
import org.jfantasy.pay.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "payments", description = "支付记录")
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private PaymentResourceAssembler assembler = new PaymentResourceAssembler();

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PayConfigController payConfigController;
    @Autowired
    private OrderController orderController;

    @JsonIgnoreProperties(
            value = @IgnoreProperty(pojo = Payment.class, name = {"payConfig", "orderKey"}),
            allow = @AllowProperty(pojo = Order.class, name = {"type", "subject", "sn"})
    )
    @ApiOperation("查询支付记录")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Payment> pager, List<PropertyFilter> filters) {
        return assembler.toResources(paymentService.findPager(pager, filters));
    }

    @ApiOperation("获取支付记录")
    @RequestMapping(value = "/{sn}", method = RequestMethod.GET)
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("sn") String sn) {
        return assembler.toResource(this.paymentService.get(sn));
    }

    @ApiOperation("支付记录对应的支付配置信息")
    @RequestMapping(value = "/{sn}/payconfig", method = RequestMethod.GET)
    @ResponseBody
    public ResultResourceSupport payconfig(@PathVariable("sn") String sn) {
        return payConfigController.view(get(sn).getPayConfig().getId());
    }

    @JsonIgnoreProperties(
            value = @IgnoreProperty(pojo = Order.class, name = {"refunds", "orderItems", "payments"}),
            allow = {@AllowProperty(pojo = PayConfig.class, name = {"id", "name"}),
                    @AllowProperty(pojo = Payment.class, name = {"id", "name"})}
    )
    @ApiOperation(value = "支付记录对应的订单信息", notes = "支付记录对应的订单信息")
    @RequestMapping(value = "/{sn}/order", method = RequestMethod.GET)
    @ResponseBody
    public ResultResourceSupport order(@PathVariable("sn") String sn) {
        return orderController.view(get(sn).getOrderKey());
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

    private Payment get(String sn) {
        Payment payment = this.paymentService.get(sn);
        if (payment == null) {
            throw new NotFoundException("[sn=" + sn + "]对应的支付记录未找到");
        }
        return payment;
    }

}
