package com.fantasy.mall.order.service;

import com.fantasy.mall.order.bean.Order;
import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.order.AbstractOrderDetailsService;
import com.fantasy.payment.order.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class MallOrderDetailsService extends AbstractOrderDetailsService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDetails loadOrderBySn(String sn) {
        final Order order = orderService.get(sn);
        if (order == null) {
            return null;
        }
        final String subject = order.getOrderItems().get(0).getName();
        return new OrderDetails() {
            @Override
            public String getSN() {
                return order.getSn();
            }

            @Override
            public String getType() {
                return order.getOrderType();
            }

            @Override
            public String getSubject() {
                return subject;
            }

            @Override
            public BigDecimal getTotalFee() {
                return order.getTotalAmount();
            }

            @Override
            public BigDecimal getPayableFee() {
                return order.getTotalAmount().subtract(order.getPaidAmount());
            }

            @Override
            public boolean isPayment() {
                return Order.OrderStatus.unprocessed.equals(order.getOrderStatus()) && (Order.PaymentStatus.unpaid.equals(order.getPaymentStatus()) || Order.PaymentStatus.partPayment.equals(order.getPaymentStatus()));
            }

        };
    }

    @Override
    public void payFailure(Payment payment) {

    }

    @Override
    public void paySuccess(Payment payment) {
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

}
