package com.fantasy.wx.rest;

import com.fantasy.framework.spring.mvc.error.RestException;
import com.fantasy.payment.bean.Payment;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionFactory;
import com.fantasy.wx.service.PayService;
import com.fantasy.wx.service.vo.PrePayment;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(value = "weixin-pays", description = "微信支付")
@RestController("weixin.payController")
@RequestMapping("/weixin/accounts/{appid}/pay")
public class PayController {

    @Autowired
    private PayService payService;
    @Autowired
    private WeiXinSessionFactory weiXinSessionFactory;

    @ApiOperation(value = "微信支付通知", notes = "微信支付通知")
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    @ResponseBody
    public String notify(@PathVariable String appid, @RequestBody String body) throws IOException {
        return payService.notify(appid, body);
    }

    @ApiOperation(value = "创建微信预支付订单", notes = "创建微信预支付订单")
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    @ResponseBody
    public PrePayment create(@PathVariable("appid") String appid, @RequestBody PrePayment prePayment) throws IOException {
        return payService.preOrder(appid, prePayment);
    }

    @ApiOperation(value = "查询微信支付结果", notes = "查询微信支付结果")
    @RequestMapping(value = "/orders/{sn}", method = RequestMethod.GET)
    @ResponseBody
    public Payment query(@PathVariable("appid") String appid, @PathVariable("sn") String sn) throws IOException {
        try {
            weiXinSessionFactory.openSession(appid);
            return payService.query(appid, sn);
        } catch (WeiXinException e) {
            throw new RestException(e.getMessage());
        }

    }

}
