package com.fantasy.payment.product;

import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.order.OrderDetails;
import com.fantasy.payment.service.PaymentContext;
import com.fantasy.system.util.SettingUtil;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝 wap 支付接口
 */
public class AlipayDirectByWap extends AbstractAlipayPaymentProduct {

    //支付宝网关地址
    private static final String ALIPAY_GATEWAY_NEW = "http://wappaygw.alipay.com/service/rest.htm?_input_charset=" + input_charset;
    /*
    public static final String RETURN_URL = "/payment/payreturn.do";// 回调处理URL
    public static final String NOTIFY_URL = "/payment/paynotify.do";// 消息通知URL
    */

    @Override
    public String getPaymentUrl() {
        return ALIPAY_GATEWAY_NEW;
    }

    @Override
    public boolean isPaySuccess(Map<String, String> parameters) {
        if (parameters == null) {
            return false;
        }
        String tradeStatus = parameters.get("trade_status");
        return StringUtils.equals(tradeStatus, "TRADE_FINISHED") || StringUtils.equals(tradeStatus, "TRADE_SUCCESS");
    }

    @Override
    public Map<String, String> getParameterMap(Map<String, String> parameters) {
        PaymentContext context = PaymentContext.getContext();
        PaymentConfig paymentConfig = context.getPaymentConfig();
        OrderDetails orderDetails = context.getOrderDetails();
        Payment payment = context.getPayment();

        String return_url = context.getReturnUrl(payment.getSn());// 回调处理URL
        String notify_url = context.getNotifyUrl(payment.getSn());// 消息通知URL

        //操作中断返回地址
        String merchant_url = SettingUtil.getServerUrl() + "/payment/merchant/" + payment.getSn();
        //用户付款中途退出返回商户的地址。需http://格式的完整路径，不允许加?id=123这类自定义参数

        String partner = paymentConfig.getBargainorId();// 合作身份者ID
        // 签名方式，选择项：0001(RSA)、MD5
        String sign_type = "MD5";
        // 无线的产品中，签名方式为rsa时，sign_type需赋值为0001而不是RSA

        //卖家支付宝帐户
        String seller_email = paymentConfig.getSellerEmail();
        //商户订单号
        String out_trade_no = orderDetails.getSN();
        //订单名称
        String subject = orderDetails.getSubject();
        //必填

        //付款金额
        DecimalFormat df = new DecimalFormat("0.00");
        String total_fee = df.format(orderDetails.getPayableFee());
        //必填

        //请求业务参数详细
        String req_dataToken = "<direct_trade_create_req><notify_url>" + notify_url + "</notify_url><call_back_url>" + return_url + "</call_back_url><seller_account_name>" + seller_email + "</seller_account_name><out_trade_no>" + out_trade_no + "</out_trade_no><subject>" + subject + "</subject><total_fee>" + total_fee + "</total_fee><merchant_url>" + merchant_url + "</merchant_url></direct_trade_create_req>";
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
        sParaTempToken.put("sec_id", sign_type);
        sParaTempToken.put("format", format);
        sParaTempToken.put("v", v);
        sParaTempToken.put("req_id", payment.getSn());//请求号
        sParaTempToken.put("req_data", req_dataToken);
        try {
            //建立请求
            String sHtmlTextToken = buildRequest(ALIPAY_GATEWAY_NEW, "", "", sParaTempToken, paymentConfig);
            //URLDECODE返回的信息
            sHtmlTextToken = URLDecoder.decode(sHtmlTextToken, input_charset);
            //获取token
            String request_token = getRequestToken(sHtmlTextToken, sign_type);
            ////////////////////////////////////根据授权码token调用交易接口alipay.wap.auth.authAndExecute//////////////////////////////////////
            //业务详细
            String req_data = "<auth_and_execute_req><request_token>" + request_token + "</request_token></auth_and_execute_req>";
            //必填
            //把请求参数打包成数组
            Map<String, String> sParaTemp = new HashMap<String, String>();
            sParaTemp.put("service", "alipay.wap.auth.authAndExecute");
            sParaTemp.put("partner", partner);
            sParaTemp.put("_input_charset", input_charset);
            sParaTemp.put("sec_id", sign_type);
            sParaTemp.put("format", format);
            sParaTemp.put("v", v);
            sParaTemp.put("req_data", req_data);
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
            String sign_type = parameters.get("sign_type");
            //RSA签名解密
            if ("0001".equals(sign_type)) {
                params = decrypt(parameters);
            }
            //XML解析notify_data数据
            Document doc_notify_data = DocumentHelper.parseText(params.get("notify_data"));
            //商户订单号
            String out_trade_no = doc_notify_data.selectSingleNode("//notify/out_trade_no").getText();
            //支付宝交易号
            String trade_no = doc_notify_data.selectSingleNode("//notify/trade_no").getText();
            //交易状态
            String trade_status = doc_notify_data.selectSingleNode("//notify/trade_status").getText();
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

}
