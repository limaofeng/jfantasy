package org.jfantasy.pay.product;

import org.jfantasy.file.FileItem;
import org.jfantasy.file.FileManager;
import org.jfantasy.file.bean.FileDetail;
import org.jfantasy.file.manager.LocalFileManager;
import org.jfantasy.framework.httpclient.HttpClientUtil;
import org.jfantasy.framework.httpclient.Response;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.PathUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.order.Order;
import org.jfantasy.pay.product.util.CertUtil;
import org.jfantasy.pay.product.util.SecureUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class Unionpay extends PayProductSupport {

    private final static Log LOG = LogFactory.getLog(Unionpay.class);

    private DeployStatus deployStatus;

    private Map<DeployStatus, Urls> urlsMap = new HashMap<DeployStatus, Urls>() {
        {
            //##########################交易发送地址配置#############################
            //######(以下配置为PM环境：入网测试环境用，生产环境配置见文档说明)#######
            Urls urls = new Urls();
            urls.setFrontTransUrl("https://101.231.204.80:5000/gateway/api/frontTransReq.do");
            urls.setAppTransUrl("https://101.231.204.80:5000/gateway/api/appTransReq.do");
            urls.setBackTransUrl("https://101.231.204.80:5000/gateway/api/backTrans.do");
            urls.setCardTransUrl("https://101.231.204.80:5000/gateway/api/cardTransReq.do");
            urls.setSingleQueryUrl("https://101.231.204.80:5000/gateway/api/queryTrans.do");
            urls.setBatchTransUrl("https://101.231.204.80:5000/gateway/api/batchTransReq.do");
            urls.setFileTransUrl("https://101.231.204.80:9080/");
            this.put(DeployStatus.Develop, urls);
        }
    };

    private Urls getUrls() {
        return urlsMap.get(this.deployStatus);
    }

    @Override
    public String web(Payment payment,Order order, Properties properties) {
        return null;
    }

    @Override
    public String wap() {
        return null;
    }

    @Override
    public String app(Payment payment,Order order) throws PayException {
        //支付配置
        PayConfig config = payment.getPayConfig();
        //准备数据
        Map<String, String> data = new HashMap<String, String>();

        String merId = "777290058123224";//商户号
        String txnTime = DateUtil.format("yyyyMMddHHmmss");// --订单发送时间
        String orderId = DateUtil.format("yyyyMMddHHmmss");// --商户订单号

        String txnAmt = "1";

        String backUrl = "/pays/" + payment.getSn() + "/notify";

        //固定填写
        data.put("version", version);//M
        //默认取值：UTF-8
        data.put("encoding", encoding);//M
        //01RSA02 MD5 (暂不支持)
        data.put("signMethod", "01");//M
        //取值：01
        data.put("txnType", "01");//M
        //01：自助消费，通过地址的方式区分前台消费和后台消费（含无跳转支付）
        data.put("txnSubType", "01");//M
        //
        data.put("bizType", "000000");//M
        data.put("channelType", "07");//M

        //后台返回商户结果时使用，如上送，则发送商户后台交易结果通知
        data.put("backUrl", backUrl);//M

        //0：普通商户直连接入2：平台类商户接入
        data.put("accessType", "0");//M
        //商户号
        data.put("merId", merId);//M

//		//商户类型为平台类商户接入时必须上送
//		contentData.put("subMerId", subMerId);//C
//		//商户类型为平台类商户接入时必须上送
//		contentData.put("subMerName", subMerName);//C
//		//商户类型为平台类商户接入时必须上送
//		contentData.put("subMerAbbr", subMerAbbr);//C

        //商户端生成
        data.put("orderId", orderId);//M
        //商户发送交易时间
        data.put("txnTime", txnTime);//M

//		//后台类交易且卡号上送；跨行收单且收单机构收集银行卡信息时上送01：银行卡02：存折03：C卡默认取值：01取值“03”表示以IC终端发起的IC卡交易，IC作为普通银行卡进行支付时，此域填写为“01”
//		contentData.put("accType", accType);//C
//		//1、  后台类消费交易时上送全卡号或卡号后4位 2、  跨行收单且收单机构收集银行卡信息时上送、  3、前台类交易可通过配置后返回，卡号可选上送
//		contentData.put("accNo", accNo);//C

        //交易单位为分
        data.put("txnAmt", txnAmt);//M
        //默认为156交易 参考公参
        data.put("currencyCode", "156");//M

//		//PC1、前台类消费交易时上送2、认证支付2.0，后台交易时可选
//		contentData.put("orderTimeout", orderTimeout);//O
//		//PC超过此时间用户支付成功的交易，不通知商户，系统自动退款，大约5个工作日金额返还到用户账户
//		contentData.put("payTimeout", payTimeout);//O
//		contentData.put("termId", termId);//O
//		//商户自定义保留域，交易应答时会原样返回
//		contentData.put("reqReserved", reqReserved);//O
//		//子域名： 活动号 marketId  移动支付订单推送时，特定商户可以通过该域上送该订单支付参加的活动号
//		contentData.put("reserved", reserved);//O
//		//格式如下：{子域名1=值&子域名2=值&子域名3=值}
//		contentData.put("riskRateInfo", riskRateInfo);//O
//		//当使用银联公钥加密密码等信息时，需上送加密证书的CertID；说明一下？目前商户、机构、页面统一套
//		contentData.put("encryptCertId", encryptCertId);//C
//		//前台消费交易若商户上送此字段，则在支付失败时，页面跳转至商户该URL（不带交易信息，仅跳转）
//		contentData.put("frontFailUrl", frontFailUrl);//O
//		//C  取值参考数据元说明
//		contentData.put("defaultPayType", defaultPayType);//O
//		//C当帐号类型为02-存折时需填写在前台类交易时填写默认银行代码，支持直接跳转到网银商户发卡银行控制系统应答返回
//		contentData.put("issInsCode", issInsCode);//O
//		//仅仅pc使用，使用哪种支付方式 由收单机构填写，取值为以下内容的一种或多种，通过逗号（，）分割。取值参考数据元说明
//		contentData.put("supPayType", supPayType);//O

//		//移动支付业务需要上送
//		contentData.put("userMac", userMac);//O
//		//前台交易，有IP防钓鱼要求的商户上送
//		contentData.put("customerIp", customerIp);//C
//		//有卡交易必填有卡交易信息域
//		contentData.put("cardTransData", cardTransData);//C
//		//渠道类型为语音支付时使用
//		contentData.put("vpcTransData", vpcTransData);//C
//		//移动支付上送
//		contentData.put("orderDesc", orderDesc);//C

        //提交请求
        try {
            //请求地址集
            Urls urls = this.getUrls();

            config.getBargainorId();//商户号

            KeyStore keyStore = CertUtil.loadKeyStore(loadFileItem(config.getSignCert()), config.getBargainorKey());

            String certPwd = config.getBargainorKey();//签名证书密码

            //整理数据
            data = settle(data);

            //添加签名
            data.put("certId", CertUtil.getCertId(keyStore));
            data.put("signature", sign(coverMapString(data), encoding, keyStore, certPwd));

            //发送请求
            LOG.debug("url ==> " + urls.getAppTransUrl() + System.getProperty("line.separator") + "data:" + data);
            Response response = HttpClientUtil.doPost(urls.getAppTransUrl(), data);
            if (response.getStatusCode() != 200) {
                throw new IOException("请求失败:" + response.getStatusCode() + "\t" + response.getBody());
            }

            //验签
            Map<String, String> result = convertResultStringToMap(response.getBody()); // 将返回结果转换为map
            if (validate(result, encoding, loadFileItem(config.getValidateCert()))) {//验证签名
                System.out.println("验证签名成功");
            } else {
                System.out.println("验证签名失败");
            }

            System.out.println("请求报文=[" + data.toString() + "]");
            System.out.println("应答报文=[" + result.toString() + "]");
        } catch (IOException e) {
            throw new PayException(e.getMessage());
        }
        return null;
    }

    @Override
    public Payment payNotify(Payment payment,  String result) throws PayException{
        return null;
    }

    public static String encoding = "UTF-8";

    public static String version = "5.0.0";

    /**
     * 整理提交的数据,去掉空值数据
     *
     * @param data 提交数据
     * @return 新的数据
     */
    public static Map<String, String> settle(Map<String, String> data) {
        Map<String, String> submitFromData = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String value = entry.getValue();
            if (StringUtils.isNotBlank(value)) {
                submitFromData.put(entry.getKey(), value.trim());
            }
        }
        return submitFromData;
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

        public void setFileTransUrl(String fileTransUrl) {
            this.fileTransUrl = fileTransUrl;
        }
    }

    public static FileItem loadFileItem(FileDetail fileDetail) {
        FileManager fileManager = new LocalFileManager(PathUtil.classes());
        return fileManager.getFileItem(fileDetail.getAbsolutePath());
        /*
        FileManager fileManager = FileManagerFactory.getInstance().getFileManager(fileDetail.getFileManagerId());
        return fileManager.getFileItem(fileDetail.getAbsolutePath());
        */
    }

    /**
     * 银联支付签名逻辑
     *
     * @param stringData 请求数据
     * @param encoding   编码格式
     * @return boolean
     */
    public static String sign(String stringData, String encoding, KeyStore keyStore, String certPwd) {
        if (StringUtil.isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        try {
            byte[] e = SecureUtil.sha1X16(stringData, encoding);
            byte[] byteSign1 = SecureUtil.base64Encode(SecureUtil.signBySoft(CertUtil.getCertPrivateKey(keyStore, certPwd), e));
            return new String(byteSign1);
        } catch (Exception var6) {
            LOG.error("签名异常", var6);
            throw new RuntimeException(var6);
        }
    }

    public static String coverMapString(Map<String, String> data) {
        TreeMap<String, String> tree = new TreeMap<String, String>();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (!"signature".equals(entry.getKey())) {
                tree.put(entry.getKey(), entry.getValue());
            }
        }

        StringBuilder sf = new StringBuilder();
        for (Map.Entry<String, String> entry : tree.entrySet()) {
            sf.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        return sf.substring(0, sf.length() - 1);
    }

    public static Map<String, String> convertResultStringToMap(String result) {
        try {
            if (StringUtils.isNotBlank(result)) {
                if (result.startsWith("{") && result.endsWith("}")) {
                    result = result.substring(1, result.length() - 1);
                }
                return parseQString(result);
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public static Map<String, String> parseQString(String str) throws UnsupportedEncodingException {
        HashMap map = new HashMap();
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        String key = null;
        boolean isKey = true;
        boolean isOpen = false;
        byte openName = 0;
        if (len > 0) {
            for (int i = 0; i < len; ++i) {
                char curChar = str.charAt(i);
                if (isKey) {
                    if (curChar == 61) {
                        key = temp.toString();
                        temp.setLength(0);
                        isKey = false;
                    } else {
                        temp.append(curChar);
                    }
                } else {
                    if (isOpen) {
                        if (curChar == openName) {
                            isOpen = false;
                        }
                    } else {
                        if (curChar == 123) {
                            isOpen = true;
                            openName = 125;
                        }
                        if (curChar == 91) {
                            isOpen = true;
                            openName = 93;
                        }
                    }
                    if (curChar == 38 && !isOpen) {
                        putKeyValueToMap(temp, isKey, key, map);
                        temp.setLength(0);
                        isKey = true;
                    } else {
                        temp.append(curChar);
                    }
                }
            }
            putKeyValueToMap(temp, isKey, key, map);
        }
        return map;
    }


    private static void putKeyValueToMap(StringBuilder temp, boolean isKey, String key, Map<String, String> map) throws UnsupportedEncodingException {
        if (isKey) {
            key = temp.toString();
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }

            map.put(key, "");
        } else {
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }

            map.put(key, temp.toString());
        }

    }

    /**
     * 验签逻辑
     *
     * @param resData  返回数据
     * @param encoding 编码格式
     * @param fileItem 验签证书
     * @return boolean
     */
    public static boolean validate(Map<String, String> resData, String encoding, FileItem fileItem) {
        LOG.debug("验签处理开始.");
        if (StringUtil.isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        String stringSign = resData.get("signature");
        String certId = resData.get("certId");
        String stringData = coverMapString(resData);
        try {
            return SecureUtil.validateSignBySoft(CertUtil.getValidateKey(certId), SecureUtil.base64Decode(stringSign.getBytes(encoding)), SecureUtil.sha1X16(stringData, encoding));
        } catch (UnsupportedEncodingException var6) {
            LOG.error(var6.getMessage(), var6);
        } catch (Exception var7) {
            LOG.error(var7.getMessage(), var7);
        }

        return false;
    }


}
