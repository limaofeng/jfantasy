package org.jfantasy.pay.product;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.httpclient.HttpClientUtil;
import org.jfantasy.framework.httpclient.Response;
import org.jfantasy.framework.jackson.JSON;
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
    public Object app(Payment payment, Order order, Properties properties) throws PayException {
        //支付配置
        PayConfig config = payment.getPayConfig();
        //准备数据
        Map<String, String> data = new HashMap<>();
        //请求地址集
        Urls urls = this.getUrls();

        try {
            String merId = config.getBargainorId();//商户号
            KeyStore keyStore = CertUtil.loadKeyStore(new RAMFileProxy(config, "signCert"), config.getBargainorKey());
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
            data.put("backUrl", paySettings.getUrl() + "/pays/" + payment.getSn() + "/notify");

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
            Map<String, String> result = SignUtil.parseQuery(response.text(), true);// 将返回结果转换为map
            result.put("signature", result.get("signature").replaceAll(" ", "+"));
            if (!verify(result, CertUtil.loadPublicKey(new RAMFileProxy(config, "validateCert")))) {//验证签名
                throw new PayException("验证签名失败");
            }
            payment.setTradeNo(result.get("tn"));
            return result;
        } catch (IOException e) {
            throw new PayException(e.getMessage());
        }
    }

    @Override
    public Object payNotify(Payment payment, String result) throws PayException {
        //支付配置
        PayConfig config = payment.getPayConfig();
        PublicKey publicKey = CertUtil.loadPublicKey(new RAMFileProxy(config, "validateCert"));

        try {
            Map<String, String> data = result.startsWith("{") && result.endsWith("}") ? (Map<String, String>) JSON.deserialize(result, HashMap.class) : SignUtil.parseQuery(result, true);
            //手机支付通知
            assert data != null;
            boolean isAppNotify = data.containsKey("sign");
            if (!(isAppNotify && verify(data.get("data"), data.get("sign"), publicKey) || verify(data, publicKey))) {//验证签名
                throw new PayException("验证签名失败");
            }
            if (isAppNotify) {
                Map<String, String> payresult = SignUtil.parseQuery(data.get("data"), true);
                payment.setStatus("success".equals(payresult.get("pay_result")) ? PaymentStatus.success : PaymentStatus.failure);
                payment.setTradeTime(DateUtil.now());
            } else {
                if (!StringUtil.nullValue(data.get("orderId")).equals(payment.getSn())) {
                    throw new PayException("通知与订单不匹配");
                }
                payment.setTradeNo(data.get("queryId"));
                payment.setStatus("00".equals(data.get("respCode")) ? PaymentStatus.success : PaymentStatus.failure);
                payment.setTradeTime(DateUtil.now());
            }
            return isAppNotify ? null : "OK";
        } finally {
            this.log("in", "notify", payment, config, result);
        }
    }

    @Override
    public String refund(Refund refund) {
        Payment payment = refund.getPayment();
        //支付配置
        PayConfig config = refund.getPayConfig();
        //准备数据
        Map<String, String> data = new HashMap<>();
        //请求地址集
        Urls urls = this.getUrls();

        try {
            String merId = config.getBargainorId();//商户号
            KeyStore keyStore = CertUtil.loadKeyStore(new RAMFileProxy(config, "signCert"), config.getBargainorKey());
            String certPwd = config.getBargainorKey();//签名证书密码

            /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
            data.put("version", version);               //版本号
            data.put("encoding", encoding);             //字符集编码 可以使用UTF-8,GBK两种方式
            data.put("signMethod", "01");               //签名方法 目前只支持01-RSA方式证书加密
            data.put("txnType", "04");                  //交易类型 04-退货
            data.put("txnSubType", "00");               //交易子类型  默认00
            data.put("bizType", "000201");              //业务类型
            data.put("channelType", "08");              //渠道类型，07-PC，08-手机

            /***商户接入参数***/
            data.put("merId", merId);                   //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
            data.put("accessType", "0");                         //接入类型，商户接入固定填0，不需修改
            data.put("orderId", refund.getSn());          //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
            data.put("txnTime", DateUtil.format(refund.getCreateTime(), "yyyyMMddHHmmss"));      //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
            data.put("currencyCode", "156");                     //交易币种（境内商户一般是156 人民币）
            data.put("txnAmt", refund.getTotalAmount().multiply(BigDecimal.valueOf(100d)).intValue() + "");                          //****退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额
            //data.put("reqReserved", "透传信息");                    //请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节
            data.put("backUrl", paySettings.getUrl() + "/pays/" + refund.getSn() + "/notify");               //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 退货交易 商户通知,其他说明同消费交易的后台通知

            /***要调通交易以下字段必须修改***/
            data.put("origQryId", payment.getTradeNo());      //****原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取

            //添加签名
            data.put("certId", CertUtil.getCertId(keyStore));
            data.put("signature", signature(data, keyStore, certPwd));

            Response response = HttpClientUtil.doPost(urls.getBackTransUrl(), data);
            if (response.getStatusCode() != 200) {
                throw new IOException("请求失败:" + response.getStatusCode() + "\t" + response.getBody());
            }
            //验签
            Map<String, String> result = SignUtil.parseQuery(response.text(), true);// 将返回结果转换为map

            if (!verify(result, CertUtil.loadPublicKey(new RAMFileProxy(config, "validateCert")))) {//验证签名
                throw new PayException("验证签名失败");
            }
            String respCode = result.get("respCode");
            if (("00").equals(respCode)) {//交易已受理(不代表交易已成功），等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
                refund.setStatus(RefundStatus.wait);
            } else if (("03").equals(respCode) || ("04").equals(respCode) || ("05").equals(respCode)) {
                //TODO 后续需发起交易状态查询交易确定交易状态
            } else {//其他应答码为失败请排查原因
                refund.setStatus(RefundStatus.failure);
            }
            return null;
        } catch (IOException e) {
            throw new PayException(e.getMessage());
        }
    }

    @Override
    public Object payNotify(Refund refund, String result) throws PayException {
        //支付配置
        PayConfig config = refund.getPayConfig();
        PublicKey publicKey = CertUtil.loadPublicKey(new RAMFileProxy(config, "validateCert"));
        try {
            Map<String, String> data = SignUtil.parseQuery(result, true);
            if (!verify(data, publicKey)) {//验证签名
                throw new PayException("验证签名失败");
            }
            if (!StringUtil.nullValue(data.get("orderId")).equals(refund.getSn())) {
                throw new PayException("通知与订单不匹配");
            }
            refund.setTradeNo(data.get("queryId"));
            refund.setStatus("00".equals(data.get("respCode")) ? RefundStatus.success : RefundStatus.failure);

            return null;
        } finally {
            //记录支付通知日志
            this.log("in", "notify", refund, config, result);
        }

    }

    public PaymentStatus query(Payment payment) {
        //请求地址集
        Urls urls = this.getUrls();
        try {
            //支付配置
            PayConfig config = payment.getPayConfig();
            String merId = config.getBargainorId();//商户号
            //签名证书
            KeyStore keyStore = CertUtil.loadKeyStore(new RAMFileProxy(config, "signCert"), config.getBargainorKey());
            String certPwd = config.getBargainorKey();//签名证书密码

            final Map<String, String> data = new TreeMap<>();

            /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
            data.put("version", version);                 //版本号
            data.put("encoding", encoding);          //字符集编码 可以使用UTF-8,GBK两种方式
            data.put("signMethod", "01");                          //签名方法 目前只支持01-RSA方式证书加密
            data.put("txnType", "00");                             //交易类型 00-默认
            data.put("txnSubType", "00");                          //交易子类型  默认00
            data.put("bizType", "000201");                         //业务类型

            /***商户接入参数***/
            data.put("merId", merId);                               //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
            data.put("accessType", "0");                           //接入类型，商户接入固定填0，不需修改

            /***要调通交易以下字段必须修改***/
            data.put("orderId", payment.getSn());                            //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
            data.put("txnTime", DateUtil.format(payment.getCreateTime(), "yyyyMMddHHmmss"));//****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间

            data.put("Signature", signature(data, keyStore, certPwd));//签名

            Response response = HttpClientUtil.doPost(urls.getQueryTransUrl(), data);

            Map<String, String> result = SignUtil.parseQuery(response.text(), true);

            if (!verify(result, CertUtil.loadPublicKey(new RAMFileProxy(config, "validateCert")))) {
                throw new PayException("验证签名失败");
            }

            return null;//response.text();

        } catch (IOException e) {


            LOG.error(e.getMessage(), e);
            throw new PayException("支付过程发生错误，错误信息为:" + e.getMessage());
        }
    }

    @Override
    public void close(Payment payment) throws PayException {

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

        private void setFrontTransUrl(String frontTransUrl) {
            this.frontTransUrl = frontTransUrl;
        }

        private String getAppTransUrl() {
            return appTransUrl;
        }

        private void setAppTransUrl(String appTransUrl) {
            this.appTransUrl = appTransUrl;
        }

        private String getBackTransUrl() {
            return backTransUrl;
        }

        private void setBackTransUrl(String backTransUrl) {
            this.backTransUrl = backTransUrl;
        }

        private String getSingleQueryUrl() {
            return singleQueryUrl;
        }

        private void setSingleQueryUrl(String singleQueryUrl) {
            this.singleQueryUrl = singleQueryUrl;
        }

        public String getBatchTransUrl() {
            return batchTransUrl;
        }

        private void setBatchTransUrl(String batchTransUrl) {
            this.batchTransUrl = batchTransUrl;
        }

        private String getFileTransUrl() {
            return fileTransUrl;
        }

        private String getCardTransUrl() {
            return cardTransUrl;
        }

        private void setCardTransUrl(String cardTransUrl) {
            this.cardTransUrl = cardTransUrl;
        }

        private String getQueryTransUrl() {
            return queryTransUrl;
        }

        private void setQueryTransUrl(String queryTransUrl) {
            this.queryTransUrl = queryTransUrl;
        }

        private void setFileTransUrl(String fileTransUrl) {
            this.fileTransUrl = fileTransUrl;
        }
    }

    private String signature(Map<String, String> params, KeyStore keyStore, String certPwd) {
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
    private boolean verify(Map<String, String> result, PublicKey publicKey) {
        String stringSign = result.get("signature");
        String stringData = SignUtil.coverMapString(result, "signature");
        String encoding = result.get("encoding");
        try {
            return SecureUtil.validateSignBySoft(publicKey, SecureUtil.base64Decode(stringSign.getBytes(encoding)), SecureUtil.sha1X16(stringData, encoding));
        } catch (IOException var6) {
            LOG.error(var6.getMessage(), var6);
        }
        return false;
    }

    /**
     * 验签逻辑
     *
     * @param data      返回数据
     * @param signature 需要比较的签名
     * @param publicKey 验签证书
     * @return boolean
     */
    private boolean verify(String data, String signature, PublicKey publicKey) {
        try {
            return SecureUtil.validateSignBySoft(publicKey, SecureUtil.base64Decode(signature.getBytes(encoding)), SecureUtil.sha1X16(data, encoding));
        } catch (IOException var7) {
            LOG.error(var7.getMessage(), var7);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}