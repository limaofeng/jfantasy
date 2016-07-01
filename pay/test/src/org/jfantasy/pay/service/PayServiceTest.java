package org.jfantasy.pay.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.pay.PayApplicationTest;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.order.entity.OrderKey;
import org.jfantasy.pay.order.entity.PaymentDetails;
import org.jfantasy.pay.order.entity.RefundDetails;
import org.jfantasy.pay.product.PayType;
import org.jfantasy.pay.service.vo.ToPayment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(PayApplicationTest.class)
public class PayServiceTest {

    private final static Log LOG = LogFactory.getLog(PayServiceTest.class);

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RefundService refundService;
    @Autowired
    private PayConfigService payConfigService;
    @Autowired
    private PayService payService;

    @Test
    @Transactional
    public void paymentDetailstoJSON() {
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
    public void refundDetailstoJSON() {
        List<Refund> refunds = refundService.find(new Criterion[0]);

        Refund refund = refunds.get(0);

        RefundDetails details = new RefundDetails();
        BeanUtil.copyProperties(details, refund);
        details.setOrderKey(OrderKey.newInstance(refund.getOrder().getType(), refund.getOrder().getSn()));
        details.setPayConfigId(refund.getPayConfig().getId());

        LOG.debug(JSON.serialize(details));
    }

    @Test
    public void pay() throws Exception {
        PayConfig payConfig = payConfigService.findUnique(Restrictions.eq("payProductId", "alipay"));
        ToPayment topayment = payService.pay(payConfig.getId(), PayType.web, "test:2016061300001", "limaofeng", new Properties());

        Payment payment = paymentService.get(topayment.getSn());

        System.out.println(payment);

    }

}