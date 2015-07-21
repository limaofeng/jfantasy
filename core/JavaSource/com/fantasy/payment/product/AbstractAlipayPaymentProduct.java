package com.fantasy.payment.product;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.product.httpClient.HttpProtocolHandler;
import com.fantasy.payment.product.httpClient.HttpRequest;
import com.fantasy.payment.product.httpClient.HttpResponse;
import com.fantasy.payment.product.httpClient.HttpResultType;
import com.fantasy.payment.product.sign.MD5;
import com.fantasy.payment.product.sign.RSA;
import com.fantasy.payment.service.PaymentContext;
import org.apache.commons.httpclient.NameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public abstract class AbstractAlipayPaymentProduct extends AbstractPaymentProduct {

    // 字符编码格式 目前支持  utf-8
    public final static String input_charset = "utf-8";

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
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    /**
     * 生成签名结果
     *
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
    public static String buildRequestMysign(Map<String, String> sPara, PaymentConfig config) {
        String prestr = createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        if ("MD5".equals(sPara.get("sec_id"))) {
            mysign = MD5.sign(prestr, config.getBargainorKey(), input_charset);
        } else if ("0001".equals(sPara.get("sec_id"))) {
            mysign = RSA.sign(prestr, config.getBargainorKey(), input_charset);
        }
        return mysign;
    }

    /**
     * 生成要请求给支付宝的参数数组
     *
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    protected static Map<String, String> buildRequestPara(Map<String, String> sParaTemp, PaymentConfig config) {
        String signType = sParaTemp.get("sign_type");
        //除去数组中的空值和签名参数
        Map<String, String> sPara = paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara, config);
        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        if (!"alipay.wap.trade.create.direct".equals(sPara.get("service")) && !"alipay.wap.auth.authAndExecute".equals(sPara.get("service"))) {
            sPara.put("sign_type", signType);
        }
        return sPara;
    }

    /**
     * 建立请求，以模拟远程HTTP的POST请求方式构造并获取支付宝的处理结果
     * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值
     * 如：buildRequest("", "",sParaTemp)
     *
     * @param ALIPAYGATEWAYNEW 支付宝网关地址
     * @param strParaFileName    文件类型的参数名
     * @param strFilePath        文件路径
     * @param sParaTemp          请求参数数组
     * @return 支付宝处理结果
     */
    public static String buildRequest(String ALIPAYGATEWAYNEW, String strParaFileName, String strFilePath, Map<String, String> sParaTemp, PaymentConfig paymentConfig) throws IOException {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp, paymentConfig);

        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        //设置编码集
        request.setCharset(input_charset);

        request.setParameters(generatNameValuePair(sPara));
        request.setUrl(ALIPAYGATEWAYNEW);
        HttpResponse response = httpProtocolHandler.execute(request, strParaFileName, strFilePath);
        return response.getStringResult();
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
     * MAP类型数组转换成NameValuePair类型
     *
     * @param properties MAP类型数组
     * @return NameValuePair类型数组
     */
    private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        return nameValuePair;
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
            verifyResponse = verifyResponse(PaymentContext.getContext().getPaymentConfig().getBargainorId(), notifyId);
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
            preSignStr = createLinkString(sParaNew);
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
     * @param partner   合作身份者ID
     * @param notifyId 通知校验ID
     * @return 服务器ATN结果
     * 验证结果集：
     * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空
     * true 返回正确信息
     * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    public static boolean verifyResponse(String partner, String notifyId) {
        //获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
        String veryfyUrl = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notifyId;
        return checkUrl(veryfyUrl);
    }

    /**
     * 获取远程服务器ATN结果
     *
     * @param urlvalue 指定URL路径地址
     * @return 服务器ATN结果
     * 验证结果集：
     * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空
     * true 返回正确信息
     * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    private static boolean checkUrl(String urlvalue) {
        try {
            URL url = new URL(urlvalue);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            return "true".equalsIgnoreCase(in.readLine());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }

    }

    @Override
    public String getPaynotifyMessage(String paymentSn) {
        return "success";
    }

}
