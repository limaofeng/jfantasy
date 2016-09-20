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
import org.jfantasy.pay.bean.*;
import org.jfantasy.pay.order.entity.OrderItem;
import org.jfantasy.pay.order.entity.OrderKey;
import org.jfantasy.pay.rest.models.OrderTransaction;
import org.jfantasy.pay.rest.models.assembler.OrderResourceAssembler;
import org.jfantasy.pay.service.AccountService;
import org.jfantasy.pay.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/** 订单 **/
@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderResourceAssembler assembler = new OrderResourceAssembler();

    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentController paymentController;
    @Autowired
    private RefundController refundController;
    @Autowired
    private TransactionController transactionController;
    @Autowired
    private AccountService accountService;

    @JsonResultFilter(
            ignore = @IgnoreProperty(pojo = Order.class, name = {"refunds", "orderItems", "payments"}),
            allow = @AllowProperty(pojo = PayConfig.class, name = {"id", "name"})
    )
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<Order> pager, List<PropertyFilter> filters) {
        return assembler.toResources(orderService.findPager(pager, filters));
    }

    @JsonResultFilter(
            ignore = @IgnoreProperty(pojo = Order.class, name = {"refunds", "orderItems", "payments"}),
            allow = @AllowProperty(pojo = PayConfig.class, name = {"id", "name"})
    )
    /** 获取订单信息 **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") String key) {
        Order order = orderService.get(OrderKey.newInstance(key));
        if(order == null){
            throw new NotFoundException("[ID="+key+"]的订单不存在");
        }
        return assembler.toResource(order);
    }

    @JsonResultFilter(allow = @AllowProperty(pojo = PayConfig.class, name = {"id", "pay_product_id", "name", "platforms","default"}))
    /** 创建订单交易 - 该接口会判断交易是否创建,如果没有交易记录会添加交易订单到交易记录 **/
    @RequestMapping(value = "/{id}/transactions", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResultResourceSupport transaction(@PathVariable("id") String key, @RequestBody OrderTransaction orderTransaction) {
        String from = accountService.findUniqueByCurrentUser().getSn();
        String to = accountService.platform().getSn();
        Order order = orderService.getOrder(OrderKey.newInstance(key));
        Transaction transaction = new Transaction();
        transaction.setFrom(from);
        transaction.setTo(to);
        transaction.setAmount(order.getPayableFee());
        transaction.set(Transaction.ORDER_KEY, key);
        transaction.set(Transaction.ORDER_SUBJECT, order.getSubject());
        transaction.setProject(new Project(orderTransaction.getType().getValue()));
        transaction.setNotes(order.getSubject());
        return transactionController.save(transaction);
    }

    @JsonResultFilter(allow = @AllowProperty(pojo = PayConfig.class, name = {"id", "payProductId", "name", "platforms"}))
    /** 获取订单交易 - 该接口会判断交易是否创建,如果没有交易记录会添加交易订单到交易记录 **/
    @RequestMapping(value = "/{id}/transactions", method = RequestMethod.GET)
    @ResponseBody
    public List<ResultResourceSupport> transactions(@PathVariable("id") String key,List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("INS_unionId", Transaction.generateUnionid(OrderTransaction.Type.payment.getValue(), key), Transaction.generateUnionid(OrderTransaction.Type.refund.getValue(), key)));
        return transactionController.seach(new Pager<Transaction>(), filters).getPageItems();
    }

    @JsonResultFilter(
            ignore = @IgnoreProperty(pojo = Payment.class, name = {"payConfig", "orderKey"}),
            allow = @AllowProperty(pojo = Order.class, name = {"key", "subject"})
    )
    /** 获取订单信息的付款信息 **/
    @RequestMapping(value = "/{id}/payments", method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> payments(@PathVariable("id") String id) {
        OrderKey key = OrderKey.newInstance(id);
        List<PropertyFilter> filters = new ArrayList<>();
        filters.add(new PropertyFilter("EQS_order.type", key.getType()));
        filters.add(new PropertyFilter("EQS_order.sn", key.getSn()));
        return paymentController.search(new Pager<Payment>(), filters);
    }

    @JsonResultFilter(ignore = @IgnoreProperty(pojo = Refund.class, name = {"order", "payConfig", "payment"}))
    /** 获取订单信息的付款信息 **/
    @RequestMapping(value = "/{id}/refunds", method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> refunds(@PathVariable("id") String id) {
        OrderKey key = OrderKey.newInstance(id);
        List<PropertyFilter> filters = new ArrayList<>();
        filters.add(new PropertyFilter("EQS_order.type", key.getType()));
        filters.add(new PropertyFilter("EQS_order.sn", key.getSn()));
        return refundController.search(new Pager<Refund>(), filters);
    }

    /** 获取订单信息的明细信息 **/
    @RequestMapping(value = "/{id}/items", method = RequestMethod.GET)
    @ResponseBody
    public List<OrderItem> items(@PathVariable("id") String id) {
        return get(id).getOrderItems();
    }

    private Order get(String id) {
        return orderService.get(OrderKey.newInstance(id));
    }

}
