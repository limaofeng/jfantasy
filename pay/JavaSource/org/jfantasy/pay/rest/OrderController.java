package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.product.order.Order;
import org.jfantasy.pay.product.order.OrderService;
import org.jfantasy.pay.product.order.OrderServiceFactory;
import org.jfantasy.pay.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "orders", description = "订单")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderServiceFactory orderServiceFactory;
    @Autowired
    private PaymentService paymentService;

    @ApiOperation("获取订单信息")
    @RequestMapping(value = "/{type}:{sn}", method = RequestMethod.GET)
    @ResponseBody
    public Order view(@PathVariable("type") String type, @PathVariable("sn") String sn) {
        OrderService orderService = orderServiceFactory.getOrderService(type);
        return orderService.loadOrder(sn);
    }

    @ApiOperation("获取订单信息的付款信息")
    @RequestMapping(value = "/{type}:{sn}/payments", method = RequestMethod.GET)
    @ResponseBody
    public Pager<Payment> payments(@PathVariable("type") String type, @PathVariable("sn") String sn, Pager<Payment> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_orderType", type));
        filters.add(new PropertyFilter("EQS_orderSn", sn));
        return paymentService.findPager(pager, filters);
    }

}
