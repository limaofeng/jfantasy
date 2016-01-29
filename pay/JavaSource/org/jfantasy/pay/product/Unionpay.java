package org.jfantasy.pay.product;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.jfantasy.file.FileItem;
import org.jfantasy.file.bean.FileDetail;
import org.jfantasy.file.bean.FileDetailKey;
import org.jfantasy.file.service.FileManagerFactory;
import org.jfantasy.file.service.FileService;
import org.jfantasy.framework.httpclient.HttpClientUtil;
import org.jfantasy.framework.httpclient.Response;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.jackson.JSON;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.order.Order;
import org.jfantasy.pay.product.sign.SignUtil;
import org.jfantasy.pay.product.util.CertUtil;
import org.jfantasy.pay.product.util.SecureUtil;
import org.jfantasy.system.util.SettingUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class Unionpay extends PayProductSupport {

    private final static Log LOG = LogFactory.getLog(Unionpay.class);

    private DeployStatus deployStatus;
    private Map<DeployStatus, Unionpay.Urls> urlsMap;

    public Unionpay() {
        this.deployStatus = DeployStatus.Production;
        this.urlsMap = new HashMap<DeployStatus, Unionpay.Urls>() {
            {
                Unionpay.Urls urls = Unionpay.this.new Urls();
                urls.setFrontTransUrl("https://101.231.204.80:5000/gateway/api/frontTransReq.do");
                urls.setAppTransUrl("https://101.231.204.80:5000/gateway/api/appTransReq.do");
                urls.setBackTransUrl("https://101.231.204.80:5000/gateway/api/backTrans.do");
                urls.setCardTransUrl("https://101.231.204.80:5000/gateway/api/cardTransReq.do");
                urls.setSingleQueryUrl("https://101.231.204.80:5000/gateway/api/queryTrans.do");
                urls.setBatchTransUrl("https://101.231.204.80:5000/gateway/api/batchTransReq.do");
                urls.setFileTransUrl("https://101.231.204.80:9080/");
                urls.setQueryTransUrl("https://101.231.204.80:5000/gateway/api/queryTrans.do");
                this.put(DeployStatus.Develop, urls);
                urls = Unionpay.this.new Urls();
                urls.setFrontTransUrl("https://gateway.95516.com/gateway/api/frontTransReq.do");
                urls.setAppTransUrl("https://gateway.95516.com/gateway/api/appTransReq.do");
                urls.setBackTransUrl("https://gateway.95516.com/gateway/api/backTransReq.do");
                urls.setCardTransUrl("https://gateway.95516.com/gateway/api/cardTransReq.do");
                urls.setSingleQueryUrl("https://gateway.95516.com/gateway/api/queryTrans.do");
                urls.setBatchTransUrl("https://gateway.95516.com/gateway/api/batchTrans.do");
                urls.setFileTransUrl("https://filedownload.95516.com/");
                urls.setQueryTransUrl("https://gateway.95516.com/gateway/api/queryTrans.do");
                this.put(DeployStatus.Production, urls);
            }
        };
    }

    private Urls getUrls() {
        return urlsMap.get(this.deployStatus);
    }

    public static String encoding = "UTF-8";

    public static String version = "5.0.0";

    @Override
    public String web(Payment payment, Order order, Properties properties) {
        return null;
    }

    @Override
    public String wap() {
        return null;
    }

    @Override
    public Object app(Payment payment, Order order) throws PayException {
        //支付配置
        PayConfig config = payment.getPayConfig();
        //准备数据
        Map<String, String> data = new HashMap<String, String>();
        //请求地址集
        Urls urls = this.getUrls();

        try {
            String merId = config.getBargainorId();//商户号
            KeyStore keyStore = CertUtil.loadKeyStore(loadFileItem(config.getSignCert()), config.getBargainorKey());
            String certPwd = config.getBargainorKey();//签名证书密码

            /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
            data.put("version", version);//版本号 全渠道默认值
            data.put("encoding", encoding);//字符集编码 可以使用UTF-8,GBK两种方式
            data.put("signMethod", "01");//签名方法 目前只支持01：RSA方式证书加密
            data.put("txnType", "01");//交易类型 01:消费
            data.put("txnSubType", "01");//交易子类 01：消费
            data.put("bizType", "000201");//填写000201
            data.put("channelType", "08");//渠道类型 08手机

            /***商户接入参数***/
            data.put("merId", merId);//商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
            data.put("accessType", "0");//接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
            data.put("orderId", payment.getSn());//商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
            data.put("txnTime", DateUtil.format(payment.getCreateTime(), "yyyyMMddHHmmss"));//订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
            data.put("accType", "01");//账号类型 01：银行卡02：存折03：IC卡帐号类型(卡介质)
            data.put("txnAmt", payment.getTotalAmount().multiply(BigDecimal.valueOf(100d)).intValue() + "");//交易金额 单位为分，不能带小数点
            data.put("currencyCode", "156");//境内商户固定 156 人民币

            //后台通知地址
            data.put("backUrl", SettingUtil.getServerUrl() + "/pays/" + payment.getSn() + "/notify");

            //添加签名
            data.put("certId", CertUtil.getCertId(keyStore));
            data.put("signature", signature(data, keyStore, certPwd));

            //发送请求
            LOG.debug("url ==> " + urls.getAppTransUrl() + System.getProperty("line.separator") + "data:" + data);
            Response response = HttpClientUtil.doPost(urls.getAppTransUrl(), data);
            if (response.getStatusCode() != 200) {
                throw new IOException("请求失败:" + response.getStatusCode() + "\t" + response.getBody());
            }

            //验签
            Map<String, String> result = WebUtil.parseQuery(response.getBody(), true);// 将返回结果转换为map

            if (!verify(result, CertUtil.loadPublicKey(loadFileItem(config.getValidateCert())))) {//验证签名
                throw new PayException("验证签名失败");
            }
            payment.setTradeNo(result.get("tn"));
            return result;
        } catch (IOException e) {
            throw new PayException(e.getMessage());
        }
    }

    @Override
    public Payment payNotify(Payment payment, String result) throws PayException {
        //支付配置
        PayConfig config = payment.getPayConfig();
        PublicKey publicKey = CertUtil.loadPublicKey(loadFileItem(config.getValidateCert()));

        try {
            Map<String, String> data = (Map<String, String>) (result.startsWith("{") && result.endsWith("}") ? JSON.deserialize(result) : WebUtil.parseQuery(result, true));
            //手机支付通知
            boolean isAppNotify = data.containsKey("sign");
            if (!(isAppNotify && verify(data.get("data"), data.get("sign"), publicKey) || verify(data, publicKey))) {//验证签名
                throw new PayException("验证签名失败");
            }
            if (isAppNotify) {
                Map<String, String> payresult = WebUtil.parseQuery(data.get("data"), true);
                if (!StringUtil.nullValue(payresult.get("tn")).equals(payment.getTradeNo())) {
                    throw new PayException("通知与订单不匹配");
                }
                payment.setStatus("success".equals(payresult.get("pay_result")) ? Payment.Status.success : Payment.Status.failure);
            } else {
                System.out.println(result);
                payment.setStatus(Payment.Status.success);
            }
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
        return payment;
    }

    @Override
    public Refund payNotify(Refund refund, String result) throws PayException {
        return null;
    }

    public String query(Payment payment) {
        //请求地址集
        Urls urls = this.getUrls();
        try {
            //支付配置
            PayConfig config = payment.getPayConfig();
            String merId = config.getBargainorId();//商户号
            //签名证书
            KeyStore keyStore = CertUtil.loadKeyStore(loadFileItem(config.getSignCert()), config.getBargainorKey());
            String certPwd = config.getBargainorKey();//签名证书密码

            final Map<String, String> data = new TreeMap<String, String>();

            // 固定填写
            data.put("version", version);// M
            // 默认取值：UTF-8
            data.put("encoding", encoding);// M
            data.put("signMethod", "01");// M
            data.put("txnType", "00");// 交易类型 00
            data.put("txnSubType", "00");//默认00
            data.put("bizType", "000000");// 默认:000000
            data.put("accessType", "0");// 0：普通商户直连接入2：平台类商户接入
            data.put("merId", merId);// M
            data.put("txnTime", DateUtil.format(payment.getCreateTime(), "yyyyMMddHHmmss"));// 被查询交易的交易时间
            data.put("orderId", payment.getSn());// 被查询交易的订单号
            //data.put("queryId", ""); 待查询交易的流水号
            //data.put("reserved", ""); 格式如下：{子域名1=值&子域名2=值&子域名3=值} 子域： origTxnType N2原交易类型余额查询时必送

            data.put("Signature", signature(data, keyStore, certPwd));//签名

            Response response = HttpClientUtil.doPost(urls.getQueryTransUrl(), data);

            Map<String, String> result = WebUtil.parseQuery(response.getBody(), true);

            if (!verify(result, CertUtil.loadPublicKey(loadFileItem(config.getValidateCert())))) {
                throw new PayException("验证签名失败");
            }

            return response.getBody();

        } catch (IOException e) {


            LOG.error(e.getMessage(), e);
            throw new PayException("支付过程发生错误，错误信息为:" + e.getMessage());
        }
    }

    public void setDeployStatus(DeployStatus deployStatus) {
        this.deployStatus = deployStatus;
    }

    class Urls {
        /**
         * 前台交易请求地址
         */
        private String frontTransUrl;
        /**
         * app 交易请求地址
         */
        private String appTransUrl;
        /**
         * 后台交易请求地址
         */
        private String backTransUrl;
        /**
         * 单笔查询请求地址
         */
        private String singleQueryUrl;
        /**
         * 批量交易请求地址
         */
        private String batchTransUrl;
        /**
         * 文件传输类交易地址
         */
        private String fileTransUrl;
        /**
         * 后台交易请求地址(若为有卡交易配置该地址)
         */
        private String cardTransUrl;
        /**
         * 交易状态查询交易
         */
        private String queryTransUrl;

        public String getFrontTransUrl() {
            return frontTransUrl;
        }

        public void setFrontTransUrl(String frontTransUrl) {
            this.frontTransUrl = frontTransUrl;
        }

        public String getAppTransUrl() {
            return appTransUrl;
        }

        public void setAppTransUrl(String appTransUrl) {
            this.appTransUrl = appTransUrl;
        }

        public String getBackTransUrl() {
            return backTransUrl;
        }

        public void setBackTransUrl(String backTransUrl) {
            this.backTransUrl = backTransUrl;
        }

        public String getSingleQueryUrl() {
            return singleQueryUrl;
        }

        public void setSingleQueryUrl(String singleQueryUrl) {
            this.singleQueryUrl = singleQueryUrl;
        }

        public String getBatchTransUrl() {
            return batchTransUrl;
        }

        public void setBatchTransUrl(String batchTransUrl) {
            this.batchTransUrl = batchTransUrl;
        }

        public String getFileTransUrl() {
            return fileTransUrl;
        }

        public String getCardTransUrl() {
            return cardTransUrl;
        }

        public void setCardTransUrl(String cardTransUrl) {
            this.cardTransUrl = cardTransUrl;
        }

        public String getQueryTransUrl() {
            return queryTransUrl;
        }

        public void setQueryTransUrl(String queryTransUrl) {
            this.queryTransUrl = queryTransUrl;
        }

        public void setFileTransUrl(String fileTransUrl) {
            this.fileTransUrl = fileTransUrl;
        }
    }

    public static FileItem loadFileItem(FileDetail fileDetail) {
        FileService fileService = SpringContextUtil.getBeanByType(FileService.class);
        assert fileService != null;
        FileDetail realFileDetail = fileService.get(FileDetailKey.newInstance(fileDetail.getAbsolutePath(), fileDetail.getFileManagerId()));
        return FileManagerFactory.getInstance().getFileManager(fileDetail.getFileManagerId()).getFileItem(realFileDetail.getRealPath());
    }

    public String signature(Map<String, String> params, KeyStore keyStore, String certPwd) {
        try {
            byte[] e = SecureUtil.sha1X16(SignUtil.coverMapString(params), params.get("encoding"));
            byte[] byteSign1 = SecureUtil.base64Encode(SecureUtil.signBySoft(CertUtil.getCertPrivateKey(keyStore, certPwd), e));
            return new String(byteSign1);
        } catch (Exception var6) {
            LOG.error("签名异常", var6);
            throw new RuntimeException(var6);
        }
    }

    /**
     * 验签逻辑
     *
     * @param result    返回数据
     * @param publicKey 验签证书
     * @return boolean
     */
    protected boolean verify(Map<String, String> result, PublicKey publicKey) {
        String stringSign = result.get("signature");
        String stringData = SignUtil.coverMapString(result, "signature");
        String encoding = result.get("encoding");
        try {
            return SecureUtil.validateSignBySoft(publicKey, SecureUtil.base64Decode(stringSign.getBytes(encoding)), SecureUtil.sha1X16(stringData, encoding));
        } catch (UnsupportedEncodingException var6) {
            LOG.error(var6.getMessage(), var6);
        } catch (Exception var7) {
            LOG.error(var7.getMessage(), var7);
        }
        return false;
    }

    protected boolean verify(String data, String signature, PublicKey publicKey) {
        try {
            return SecureUtil.validateSignBySoft(publicKey, SecureUtil.base64Decode(signature.getBytes(encoding)), SecureUtil.sha1X16(data, encoding));
        } catch (UnsupportedEncodingException var6) {
            LOG.error(var6.getMessage(), var6);
        } catch (Exception var7) {
            LOG.error(var7.getMessage(), var7);
        }
        return false;
    }


}
