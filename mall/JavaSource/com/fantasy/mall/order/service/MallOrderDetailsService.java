package com.fantasy.mall.order.service;

import com.fantasy.common.order.*;
import com.fantasy.payment.bean.Payment;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public class MallOrderDetailsService extends AbstractOrderService {

    @Autowired
    private OrderService orderService;

    @Override
    public Order loadOrderBySn(String sn) {
        final com.fantasy.mall.order.bean.Order order = orderService.get(sn);
        if (order == null) {
            return null;
        }
        final String subject = order.getOrderItems().get(0).getName();
        return new Order() {
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
                return com.fantasy.mall.order.bean.Order.OrderStatus.unprocessed.equals(order.getOrderStatus()) && (com.fantasy.mall.order.bean.Order.PaymentStatus.unpaid.equals(order.getPaymentStatus()) || com.fantasy.mall.order.bean.Order.PaymentStatus.partPayment.equals(order.getPaymentStatus()));
            }

            @Override
            public List<OrderItem> getOrderItems() {
                return null;
            }

            @Override
            public ShipAddress getShipAddress() {
                return null;
            }

        };
    }

    @Override
    public void payFailure(Payment payment) {

    }

    @Override
    public void paySuccess(Payment payment) {
        com.fantasy.mall.order.bean.Order order = orderService.get(payment.getOrderSn());
        order.setPaidAmount(order.getTotalAmount());
        order.setPaymentStatus(com.fantasy.mall.order.bean.Order.PaymentStatus.paid);
        // 更新支付金额
        order.setPaidAmount(order.getPaidAmount().add(payment.getTotalAmount()));
        order.setPaymentStatus(com.fantasy.mall.order.bean.Order.PaymentStatus.partPayment);
        // 如果已付金额与支付金额相等更新订单状态为已支付
        if (order.getPaidAmount().equals(order.getPaymentFee())) {
            order.setPaymentStatus(com.fantasy.mall.order.bean.Order.PaymentStatus.paid);
        }
        orderService.save(order);
    }

    @Override
    public OrderUrls getOrderUrls() {
        return null;
    }

}
