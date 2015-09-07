package com.fantasy.common.order;

import com.fantasy.payment.bean.Payment;

import java.math.BigDecimal;
import java.util.List;

public class TestOrderDetailsService extends AbstractOrderService {

    @Override
    public Order loadOrderBySn(final String sn) {
        return new Order() {

            @Override
            public String getSN() {
                return sn;
            }

            @Override
            public String getType() {
                return "TEST";
            }

            @Override
            public String getSubject() {
                return "测试订单";
            }

            @Override
            public BigDecimal getTotalFee() {
                return BigDecimal.valueOf(0.01);
            }

            @Override
            public BigDecimal getPayableFee() {
                return BigDecimal.valueOf(0.01);
            }

            @Override
            public boolean isPayment() {
                return true;
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
        LOG.debug("订单支付失败");
    }

    @Override
    public void paySuccess(Payment payment) {
        LOG.debug("订单支付成功");
    }

}
