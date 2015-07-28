package com.fantasy.payment.rest;

import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.error.PaymentException;
import com.fantasy.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @RequestMapping(value = "/{sn}")
    public Payment view(@PathVariable String sn) throws Exception {
        return paymentService.get(sn);
    }

    /**
     * 支付提交
     *
     * @param ordertype       订单类型
     * @param ordersn         订单编号
     * @param paymentConfigId 支付方式
     * @return {string}
     */
    @RequestMapping(method = RequestMethod.POST)
    public String submit(@RequestParam String ordertype, @RequestParam String ordersn, Long paymentConfigId, HttpServletRequest request) throws IOException, PaymentException {
        return paymentService.buildRequest(ordertype, ordersn, paymentConfigId, WebUtil.getParameterMap(request));
    }

    // 支付结果
    public String payreturn(String sn, HttpServletRequest request) throws IOException, PaymentException {
        return paymentService.payreturn(sn, WebUtil.getParameterMap(request));
    }

    //支付异步通知
    public String paynotify(String sn, HttpServletRequest request) throws IOException, PaymentException {
        return paymentService.paynotify(sn, WebUtil.getParameterMap(request));
    }

}
