package org.jfantasy.pay.listener;

import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.order.entity.PaymentDetails;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.order.entity.enums.PaymentType;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;


public class ONSProducerPayListenerTest {

    @Test
    public void on() throws Exception {
        Payment payment = new Payment();
        payment.setSn("T0001");
        payment.setPayer("limaofeng");
        payment.setBankName("bankName");
        payment.setBankAccount("000001");
        payment.setMemo("memo");
        payment.setType(PaymentType.online);
        payment.setPaymentFee(BigDecimal.ZERO);
        payment.setTotalAmount(BigDecimal.ZERO);
        payment.setPayConfigName("测试配置");
        payment.setTradeNo("TradeNo");
        payment.setOrder(new Order());
        payment.setPayConfig(new PayConfig());
        payment.setStatus(PaymentStatus.close);
        PaymentDetails details = new PaymentDetails();
        BeanUtil.copyProperties(details,payment);
        Assert.assertEquals(payment.getStatus(),details.getStatus());
    }

}