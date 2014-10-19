package com.fantasy.payment.product;

import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.system.util.SettingUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 支付宝（担保交易）
 */
public class AlipayPartner extends AbstractPaymentProduct {
    /**
     * 支付宝消息验证地址
     */
    private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";
    public static final String PAYMENT_URL = "https://mapi.alipay.com/gateway.do?_input_charset=UTF-8";// 支付请求URL
    public static final String RETURN_URL = "/payment/payreturn.do";// 回调处理URL
    public static final String NOTIFY_URL = "/payment/paynotify.do";// 消息通知URL
    public static final String SHOW_URL = "/payment.do";// 支付单回显url
    private static final Log logger = LogFactory.getLog(AlipayPartner.class);

    @Override
    public String getPaymentUrl() {
        return PAYMENT_URL;
    }

    @Override
    public String getPaymentSn(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return null;
        }
        String outTradeNo = httpServletRequest.getParameter("out_trade_no");
        if (StringUtils.isEmpty(outTradeNo)) {
            return null;
        }
        return outTradeNo;
    }

    @Override
    public BigDecimal getPaymentAmount(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return null;
        }
        String totalFee = httpServletRequest.getParameter("total_fee");
        if (StringUtils.isEmpty(totalFee)) {
            return null;
        }
        return new BigDecimal(totalFee);
    }

    public boolean isPaySuccess(HttpServletRequest httpServletRequest) {
        if (httpServletRequest == null) {
            return false;
        }
        String tradeStatus = httpServletRequest.getParameter("trade_status");
        return StringUtils.equals(tradeStatus, "WAIT_SELLER_SEND_GOODS");
    }

    @Override
    public Map<String, String> getParameterMap(PaymentConfig paymentConfig, String paymentSn, BigDecimal paymentAmount, HttpServletRequest httpServletRequest) {
        HttpServletResponse response = ServletActionContext.getResponse();
        String _input_charset = "UTF-8";// 字符集编码格式（UTF-8、GBK）
        String body = "";// 订单描述
        String logistics_fee = "0";// 物流费用
        String logistics_payment = "SELLER_PAY";// 物流支付方式（SELLER_PAY：卖家承担运费、BUYER_PAY：买家承担运费）
        String logistics_type = "EXPRESS";// 物流类型（EXPRESS：快递、POST：平邮、EMS：EMS）
        String notify_url = SettingUtil.getServerUrl() + response.encodeURL(NOTIFY_URL + "?sn=" + paymentSn);// 消息通知URL
        AtomicReference<String> out_trade_no = new AtomicReference<String>(paymentSn);// 支付编号
        String partner = paymentConfig.getBargainorId();// 合作身份者ID
        String payment_type = "1";// 支付类型（固定值：1）
        String price = String.format("%.2f", paymentAmount);// 总金额（单位：元）
        String quantity = "1";// 商品数量
        String return_url = SettingUtil.getServerUrl() + response.encodeURL(RETURN_URL + "?sn=" + paymentSn);// 回调处理URL
        String seller_id = paymentConfig.getSellerEmail();// 商家ID 如："shenzhenying@haoluesoft.com";
        String service = "create_partner_trade_by_buyer";// 接口类型（create_partner_trade_by_buyer：担保交易）
        String show_url = SettingUtil.getServerUrl() + response.encodeURL(SHOW_URL + "?sn=" + paymentSn);// 支付结果显示URL
        String sign_type = "MD5";//签名加密方式（MD5）
        AtomicReference<String> subject = new AtomicReference<String>(paymentSn);// 订单的名称、标题、关键字等
        String key = paymentConfig.getBargainorKey();// 密钥

        //TODO 担保支付时的收货人信息如何设置
        String receive_name = "昊略软件";//收货人姓名
        String receive_address = "上海市徐汇区田林路140号28号楼G09";//收货人地址
        String receive_zip = "200233";//收货人邮编
        String receive_phone = "0571-88158090";//收货人电话号码
        String receive_mobile = "15921884771";//收货人手机号码

        // 生成签名
        Map<String, String> signMap = new LinkedHashMap<String, String>();

        signMap.put("service", service);
        signMap.put("partner", partner);
        signMap.put("_input_charset", _input_charset);
        signMap.put("payment_type", payment_type);
        signMap.put("notify_url", notify_url);
        signMap.put("return_url", return_url);
        signMap.put("seller_email", seller_id);
        signMap.put("out_trade_no", out_trade_no.get());
        signMap.put("subject", subject.get());
        signMap.put("price", price);
        signMap.put("quantity", quantity);
        signMap.put("logistics_fee", logistics_fee);
        signMap.put("logistics_type", logistics_type);
        signMap.put("logistics_payment", logistics_payment);
        signMap.put("body", body);
        signMap.put("show_url", show_url);
        signMap.put("receive_name", receive_name);
        signMap.put("receive_address", receive_address);
        signMap.put("receive_zip", receive_zip);
        signMap.put("receive_phone", receive_phone);
        signMap.put("receive_mobile", receive_mobile);
        String sign = DigestUtils.md5Hex(getParameterString(signMap) + key);
        // 参数处理
        Map<String, String> parameterMap = new HashMap<String, String>(paraFilter(signMap));
        parameterMap.put("sign_type", sign_type);
        parameterMap.put("sign", sign);
        return parameterMap;
    }

    @Override
    public boolean verifySign(PaymentConfig paymentConfig, HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            valueStr = WebUtil.transformCoding(valueStr, "ISO-8859-1", "utf-8");
            params.put(name, valueStr);
        }
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号
//        String out_trade_no = WebUtil.transformCoding(request.getParameter("out_trade_no"), "ISO-8859-1", "UTF-8");
        //支付宝交易号
//        String trade_no = WebUtil.transformCoding(request.getParameter("trade_no"), "ISO-8859-1", "UTF-8");
        //交易状态
//        String trade_status = WebUtil.transformCoding(request.getParameter("trade_status"), "ISO-8859-1", "UTF-8");
        //移除回调链接中的 paymentSn sign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        params.remove("sn");
        return StringUtils.equals(params.get("sign"), DigestUtils.md5Hex(getParameterString(paraFilter(params)) + paymentConfig.getBargainorKey())) && verifyResponse(paymentConfig.getBargainorId(), params.get("notify_id"));
    }

    /**
     * 获取远程服务器ATN结果,验证返回URL
     *
     * @param notify_id 通知校验ID
     * @return 服务器ATN结果
     * 验证结果集：
     * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空
     * true 返回正确信息
     * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    private static boolean verifyResponse(String partner, String notify_id) {
        if (notify_id == null) {
            return true;
        }
        //获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
        try {
            URL url = new URL(HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notify_id);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            return "true".equalsIgnoreCase(in.readLine());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String getPayreturnMessage(String paymentSn) {
        return "<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /><title>页面跳转中..</title></head><body onload=\"javascript: document.forms[0].submit();\"><form action=\"" + SettingUtil.getServerUrl() + RESULT_URL + "\"><input type=\"hidden\" name=\"sn\" value=\"" + paymentSn + "\" /></form></body></html>";
    }

    @Override
    public String getPaynotifyMessage() {
        return "success";
    }

}