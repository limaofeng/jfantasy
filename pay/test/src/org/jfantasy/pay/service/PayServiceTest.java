package org.jfantasy.pay.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.jackson.ThreadJacksonMixInHolder;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.pay.ApplicationTest;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.order.entity.OrderKey;
import org.jfantasy.pay.order.entity.PaymentDetails;
import org.jfantasy.pay.order.entity.RefundDetails;
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

    private final static Log LOG = LogFactory.getLog(PayServiceTest.class);

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RefundService refundService;

    @Test
    @Transactional
    public void paymentDetailstoJSON(){
        List<Payment> payments = paymentService.find(new Criterion[0]);

        Payment payment = payments.get(0);

        PaymentDetails details = new PaymentDetails();
        BeanUtil.copyProperties(details, payment);
        details.setOrderKey(OrderKey.newInstance(payment.getOrder().getType(), payment.getOrder().getSn()));
        details.setPayConfigId(payment.getPayConfig().getId());

        LOG.debug(JSON.serialize(details));
    }

    @Test
    @Transactional
    public void refundDetailstoJSON(){
        List<Refund> refunds = refundService.find(new Criterion[0]);

        Refund refund = refunds.get(0);

        RefundDetails details = new RefundDetails();
        BeanUtil.copyProperties(details, refund);
        details.setOrderKey(OrderKey.newInstance(refund.getOrder().getType(), refund.getOrder().getSn()));
        details.setPayConfigId(refund.getPayConfig().getId());

        LOG.debug(JSON.serialize(details));
    }

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