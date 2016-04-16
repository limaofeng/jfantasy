package org.jfantasy.pay.product;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.order.Order;
import org.jfantasy.pay.product.sign.MD5;
import org.jfantasy.pay.product.sign.RSA;
import org.jfantasy.pay.product.sign.SignUtil;
import org.jfantasy.pay.service.PaymentContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AlipayPayProductSupport extends PayProductSupport {

    // 字符编码格式 目前支持  utf-8
    public final static String input_charset = "utf-8";

    public static final DecimalFormat RMB_YUAN_FORMAT = new DecimalFormat("#0.00");

    @Override
    public Object app(Payment payment, Order order, Properties properties) throws PayException {
        PayConfig config = payment.getPayConfig();

        Map<String, String> data = new TreeMap<String, String>();
        data.put("service", "mobile.securitypay.pay");//接口名称
        data.put("partner", config.getBargainorId());//合作者身份ID
        data.put("_input_charset", input_charset);//参数编码字符集
        data.put("sign_type", "RSA");//签名方式
        data.put("notify_url", SettingUtil.getServerUrl() + "/pays/" + payment.getSn() + "/notify");//服务器异步通知页面路径

        data.put("out_trade_no", payment.getSn());//商户网站唯一订单号
        data.put("subject", order.getSubject());//商品名称
        data.put("payment_type", "1");//支付类型
        data.put("seller_id", config.getSellerEmail());//卖家支付宝账号
        data.put("total_fee", RMB_YUAN_FORMAT.format(order.getPayableFee()));//总金额
        data.put("body", order.getBody());//商品详情

        if (properties.containsKey("app_id")) {
            data.put("app_id", properties.getProperty("app_id"));//客户端号
        }
        if (properties.containsKey("app_id")) {
            data.put("appenv", properties.getProperty("appenv"));//客户端来源
        }
        data.put("goods_type", properties.getProperty("goods_type", "1"));//商品类型 具体区分本地交易的商品类型。1：实物交易； 0：虚拟交易。默认为1（实物交易）。
        data.put("rn_check", properties.getProperty("rn_check", "F"));//是否发起实名校验 T：发起实名校验；F：不发起实名校验。
        data.put("it_b_pay", properties.getProperty("it_b_pay", "30m"));//未付款交易的超时时间 设置未付款交易的超时时间，一旦超时，该笔交易就会自动被关闭。当用户输入支付密码、点击确认付款后（即创建支付宝交易后）开始计时。取值范围：1m～15d，或者使用绝对时间（示例格式：2014-06-13 16:00:00）。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。该参数数值不接受小数点，如1.5h，可转换为90m。
        if (properties.containsKey("extern_token")) {
            data.put("extern_token", properties.getProperty("extern_token"));//授权令牌 开放平台返回的包含账户信息的token（授权令牌，商户在一定时间内对支付宝某些服务的访问权限）。通过授权登录后获取的alipay_open_id，作为该参数的value，登录授权账户即会为支付账户。
        }
        if (properties.containsKey("out_context")) {
            data.put("out_context", properties.getProperty("out_context"));//商户业务扩展参数 业务扩展参数，支付宝特定的业务需要添加该字段，json格式。 商户接入时和支付宝协商确定。
        }

        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANesnTGjdj/aEtnZ" +
                "P39jKpVEEj7cuvwc4DHJbmGpyKti235ejE9V8h2puTqX3Xof5UidjFNwiHChFZmi" +
                "+EU8i8OQK09PR4OALhg9TdSBWF+mwLpJ5LeHYEXXUkWwWtK94IW12K/n4aEEVtRZ" +
                "rfa9REKrclCATnT5v/Qspqp86oNdAgMBAAECgYEAoOyHDfat0M7iqfHT0zUnHOEB" +
                "zC3exya0kfF+jxikRl0o8Y2Sm8/BLCjrsLCH7QvHhPspLUkWRROsjkpvfRnEHfTY" +
                "d3NeccKW2YuFmcL2U7J3AHvdghSR8IE2sTA8CRorCMeS0FjHc+zgJTOIalrzgDjU" +
                "U4C6KRGaSuCggy0wp4ECQQDsAkKQoLDN7Ig2D8KkhR3C9R4VgXgJBwZjJYQLHE2Q" +
                "lMPTp0TnHgJJfJFcec2kM96gIULwlsoiXNLWp313GUMFAkEA6fFop4CUVFrWWw01" +
                "WqhLSb3Q1aQ83XYOf7eku2SgEv5jEkZmpkJu5k3dSCRcgriXPhLw7hFIvEW6Gpy2" +
                "uEZeeQJBAM0LzZ9wLQxMI6+sk7RyfwACDIgsuwhE1TTQxF8O0Qj7ZwP9gKy38s67" +
                "7mME5Dh0ZEiFfW4f5DBkqz2ZuTT/eq0CQDLK1DMR6qKJ+mJYcs4VHguLp8zK1OAs" +
                "Yqd+IskA5vRYwP/VwzGz2Mot+65PHrrPAx9aE29M12LxLJ/ciJtnw9kCQEVvWtNU" +
                "CDLaWnH4SxTG7P9OsfNWgz//O7eT69wsGoFHb2PeN9XVDBQbr7SomrGeYa8SwqCS" +
                "rU4/l+JjAg+bR7o=";

        data.put("sign", StringUtil.encodeURI(sign(data, privateKey), "utf-8"));

        return SignUtil.coverMapString(data);
    }

    /**
     * 把数组所有元素按照固定参数排序，以“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkStringNoSort(Map<String, String> params) {
        //手机网站支付MD5签名固定参数排序，顺序参照文档说明
        return "service=" + params.get("service") + "&v=" + params.get("v") + "&sec_id=" + params.get("sec_id") + "&notify_data=" + params.get("notify_data");
    }

    /**
     * 根据参数集合组合参数字符串（忽略空值参数）
     *
     * @param params 请求参数
     * @return 参数字符串
     */
    protected String getParameterString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        AtomicReference<StringBuffer> stringBuffer = new AtomicReference<StringBuffer>(new StringBuffer());
        for (String key : keys) {
            String value = params.get(key);
            if (StringUtils.isNotEmpty(value)) {
                stringBuffer.get().append("&").append(key).append("=").append(value);
            }
        }
        stringBuffer.get().deleteCharAt(0);
        return stringBuffer.get().toString();
    }

    protected static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.isEmpty()) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || "".equals(value) || "sign".equalsIgnoreCase(key) || "sign_type".equalsIgnoreCase(key)) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * 生成签名结果
     *
     * @param data 要签名的数组
     * @param key  sign_type = MD5 时为 安全校验码 如果为 sign_type = RSA 时为
     * @return 签名结果字符串
     */
    public static String sign(Map<String, String> data, String key) {
        if (!data.containsKey("sign_type")) {
            data.put("sign_type", "MD5");
        }
        String signType = data.get("sign_type");
        if ("MD5".equals(signType)) {
            return MD5.sign(SignUtil.coverMapString(data, "sign", "sign_type"), key, input_charset);
        } else if ("RSA".equals(signType)) {
            return RSA.sign(SignUtil.coverMapString(data, "sign", "sign_type"), key, input_charset);
        } else if ("DSA".equals(signType)) {
            throw new PayException("DSA 签名方式还未实现");
        }
        throw new PayException("不支持的签名方式 => " + signType);
    }

    public static boolean verify(Map<String, String> data, String key) {
        String signType = data.get("sign_type");
        if ("MD5".equals(signType)) {
            return MD5.verify(SignUtil.coverMapString(data, "sign", "sign_type"), data.get("sign"), key, input_charset);
        } else if ("RSA".equals(signType)) {
            return RSA.verify(SignUtil.coverMapString(data, "sign", "sign_type"), data.get("sign"), key, input_charset);
        } else if ("DSA".equals(signType)) {
            throw new PayException("DSA 签名方式还未实现");
        }
        throw new PayException("不支持的签名方式 => " + signType);
    }

    /**
     * 生成要请求给支付宝的参数数组
     *
     * @param data 请求前的参数数组
     * @return 要请求的参数数组
     */
    protected static Map<String, String> buildRequestPara(Map<String, String> data, String bargainorKey) {
        String signType = ObjectUtil.defaultValue(data.get("sign_type"), "MD5");
        //签名结果与签名方式加入请求提交参数组中
        data.put("sign", sign(data, bargainorKey));
        if (!"alipay.wap.trade.create.direct".equals(data.get("service")) && !"alipay.wap.auth.authAndExecute".equals(data.get("service"))) {
            data.put("sign_type", signType);
        }
        return data;
    }

    /**
     * 解析远程模拟提交后返回的信息，获得token
     *
     * @param text 要解析的字符串
     * @return 解析结果
     */
    public static String getRequestToken(String text, String signType) throws Exception {
        String requestToken = "";
        //以“&”字符切割字符串
        String[] strSplitText = text.split("&");
        //把切割后的字符串数组变成变量与数值组合的字典数组
        Map<String, String> paraText = new HashMap<String, String>();
        for (String aStrSplitText : strSplitText) {

            //获得第一个=字符的位置
            int nPos = aStrSplitText.indexOf("=");
            //获得字符串长度
            int nLen = aStrSplitText.length();
            //获得变量名
            String strKey = aStrSplitText.substring(0, nPos);
            //获得数值
            String strValue = aStrSplitText.substring(nPos + 1, nLen);
            //放入MAP类中
            paraText.put(strKey, strValue);
        }

        if (paraText.get("res_data") != null) {
            String resData = paraText.get("res_data");
            //解析加密部分字符串（RSA与MD5区别仅此一句）
            if ("0001".equals(signType)) {
                resData = RSA.decrypt(resData, PaymentContext.getContext().getPaymentConfig().getBargainorKey(), input_charset);
            }

            //token从res_data中解析出来（也就是说res_data中已经包含token的内容）
            Document document = DocumentHelper.parseText(resData);
            requestToken = document.selectSingleNode("//direct_trade_create_res/request_token").getText();
        }
        return requestToken;
    }

    /**
     * 解密
     *
     * @param inputPara 要解密数据
     * @return 解密后结果
     */
    public static Map<String, String> decrypt(Map<String, String> inputPara) throws Exception {
        inputPara.put("notify_data", RSA.decrypt(inputPara.get("notify_data"), PaymentContext.getContext().getPaymentConfig().getBargainorKey(), input_charset));
        return inputPara;
    }

    /**
     * 验证消息是否是支付宝发出的合法消息，验证服务器异步通知
     *
     * @param params 通知返回来的参数数组
     * @return 验证结果
     */
    public static boolean verifyNotify(Map<String, String> params) throws Exception {
        //获取是否是支付宝服务器发来的请求的验证结果
        boolean verifyResponse;
        try {
            //XML解析notify_data数据，获取notify_id
            Document document = DocumentHelper.parseText(params.get("notify_data"));
            String notifyId = document.selectSingleNode("//notify/notify_id").getText();
            verifyResponse = verifyNotifyId(PaymentContext.getContext().getPaymentConfig().getBargainorId(), notifyId);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            verifyResponse = false;
        }
        //获取返回时的签名验证结果
        //判断responsetTxt是否为true，isSign是否为true
        //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        return getSignVeryfy(params, ObjectUtil.defaultValue(params.get("sign"), ""), false) && verifyResponse;
    }


    /**
     * 根据反馈回来的信息，生成签名结果
     *
     * @param params 通知返回来的参数数组
     * @param sign   比对的签名结果
     * @param isSort 是否排序
     * @return 生成的签名结果
     */
    private static boolean getSignVeryfy(Map<String, String> params, String sign, boolean isSort) {
        //过滤空值、sign与sign_type参数
        Map<String, String> sParaNew = paraFilter(params);
        //获取待签名字符串
        String preSignStr;
        if (isSort) {
            preSignStr = null;//createLinkString(sParaNew);
        } else {
            preSignStr = createLinkStringNoSort(sParaNew);
        }
        //获得签名验证结果
        boolean isSign = false;
        if ("MD5".equals(params.get("sign_type"))) {
            isSign = MD5.verify(preSignStr, sign, PaymentContext.getContext().getPaymentConfig().getBargainorKey(), input_charset);
        } else if ("0001".equals(params.get("sign_type"))) {
            isSign = RSA.verify(preSignStr, sign, PaymentContext.getContext().getPaymentConfig().getBargainorId(), input_charset);
        }
        return isSign;
    }

    /**
     * 支付宝消息验证地址
     */
    private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

    /**
     * 获取远程服务器ATN结果,验证返回URL
     *
     * @param partner  合作身份者ID
     * @param notifyId 通知校验ID
     * @return 服务器ATN结果
     * 验证结果集：
     * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空
     * true 返回正确信息
     * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    public static boolean verifyNotifyId(String partner, String notifyId) {
        //获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
        String veryfyUrl = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notifyId;
        try {
            URL url = new URL(veryfyUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            return "true".equalsIgnoreCase(in.readLine());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    public String getPaynotifyMessage(String paymentSn) {
        return "success";
    }

    @Override
    public String web(Payment payment, Order order, Properties properties) throws PayException {
        return null;
    }

    @Override
    public String wap() {
        return null;
    }

}
