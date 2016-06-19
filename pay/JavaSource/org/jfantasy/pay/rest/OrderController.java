package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.order.entity.OrderItem;
import org.jfantasy.pay.order.entity.OrderKey;
import org.jfantasy.pay.rest.models.assembler.OrderResourceAssembler;
import org.jfantasy.pay.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "orders", description = "订单")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderResourceAssembler assembler = new OrderResourceAssembler();

    @Autowired
    private OrderService orderService;
    @Autowired
    private PayConfigController payConfigController;
    @Autowired
    private PaymentController paymentController;
    @Autowired
    private RefundController refundController;

    @JsonIgnoreProperties(
            value = @IgnoreProperty(pojo = Order.class, name = {"refunds", "orderItems", "payments"}),
            allow = @AllowProperty(pojo = PayConfig.class, name = {"id", "name"})
    )
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Order> pager, List<PropertyFilter> filters) {
        return assembler.toResources(orderService.findPager(pager, filters));
    }

    @JsonIgnoreProperties(
            value = @IgnoreProperty(pojo = Order.class, name = {"refunds", "orderItems", "payments"}),
            allow = @AllowProperty(pojo = PayConfig.class, name = {"id", "name"})
    )
    @ApiOperation("获取订单信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") String key) {
        return assembler.toResource(orderService.get(OrderKey.newInstance(key)));
    }

    @JsonIgnoreProperties(
            value = @IgnoreProperty(pojo = Payment.class, name = {"payConfig", "orderKey"}),
            allow = @AllowProperty(pojo = Order.class, name = {"key", "subject"})
    )
    @ApiOperation("获取订单信息的付款信息")
    @RequestMapping(value = "/{id}/payments", method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> payments(@PathVariable("id") String id) {
        OrderKey key = OrderKey.newInstance(id);
        List<PropertyFilter> filters = new ArrayList<>();
        filters.add(new PropertyFilter("EQS_order.type",  key.getType()));
        filters.add(new PropertyFilter("EQS_order.sn", key.getSn()));
        return paymentController.search(new Pager<Payment>(), filters);
    }

    @JsonIgnoreProperties({@IgnoreProperty(pojo = Refund.class, name = {"order", "payConfig", "payment"})})
    @ApiOperation("获取订单信息的付款信息")
    @RequestMapping(value = "/{id}/refunds", method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> refunds(@PathVariable("id") String id) {
        OrderKey key = OrderKey.newInstance(id);
        List<PropertyFilter> filters = new ArrayList<>();
        filters.add(new PropertyFilter("EQS_order.type", key.getType()));
        filters.add(new PropertyFilter("EQS_order.sn", key.getSn()));
        return refundController.search(new Pager<Refund>(), filters);
    }

    @ApiOperation("获取订单信息的明细信息")
    @RequestMapping(value = "/{id}/items", method = RequestMethod.GET)
    @ResponseBody
    public List<OrderItem> items(@PathVariable("id") String id) {
        return get(id).getOrderItems();
    }

    private Order get(String id) {
        return orderService.get(OrderKey.newInstance(id));
    }

}
