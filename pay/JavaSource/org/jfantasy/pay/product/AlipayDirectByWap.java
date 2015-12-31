package org.jfantasy.pay.product;

import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.system.util.SettingUtil;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.product.order.Order;
import org.jfantasy.pay.service.PaymentContext;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝 wap 支付接口
 */
public class AlipayDirectByWap extends AlipayPayProductSupport {

    //支付宝网关地址
    private static final String ALIPAY_GATEWAY_NEW = "http://wappaygw.alipay.com/service/rest.htm?_input_charset=" + input_charset;
    /*
    public static final String RETURN_URL = "/payment/payreturn.do";// 回调处理URL
    public static final String NOTIFY_URL = "/payment/paynotify.do";// 消息通知URL
    */

    public String getPaymentUrl() {
        return ALIPAY_GATEWAY_NEW;
    }

    public Map<String, String> getParameterMap(Parameters parameters) {
        PaymentContext context = PaymentContext.getContext();
        PayConfig paymentConfig = context.getPaymentConfig();
        Order orderDetails = context.getOrderDetails();
        Payment payment = context.getPayment();

        String returnUrl = context.getReturnUrl(payment.getSn());// 回调处理URL
        String notifyUrl = context.getNotifyUrl(payment.getSn());// 消息通知URL

        //操作中断返回地址
        String merchantUrl = SettingUtil.getServerUrl() + "/payment/merchant/" + payment.getSn();
        //用户付款中途退出返回商户的地址。需http://格式的完整路径，不允许加?id=123这类自定义参数

        String partner = paymentConfig.getBargainorId();// 合作身份者ID
        // 签名方式，选择项：0001(RSA)、MD5
        String signType = "MD5";
        // 无线的产品中，签名方式为rsa时，sign_type需赋值为0001而不是RSA

        //卖家支付宝帐户
        String sellerEmail = paymentConfig.getSellerEmail();
        //商户订单号
        String outTradeNo = orderDetails.getSN();
        //订单名称
        String subject = orderDetails.getSubject();
        //必填

        //付款金额
        DecimalFormat df = new DecimalFormat("0.00");
        String totalFee = df.format(orderDetails.getPayableFee());
        //必填

        //请求业务参数详细
        String reqDataToken = "<direct_trade_create_req><notify_url>" + notifyUrl + "</notify_url><call_back_url>" + returnUrl + "</call_back_url><seller_account_name>" + sellerEmail + "</seller_account_name><out_trade_no>" + outTradeNo + "</out_trade_no><subject>" + subject + "</subject><total_fee>" + totalFee + "</total_fee><merchant_url>" + merchantUrl + "</merchant_url></direct_trade_create_req>";
        //必填

        //返回格式
        String format = "xml";
        //必填，不需要修改
        //返回格式
        String v = "2.0";
        //必填，不需要修改

        //把请求参数打包成数组
        Map<String, String> sParaTempToken = new HashMap<String, String>();
        sParaTempToken.put("service", "alipay.wap.trade.create.direct");
        sParaTempToken.put("partner", partner);
        sParaTempToken.put("_input_charset", input_charset);
        sParaTempToken.put("sec_id", signType);
        sParaTempToken.put("format", format);
        sParaTempToken.put("v", v);
        sParaTempToken.put("req_id", payment.getSn());//请求号
        sParaTempToken.put("req_data", reqDataToken);
        try {
            //建立请求
            String sHtmlTextToken = buildRequest(ALIPAY_GATEWAY_NEW, "", "", sParaTempToken, paymentConfig);
            //URLDECODE返回的信息
            sHtmlTextToken = URLDecoder.decode(sHtmlTextToken, input_charset);
            //获取token
            String requestToken = getRequestToken(sHtmlTextToken, signType);
            ////////////////////////////////////根据授权码token调用交易接口alipay.wap.auth.authAndExecute//////////////////////////////////////
            //业务详细
            String reqData = "<auth_and_execute_req><request_token>" + requestToken + "</request_token></auth_and_execute_req>";
            //必填
            //把请求参数打包成数组
            Map<String, String> sParaTemp = new HashMap<String, String>();
            sParaTemp.put("service", "alipay.wap.auth.authAndExecute");
            sParaTemp.put("partner", partner);
            sParaTemp.put("_input_charset", input_charset);
            sParaTemp.put("sec_id", signType);
            sParaTemp.put("format", format);
            sParaTemp.put("v", v);
            sParaTemp.put("req_data", reqData);
            return buildRequestPara(sParaTemp, paymentConfig);
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
            return Collections.emptyMap();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    @Override
    public boolean verifySign(Map<String, String> parameters) {
        Map<String, String> params = parameters;
        try {
            String signType = parameters.get("sign_type");
            //RSA签名解密
            if ("0001".equals(signType)) {
                params = decrypt(parameters);
            }
            //XML解析notify_data数据
            Document docNotifyData = DocumentHelper.parseText(params.get("notify_data"));
            //商户订单号
            String outTradeNo = docNotifyData.selectSingleNode("//notify/out_trade_no").getText();
            //支付宝交易号
            String tradeNo = docNotifyData.selectSingleNode("//notify/trade_no").getText();
            //交易状态
            String tradeStatus = docNotifyData.selectSingleNode("//notify/trade_status").getText();
            //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
            return verifyNotify(params);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String buildRequest(Map<String, String> sParaTemp) {
        return super.buildRequest(sParaTemp, "get", "确定");
    }

    public PayResult parsePayResult(Map<String, String> parameters) {
        Map<String, String> params = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            params.put(entry.getKey(), WebUtil.transformCoding(entry.getValue(), "ISO-8859-1", "utf-8"));
        }
        try {
            String signType = parameters.get("sign_type");
            //RSA签名解密
            if ("0001".equals(signType)) {
                params = decrypt(parameters);
            }
            //XML解析notify_data数据
            Document docNotifyData = DocumentHelper.parseText(params.get("notify_data"));
            //商户订单号
            params.put("out_trade_no", docNotifyData.selectSingleNode("//notify/out_trade_no").getText());
            //支付宝交易号
            params.put("trade_no", docNotifyData.selectSingleNode("//notify/trade_no").getText());
            //交易状态
            params.put("trade_status", docNotifyData.selectSingleNode("//notify/trade_status").getText());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        PayResult payResult = new PayResult();
        payResult.setPaymentSN(params.get("out_trade_no"));//支付编号
        payResult.setTradeNo(params.get("trade_no"));//交易流水号
        payResult.setTotalFee(BigDecimal.valueOf(Double.valueOf(params.get("total_fee"))));//交易金额
        String tradeStatus = params.get("trade_status");
        payResult.setStatus((StringUtils.equals(tradeStatus, "TRADE_FINISHED") || StringUtils.equals(tradeStatus, "TRADE_SUCCESS")) ? PayResult.PayStatus.success : PayResult.PayStatus.failure);
        return payResult;
    }

}
