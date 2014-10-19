package com.fantasy.mall.order.service;

import com.fantasy.mall.order.bean.Order;
import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.error.PaymentException;
import com.fantasy.payment.service.PaymentOrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class MallPaymentOrderService implements PaymentOrderService{

    @Autowired
    private OrderService orderService;

    @Override
    public void payCheck(String sn) throws PaymentException {

    }

    @Override
    public BigDecimal totalAmount(String sn) {
        return null;
    }

    @Override
    public BigDecimal amountPayable(String sn) {
        return null;
    }

    @Override
    public String paymentType(String sn) {
        return null;
    }

    @Override
    public void ready(Payment payment) {

    }

    @Override
    public void success(Payment payment) {
        Order order = orderService.get(payment.getOrderSn());
        order.setPaidAmount(order.getTotalAmount());
        order.setPaymentStatus(Order.PaymentStatus.paid);
        // 更新支付金额
        order.setPaidAmount(order.getPaidAmount().add(payment.getTotalAmount()));
        order.setPaymentStatus(Order.PaymentStatus.partPayment);
        // 如果已付金额与支付金额相等更新订单状态为已支付
        if (order.getPaidAmount().equals(order.getPaymentFee())) {
            order.setPaymentStatus(Order.PaymentStatus.paid);
        }
        orderService.save(order);
    }

    @Override
    public String url(String sn) {
        return null;
    }
}
