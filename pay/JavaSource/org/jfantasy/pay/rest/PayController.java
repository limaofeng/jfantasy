package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.rest.form.PayForm;
import org.jfantasy.pay.service.PayService;
import org.jfantasy.pay.service.vo.ToPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(value = "pay", description = "支付操作")
@RestController
@RequestMapping("/pays")
public class PayController {

    @Autowired
    private PayService payService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ToPayment execute(@RequestBody PayForm payForm) throws PayException {
        return payService.pay(payForm.getPayconfigId(),payForm.getPayType(),payForm.getOrderType(),payForm.getOrderSn(),payForm.getPayer());
        //payService.buildRequest(payForm.getOrderType(), payForm.getOrderSn(), payForm.getPayconfigId(), payForm.getPayer(), payForm.getParameters());
    }

    @ApiOperation("支付同步通知")
    @RequestMapping(value = "/{sn}/return", method = RequestMethod.POST)
    @ResponseBody
    public String payreturn(@PathVariable("sn") String sn, HttpServletRequest request) throws PayException {
        return null;//paymentService.payreturn(sn, WebUtil.getParameterMap(request));
    }

    @ApiOperation("支付异步通知")
    @RequestMapping(value = "/{sn}/notify", method = RequestMethod.POST)
    @ResponseBody
    public String paynotify(@PathVariable("sn") String sn, HttpServletRequest request) throws PayException {
        System.out.println(sn + "|" + request.getParameterMap().toString());
        return null;//paymentService.paynotify(sn, WebUtil.getParameterMap(request));
    }

}
