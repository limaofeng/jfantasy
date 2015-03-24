package com.fantasy.payment.interceptor;

import com.fantasy.payment.service.PaymentConfiguration;
import com.fantasy.payment.service.PaymentService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 支付成功拦截器
 */
@Component
@Aspect
public class PaymentInterceptor {

    @Autowired
    private PaymentConfiguration paymentConfiguration;
    @Autowired
    private PaymentService paymentService;


    @After("execution(public * com.fantasy.payment.service.PaymentService.ready(..))")
    public void ready(JoinPoint point) {
//        Payment payment = (Payment) point.getArgs()[0];
//        paymentConfiguration.getPaymentOrderService(payment.getOrderType()).ready(payment);
    }

    @After("execution(public * com.fantasy.payment.service.PaymentService.success(..))")
    public void success(JoinPoint point) {
//        Payment payment = paymentService.get((String) point.getArgs()[0]);
//        paymentConfiguration.getPaymentOrderService(payment.getOrderType()).success(payment);
    }

}
