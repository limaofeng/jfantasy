package com.fantasy.payment.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.error.PaymentException;
import com.fantasy.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/system/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /*
    @RequestMapping(value = "/{sn}")
    public Payment view(@PathVariable String sn) throws Exception {
        return paymentService.get(sn);
    }*/

    /**
     * 支付提交
     *
     * @param ordertype       订单类型
     * @param ordersn         订单编号
     * @param paymentConfigId 支付方式
     * @return {string}
     * @RequestMapping(method = RequestMethod.POST)
     * @ResponseStatus(value = HttpStatus.CREATED)
     * public String submit(@RequestParam String ordertype, @RequestParam String ordersn, Long paymentConfigId, HttpServletRequest request) throws IOException, PaymentException {
     * return paymentService.buildRequest(ordertype, ordersn, paymentConfigId, WebUtil.getParameterMap(request));
     * }
     */

    /*
    // 支付结果
    public String payreturn(String sn, HttpServletRequest request) throws IOException, PaymentException {
        return paymentService.payreturn(sn, WebUtil.getParameterMap(request));
    }

    //支付异步通知
    public String paynotify(String sn, HttpServletRequest request) throws IOException, PaymentException {
        return paymentService.paynotify(sn, WebUtil.getParameterMap(request));
    }*/
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Payment> search(Pager<Payment> pager, List<PropertyFilter> filters) {
        return paymentService.findPager(pager, filters);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Payment view(@PathVariable("id") Long id) {
        return this.paymentService.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.paymentService.delete(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Long... ids) {
        this.paymentService.delete(ids);
    }

}
