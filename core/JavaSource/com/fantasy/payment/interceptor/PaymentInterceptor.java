package com.fantasy.payment.interceptor;

import com.fantasy.payment.service.PaymentConfiguration;
import com.fantasy.payment.service.PaymentService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 支付成功拦截器
 */
@Component
@Aspect
public class PaymentInterceptor {

    @Resource
    private PaymentConfiguration paymentConfiguration;
    @Resource
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
