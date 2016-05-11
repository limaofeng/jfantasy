package org.jfantasy.pay.service;

import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.jackson.ThreadJacksonMixInHolder;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.pay.ApplicationTest;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.service.vo.ToPayment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationTest.class)
public class PayServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    @Transactional
    public void pay() throws Exception {
        ThreadJacksonMixInHolder holder = ThreadJacksonMixInHolder.getMixInHolder();
        holder.addIgnorePropertyNames(Payment.class,"order","payConfig");

        List<Payment> payments = paymentService.find(Restrictions.eq("order.type","consulting"),Restrictions.eq("order.sn","C2016050900002"));
        Payment payment = payments.get(0);

        ToPayment toPayment = new ToPayment();
        BeanUtil.copyProperties(toPayment, payment, "status", "type");
        toPayment.setStatus(payment.getStatus());
        toPayment.setType(payment.getType());
        JSON.serialize(toPayment);
    }

}