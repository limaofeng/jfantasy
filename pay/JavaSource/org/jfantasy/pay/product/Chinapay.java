package org.jfantasy.pay.product;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.jfantasy.framework.httpclient.HttpClientUtil;
import org.jfantasy.framework.httpclient.Response;
import org.jfantasy.framework.util.HandlebarsTemplateUtils;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.order.entity.enums.RefundStatus;
import org.jfantasy.pay.product.sign.SignUtil;
import org.jfantasy.pay.product.util.CertUtil;
import org.jfantasy.pay.product.util.RAMFileProxy;
import org.jfantasy.pay.product.util.SecureUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class Chinapay extends PayProductSupport {

    private static final Logger LOG = Logger.getLogger(Chinapay.class);

    private Urls urls = new Urls() {
        {
            this.setFrontTransUrl("https://payment.chinapay.com/CTITS/service/rest/page/nref/000000000017/0/0/0/0/0");
            this.setBackTransUrl("https://payment.chinapay.com/CTITS/service/rest/forward/sta/000000000017/0/0/0/0/0");
            this.setQueryTransUrl("https://payment.chinapay.com/CTITS/service/rest/forward/syn/000000000060/0/0/0/0/0");
            //0401退款、0402退款撤销、0409退款重汇、9908通知分账的交易接收地址
            this.setAfterTransUrl("https://payment.chinapay.com/CTITS/service/rest/forward/syn/000000000065/0/0/0/0/0", "0401", "0402", "0409", "9908");
            //0403消费撤销、0203预授权撤销、0204预授权完成撤销、0202预授权完成的交易接收地址
            this.setAfterTransUrl("https://payment.chinapay.com/CTITS/service/rest/page/nref/000000000017/0/0/0/0/0", "0403", "0203", "0204", "0202");
        }
    };

    @Override
    public String web(Payment payment, Order order, Properties properties) throws PayException {
        //支付配置
        PayConfig config = payment.getPayConfig();

        final Map<String, String> data = new TreeMap<String, String>();

        try {
            String merId = config.getBargainorId();//商户号
            KeyStore keyStore = CertUtil.loadKeyStore(new RAMFileProxy(config, "signCert"), config.getBargainorKey());//签名证书
            String certPwd = config.getBargainorKey();//签名证书密码

            //常规参数
            data.put("Version", "20140728");
            data.put("MerId", merId);//商户号
            data.put("MerOrderNo", payment.getSn());//商户订单号
            data.put("TranDate", DateUtil.format(payment.getCreateTime(), "yyyyMMdd"));//商户交易日期
            data.put("TranTime", DateUtil.format(payment.getCreateTime(), "HHmmss"));//商户交易日期
            data.put("OrderAmt", payment.getTotalAmount().multiply(BigDecimal.valueOf(100d)).intValue() + "");//商户交易金额
            data.put("BusiType", "0001");//业务类型
            data.put("MerBgUrl", SettingUtil.getServerUrl() + "/pays/" + payment.getSn() + "/notify");

            //额外参数
            if (properties != null) {
                if (StringUtil.isNotBlank(properties.getProperty(PROPERTIES_BACKURL))) {
                    data.put("MerPageUrl", properties.getProperty(PROPERTIES_BACKURL));//同步通知
                    LOG.debug("添加参数 MerPageUrl = " + data.get("MerPageUrl"));
                }
            }

            //签名
            data.put("Signature", signature(data, keyStore, certPwd));

            //拼接支付表单
            return HandlebarsTemplateUtils.processTemplateIntoString(HandlebarsTemplateUtils.template("/org.jfantasy.pay.product.template/pay"), new HashMap<String, Object>() {
                {
                    this.put("url", urls.getFrontTransUrl());
                    this.put("params", data.entrySet());
                }
            });
        } catch (IOException e) {
            throw new PayException("支付过程发生错误，错误信息为:" + e.getMessage());
        } finally {
            //记录支付日志
            this.log("out", "web", payment, config, "curl -X POST --data \"" + SignUtil.coverMapString(data) + "\" " + urls.getFrontTransUrl());
        }
    }

    private String signature(Map<String, String> params, KeyStore keyStore, String certPwd) throws PayException {
        try {
            return SignUtil.encodeBase64(SecureUtil.sign(SignUtil.coverMapString(params, "Signature", "CertId").getBytes("UTF-8"), CertUtil.getCertPrivateKey(keyStore, certPwd), "SHA512WithRSA"), "UTF-8");
        } catch (Exception e) {
            throw new PayException("签名过程发生错误，错误信息为:" + e.getMessage());
        }
    }

    private boolean verify(Map<String, String> result, PublicKey publicKey) {
        try {
            return SecureUtil.verify(SignUtil.coverMapString(result, "Signature", "CertId").getBytes("UTF-8"), Base64.decodeBase64(result.get("Signature").getBytes("UTF-8")), publicKey, "SHA512WithRSA");
        } catch (Exception e) {
            LOG.error("签名过程发生错误，错误信息为:" + e.getMessage());
            return false;
        }
    }

    @Override
    public Object payNotify(Payment payment, String result) throws PayException {
        Map<String, String> data = SignUtil.parseQuery(result, true);
        //支付配置
        PayConfig config = payment.getPayConfig();

        try {
            if (!verify(data, CertUtil.loadPublicKey(new RAMFileProxy(config, "validateCert")))) {
                throw new PayException("验证签名失败");
            }

            if ("0000".equals(data.get("OrderStatus"))) {
                payment.setStatus(PaymentStatus.success);
            }

            payment.setTradeNo(data.get("AcqSeqId"));
            payment.setTradeTime(DateUtil.now());
            return null;

        } finally {//记录支付通知日志
            this.log("in", "notify", payment, config, result);
        }
    }

    @Override
    public Object payNotify(Refund refund, String result) throws PayException {
        Map<String, String> data = SignUtil.parseQuery(result, true);
        //支付配置
        PayConfig config = refund.getPayConfig();

        try {
            if (!verify(data, CertUtil.loadPublicKey(new RAMFileProxy(config, "validateCert")))) {
                throw new PayException("验证签名失败");
            }

            if (!data.get("RefundAmt").equals(refund.getTotalAmount().multiply(BigDecimal.valueOf(100d)).intValue() + "")) {
                throw new PayException("交易金额不匹配");
            }

            refund.setTradeNo(data.get("AcqSeqId"));

            return null;

        } finally {//记录退款通知日志
            this.log("in", "notify", refund, config, result);
        }
    }

    public PaymentStatus query(Payment payment) {
        try {
            //支付配置
            PayConfig config = payment.getPayConfig();
            String merId = config.getBargainorId();//商户号
            //签名证书
            KeyStore keyStore = CertUtil.loadKeyStore(new RAMFileProxy(config, "signCert"), config.getBargainorKey());
            String certPwd = config.getBargainorKey();//签名证书密码

            final Map<String, String> data = new TreeMap<String, String>();
            data.put("Version", "20140728");
            data.put("MerId", merId);//商户号
            data.put("MerOrderNo", payment.getSn());//商户订单号
            data.put("TranDate", DateUtil.format(payment.getCreateTime(), "yyyyMMdd"));//商户交易日期
            data.put("TranTime", DateUtil.format(payment.getCreateTime(), "HHmmss"));//商户交易日期
            data.put("TranType", "0502");//交易类型 TODO 发起交易时非必填,为什么查询交易时必填
            data.put("BusiType", "0001");//业务类型

            data.put("Signature", signature(data, keyStore, certPwd));//签名

            Response response = HttpClientUtil.doPost(urls.getQueryTransUrl(), data);

            Map<String, String> result = SignUtil.parseQuery(response.text(), true);

            if (!verify(result, CertUtil.loadPublicKey(new RAMFileProxy(config, "validateCert")))) {
                throw new PayException("验证签名失败");
            }

            if (!"0000".equals(result.get("respCode"))) {
                throw new PayException(result.get("respMsg"));
            }

            if ("0000".equals(result.get("OrderStatus"))) {
                payment.setStatus(PaymentStatus.success);
            }

            payment.setTradeNo(result.get("AcqSeqId"));
            payment.setTradeTime(DateUtil.now());
            return null;//response.text();

        } catch (IOException e) {


            LOG.error(e.getMessage(), e);
            throw new PayException("支付过程发生错误，错误信息为:" + e.getMessage());
        }
    }

    @Override
    public void close(Payment payment) throws PayException {

    }

    public String refund(Refund refund) {
        String url = urls.getAfterTransUrl("0401");
        Payment payment = refund.getPayment();
        try {
            //支付配置
            PayConfig config = refund.getPayConfig();
            String merId = config.getBargainorId();//商户号
            //签名证书
            KeyStore keyStore = CertUtil.loadKeyStore(new RAMFileProxy(config, "signCert"), config.getBargainorKey());
            String certPwd = config.getBargainorKey();//签名证书密码

            final Map<String, String> data = new TreeMap<String, String>();
            data.put("Version", "20140728");
            data.put("MerId", merId);//商户号
            data.put("MerOrderNo", refund.getSn());//商户订单号
            data.put("TranDate", DateUtil.format(refund.getCreateTime(), "yyyyMMdd"));//商户交易日期
            data.put("TranTime", DateUtil.format(refund.getCreateTime(), "HHmmss"));//商户交易日期
            //原付款交易
            data.put("OriOrderNo", payment.getSn());
            data.put("OriTranDate", DateUtil.format(payment.getCreateTime(), "yyyyMMdd"));
            //退款金额
            data.put("RefundAmt", refund.getTotalAmount().multiply(BigDecimal.valueOf(100d)).intValue() + "");

            data.put("TranType", "0401");//交易类型
            data.put("BusiType", "0001");//业务类型
            data.put("MerBgUrl", SettingUtil.getServerUrl() + "/pays/" + refund.getSn() + "/notify");

            data.put("Signature", signature(data, keyStore, certPwd));//签名

            MDC.put("body", "curl -X POST --data \"" + SignUtil.coverMapString(data) + "\" " + url);
            LOG.info(MDC.getContext());

            Response response = HttpClientUtil.doPost(url, data);

            Map<String, String> result = SignUtil.parseQuery(response.text(), true);

            if (!verify(result, CertUtil.loadPublicKey(new RAMFileProxy(config, "validateCert")))) {
                throw new PayException("验证签名失败");
            }

            if ("1003".equals(result.get("respCode"))) {
                refund.setStatus(RefundStatus.wait);
            } else {
                refund.setStatus(RefundStatus.failure);
            }

            MDC.put("body", response.getBody());
            LOG.info(MDC.getContext());

            return null;

        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new PayException("支付过程发生错误，错误信息为:" + e.getMessage());
        }
    }

    private class Urls {
        /**
         * 前台交易请求地址
         */
        private String frontTransUrl;
        /**
         * 后台交易请求地址
         */
        private String backTransUrl;
        /**
         * 交易查询接口
         */
        private String queryTransUrl;
        /**
         * 后续接口
         */
        private Map<String, String> afterTransUrls = new HashMap<String, String>();

        String getFrontTransUrl() {
            return frontTransUrl;
        }

        void setFrontTransUrl(String frontTransUrl) {
            this.frontTransUrl = frontTransUrl;
        }

        String getBackTransUrl() {
            return backTransUrl;
        }

        void setBackTransUrl(String backTransUrl) {
            this.backTransUrl = backTransUrl;
        }

        String getQueryTransUrl() {
            return queryTransUrl;
        }

        void setQueryTransUrl(String queryTransUrl) {
            this.queryTransUrl = queryTransUrl;
        }

        String getAfterTransUrl(String tranType) {
            return this.afterTransUrls.get(tranType);
        }

        void setAfterTransUrl(String url, String... tranTypes) {
            for (String tranType : tranTypes) {
                this.afterTransUrls.put(tranType, url);
            }
        }
    }

}
