package org.jfantasy.pay.product;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.jfantasy.file.FileItem;
import org.jfantasy.file.bean.FileDetail;
import org.jfantasy.file.bean.FileDetailKey;
import org.jfantasy.file.service.FileManagerFactory;
import org.jfantasy.file.service.FileService;
import org.jfantasy.framework.httpclient.HttpClientUtil;
import org.jfantasy.framework.httpclient.Response;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.HandlebarsTemplateUtils;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.order.Order;
import org.jfantasy.pay.product.sign.SignUtil;
import org.jfantasy.pay.product.util.CertUtil;
import org.jfantasy.pay.product.util.SecureUtil;
import org.jfantasy.system.util.SettingUtil;

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
        }
    };

    @Override
    public String web(Payment payment, Order order, Properties properties) throws PayException {
        //支付配置
        PayConfig config = payment.getPayConfig();

        final Map<String, String> data = new TreeMap<String, String>();

        try {
            String merId = config.getBargainorId();//商户号
            KeyStore keyStore = CertUtil.loadKeyStore(loadFileItem(config.getSignCert()), config.getBargainorKey());//签名证书
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
                if (StringUtil.isNotBlank(properties.getProperty("backUrl"))) {
                    data.put("MerPageUrl", properties.getProperty("backUrl"));//同步通知
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
            MDC.put("type", "out");
            MDC.put("payType", "web");
            MDC.put("paymentSn", payment.getSn());
            MDC.put("payProductId", config.getPayProductId());
            MDC.put("payConfigId", config.getId());
            MDC.put("body", "curl -X POST --data \"" + SignUtil.coverMapString(data) + "\" " + urls.getFrontTransUrl());
            LOG.info(MDC.getContext());
        }
    }

    public String signature(Map<String, String> params, KeyStore keyStore, String certPwd) throws PayException {
        try {
            return SignUtil.encodeBase64(SecureUtil.sign(SignUtil.coverMapString(params, "Signature", "CertId").getBytes("UTF-8"), CertUtil.getCertPrivateKey(keyStore, certPwd), "SHA512WithRSA"), "UTF-8");
        } catch (Exception e) {
            throw new PayException("签名过程发生错误，错误信息为:" + e.getMessage());
        }
    }

    public boolean verify(Map<String, String> result, PublicKey publicKey) {
        try {
            return SecureUtil.verify(SignUtil.coverMapString(result, "Signature", "CertId").getBytes("UTF-8"), Base64.decodeBase64(result.get("Signature").getBytes("UTF-8")), publicKey, "SHA512WithRSA");
        } catch (Exception e) {
            LOG.error("签名过程发生错误，错误信息为:" + e.getMessage());
            return false;
        }
    }

    @Override
    public String wap() {
        return null;
    }

    @Override
    public String app(Payment payment, Order order) throws PayException {
        return null;
    }

    @Override
    public Payment payNotify(Payment payment, String result) throws PayException {
        Map<String, String> data = WebUtil.parseQuery(result, true);
        //支付配置
        PayConfig config = payment.getPayConfig();

        try {

            if (!verify(data, CertUtil.loadPublicKey(loadFileItem(config.getValidateCert())))) {
                throw new PayException("验证签名失败");
            }

            if ("0000".equals(data.get("OrderStatus"))) {
                payment.setStatus(Payment.Status.success);
            }

            payment.setTradeNo(data.get("AcqSeqId"));

            return payment;

        } finally {
            //记录支付通知日志
            MDC.put("type", "in");
            MDC.put("payType", "notify");
            MDC.put("paymentSn", payment.getSn());
            MDC.put("payProductId", config.getPayProductId());
            MDC.put("payConfigId", config.getId());
            MDC.put("body", result);
            LOG.info(MDC.getContext());
        }
    }

    public String query(Payment payment) {
        try {
            //支付配置
            PayConfig config = payment.getPayConfig();
            String merId = config.getBargainorId();//商户号
            //签名证书
            KeyStore keyStore = CertUtil.loadKeyStore(loadFileItem(config.getSignCert()), config.getBargainorKey());
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

            Map<String, String> result = WebUtil.parseQuery(response.getBody(), true);

            if (!verify(result, CertUtil.loadPublicKey(loadFileItem(config.getValidateCert())))) {
                throw new PayException("验证签名失败");
            }

            if (!"0000".equals(result.get("respCode"))) {
                throw new PayException(result.get("respMsg"));
            }

            if ("0000".equals(result.get("OrderStatus"))) {
                payment.setStatus(Payment.Status.success);
            }

            payment.setTradeNo(result.get("AcqSeqId"));

            return response.getBody();

        } catch (IOException e) {


            LOG.error(e.getMessage(), e);
            throw new PayException("支付过程发生错误，错误信息为:" + e.getMessage());
        }
    }

    public static FileItem loadFileItem(FileDetail fileDetail) {
        FileService fileService = SpringContextUtil.getBeanByType(FileService.class);
        assert fileService != null;
        FileDetail realFileDetail = fileService.get(FileDetailKey.newInstance(fileDetail.getAbsolutePath(), fileDetail.getFileManagerId()));
        return FileManagerFactory.getInstance().getFileManager(fileDetail.getFileManagerId()).getFileItem(realFileDetail.getRealPath());
    }

    class Urls {
        /**
         * 前台交易请求地址
         */
        private String frontTransUrl;
        /**
         * 后台交易请求地址
         */
        private String backTransUrl;
        /**
         *
         */
        private String queryTransUrl;

        public String getFrontTransUrl() {
            return frontTransUrl;
        }

        public void setFrontTransUrl(String frontTransUrl) {
            this.frontTransUrl = frontTransUrl;
        }

        public String getBackTransUrl() {
            return backTransUrl;
        }

        public void setBackTransUrl(String backTransUrl) {
            this.backTransUrl = backTransUrl;
        }

        public String getQueryTransUrl() {
            return queryTransUrl;
        }

        public void setQueryTransUrl(String queryTransUrl) {
            this.queryTransUrl = queryTransUrl;
        }
    }

}
