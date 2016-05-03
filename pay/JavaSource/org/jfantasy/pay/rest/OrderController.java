package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.order.entity.OrderKey;
import org.jfantasy.pay.service.OrderService;
import org.jfantasy.pay.service.PaymentService;
import org.jfantasy.pay.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "orders", description = "订单")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RefundService refundService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Order> search(Pager<Order> pager, List<PropertyFilter> filters) {
        return orderService.findPager(pager,filters);
    }

    @ApiOperation("获取订单信息")
    @RequestMapping(value = "/{type}:{sn}", method = RequestMethod.GET)
    @ResponseBody
    public Order view(@PathVariable("type") String type, @PathVariable("sn") String sn) {
        return orderService.get(OrderKey.newInstance(type,sn));
    }

    @ApiOperation("获取订单信息的付款信息")
    @RequestMapping(value = "/{type}:{sn}/payments", method = RequestMethod.GET)
    @ResponseBody
    public Pager<Payment> payments(@PathVariable("type") String type, @PathVariable("sn") String sn, Pager<Payment> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_orderType", type));
        filters.add(new PropertyFilter("EQS_orderSn", sn));
        return paymentService.findPager(pager, filters);
    }

    @ApiOperation("获取订单信息的付款信息")
    @RequestMapping(value = "/{type}:{sn}/refunds", method = RequestMethod.GET)
    @ResponseBody
    public Pager<Refund> refunds(@PathVariable("type") String type, @PathVariable("sn") String sn, Pager<Refund> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_orderType", type));
        filters.add(new PropertyFilter("EQS_orderSn", sn));
        return refundService.findPager(pager, filters);
    }

}
