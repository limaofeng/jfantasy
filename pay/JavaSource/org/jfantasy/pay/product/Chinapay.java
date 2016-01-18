package org.jfantasy.pay.product;

import org.jfantasy.file.FileItem;
import org.jfantasy.file.bean.FileDetail;
import org.jfantasy.file.bean.FileDetailKey;
import org.jfantasy.file.service.FileManagerFactory;
import org.jfantasy.file.service.FileService;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.HandlebarsTemplateUtils;
import org.jfantasy.framework.util.common.DateUtil;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Chinapay extends PayProductSupport {

    private Urls urls = new Urls() {
        {
            this.setFrontTransUrl("https://payment.chinapay.com/CTITS/service/rest/page/nref/000000000017/0/0/0/0/0");
            this.setBackTransUrl("https://payment.chinapay.com/CTITS/service/rest/forward/sta/000000000017/0/0/0/0/0");
        }
    };

    @Override
    public String web(Order order, Payment payment) throws PayException {
        try {
            //支付配置
            PayConfig config = payment.getPayConfig();
            String merId = config.getBargainorId();//商户号
            //签名证书
            KeyStore keyStore = CertUtil.loadKeyStore(loadFileItem(config.getSignCert()), config.getBargainorKey());
            String certPwd = config.getBargainorKey();//签名证书密码
            //交易日期
            Date now = payment.getCreateTime();

            final Map<String, String> data = new TreeMap<String, String>();
            data.put("Version", "20140728");
            data.put("MerId", merId);//商户号
            data.put("MerOrderNo", payment.getSn());//商户订单号
            data.put("TranDate", DateUtil.format(now, "yyyyMMdd"));//商户交易日期
            data.put("TranTime", DateUtil.format(now, "HHmmss"));//商户交易日期
            data.put("OrderAmt", payment.getTotalAmount().multiply(BigDecimal.valueOf(100d)).intValue() + "");//商户交易金额
            data.put("BusiType", "0001");//业务类型
            data.put("MerBgUrl", SettingUtil.getServerUrl() + "/" + payment.getSn() + "/notify");
            data.put("Signature", signature(data, keyStore, certPwd));//签名

            return HandlebarsTemplateUtils.processTemplateIntoString(HandlebarsTemplateUtils.template("/org.jfantasy.pay.product.template/pay"), new HashMap<String, Object>() {
                {
                    this.put("url", urls.getFrontTransUrl());
                    this.put("params", data.entrySet());
                }
            });
        } catch (IOException e) {
            throw new PayException("支付过程发生错误，错误信息为:" + e.getMessage());
        }
    }

    public String signature(Map<String, String> params, KeyStore keyStore, String certPwd) throws PayException {
        try {
            return SignUtil.encodeBase64(SecureUtil.sign(SignUtil.coverMapString(params, "Signature", "CertId").getBytes("UTF-8"), CertUtil.getCertPrivateKey(keyStore, certPwd), "SHA512WithRSA"), "UTF-8");
        } catch (Exception e) {
            throw new PayException("签名过程发生错误，错误信息为:" + e.getMessage());
        }
    }

    @Override
    public String wap() {
        return null;
    }

    @Override
    public String app(Order order, Payment payment) throws PayException {
        return null;
    }

    @Override
    public String asyncNotify() {
        return null;
    }

    @Override
    public String syncNotify() {
        return null;
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
    }

}
