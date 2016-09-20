package org.jfantasy.pay.rest;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.product.PayType;
import org.jfantasy.pay.product.Weixinpay;
import org.jfantasy.pay.service.PayConfigService;
import org.jfantasy.pay.service.PayService;
import org.jfantasy.pay.service.TransactionService;
import org.jfantasy.pay.service.vo.ToPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/** 微信支付接口 **/
@RestController
@RequestMapping("/weixinpay/{appid}")
public class WeixinPayController {

    private final PayConfigService payConfigService;
    private final PayService payService;
    private final TransactionService transactionService;

    @Autowired
    public WeixinPayController(PayService payService, PayConfigService payConfigService, TransactionService transactionService) {
        this.payService = payService;
        this.payConfigService = payConfigService;
        this.transactionService = transactionService;
    }

    /**
     * 生成扫码支付的二维码
     */
    /** 微信扫码支付二维码 **/
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    @ResponseBody
    public void qrcode(@PathVariable("appid") String appid, @RequestParam("tid") String transactionId, @RequestParam(value = "model", required = false) String model, HttpServletResponse response) throws WriterException, IOException {
        PayConfig config = payConfigService.findByWeixin(appid);
        if (config == null) {
            throw new RestException("该微信号未配置支付接口");
        }

        model = StringUtil.defaultValue(model, "1");

        String url;
        if ("1".equals(model)) {
            Map<String, String> data = new TreeMap<>();
            data.put("appid", appid);
            data.put("mch_id", config.getBargainorId());
            data.put("nonce_str", Weixinpay.generateNonceString(32));
            data.put("time_stamp", DateUtil.now().getTime() / 1000 + "");
            data.put("product_id", transactionId);

            data.put("sign", Weixinpay.sign(data, config.getBargainorKey()));

            url = "weixin://wxpay/bizpayurl?" + WebUtil.getQueryString(data);
        } else {
            Properties prop = new Properties();
            prop.put("trade_type", "NATIVE");
            prop.put("product_id", transactionId);

            ToPayment payment = payService.pay(transactionService.get(transactionId), config.getId(), PayType.web, "", prop);

            url = (String) payment.getSource();
        }

        response.setContentType("image/png");
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 300, 300, hints);
        MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
    }

    /** 微信扫码回调接口,需要在微信平台中配置该接口 **/
    @RequestMapping(value = "/paycallback", method = RequestMethod.POST)
    @ResponseBody
    public String callback(@PathVariable("appid") String appid, @RequestBody String body) {
        PayConfig config = payConfigService.findByWeixin(appid);
        if (config == null) {
            throw new RestException("该微信号未配置支付接口");
        }

        Map<String, String> data = Weixinpay.xmlToMap(body);

        if (!Weixinpay.verify(data, config.getBargainorKey())) {
            throw new RestException("签名错误");
        }

        Properties prop = new Properties();
        prop.put("openid", data.get("openid"));
        prop.put("trade_type", "NATIVE");
        prop.put("product_id", data.get("product_id"));
        String tid = data.get("product_id");
        ToPayment payment = payService.pay(transactionService.get(tid), config.getId(), PayType.web, "", prop);

        return (String) payment.getSource();
    }


}
