package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.rest.models.RefundForm01;
import org.jfantasy.pay.rest.models.assembler.RefundResourceAssembler;
import org.jfantasy.pay.service.PayService;
import org.jfantasy.pay.service.RefundService;
import org.jfantasy.pay.service.vo.ToRefund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "refund", description = "退款记录")
@RestController
@RequestMapping("/refunds")
public class RefundController {

    private RefundResourceAssembler assembler = new RefundResourceAssembler();

    private final RefundService refundService;
    private final PayService payService;
    @Autowired
    private OrderController orderController;
    @Autowired
    private PaymentController paymentController;
    @Autowired
    private PayConfigController payConfigController;

    @Autowired
    public RefundController( PayService payService, RefundService refundService) {
        this.payService = payService;
        this.refundService = refundService;
    }

    @JsonResultFilter(ignore = @IgnoreProperty(pojo = Refund.class, name = {"order", "payConfig", "payment"}))
    @ApiOperation("查询退款记录")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Refund> pager, List<PropertyFilter> filters) {
        return assembler.toResources(refundService.findPager(pager, filters));
    }

    @JsonResultFilter(ignore = @IgnoreProperty(pojo = Refund.class, name = {"order", "payConfig", "payment"}))
    @ApiOperation(value = "更新退款记录", notes = "该方法只能修改 退款状态 ")
    @RequestMapping(value = "/{sn}", method = RequestMethod.PATCH)
    @ResponseBody
    public ToRefund update(@PathVariable("sn") String sn, @RequestBody RefundForm01 form) {
        return payService.refund(sn, form.getStatus(), form.getRemark());
    }

    @JsonResultFilter(allow = {
            @AllowProperty(pojo = Order.class, name = {"key"}),
            @AllowProperty(pojo = Payment.class, name = {"sn", "totalAmount", "status"}),
            @AllowProperty(pojo = PayConfig.class, name = {"id"})
    })
    @ApiOperation("获取退款记录")
    @RequestMapping(value = "/{sn}", method = RequestMethod.GET)
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("sn") String sn) {
        return assembler.toResource(this.refundService.get(sn));
    }

    @ApiOperation("删除退款记录")
    @RequestMapping(value = "/{sn}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("sn") String sn) {
        this.refundService.delete(sn);
    }

    @ApiOperation("批量删除退款记录")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String... sns) {
        this.refundService.delete(sns);
    }

    @JsonResultFilter(
            ignore = @IgnoreProperty(pojo = Order.class, name = {"refunds", "orderItems", "payments"}),
            allow = @AllowProperty(pojo = PayConfig.class, name = {"id", "name"})
    )
    @RequestMapping(value = "/{sn}/order", method = RequestMethod.GET)
    public ResultResourceSupport order(@PathVariable("sn") String sn) {
        Refund refund = this.refundService.get(sn);
        if (refund == null) {
            throw new NotFoundException("对象不存在");
        }
        return orderController.view(refund.getOrderKey());
    }

    @RequestMapping(value = "/{sn}/payconfig", method = RequestMethod.GET)
    public ResultResourceSupport payconfig(@PathVariable("sn") String sn) {
        Refund refund = this.refundService.get(sn);
        if (refund == null) {
            throw new NotFoundException("对象不存在");
        }
        return payConfigController.view(refund.getPayConfig().getId());
    }

    @RequestMapping(value = "/{sn}/payment", method = RequestMethod.GET)
    public ResultResourceSupport payment(@PathVariable("sn") String sn) {
        Refund refund = this.refundService.get(sn);
        if (refund == null) {
            throw new NotFoundException("对象不存在");
        }
        return paymentController.view(refund.getPayment().getSn());
    }

}
