package com.fantasy.wx.pay;

import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.util.CommonUtil;
import com.fantasy.wx.util.MD5SignUtil;
import com.fantasy.wx.util.SHA1Util;

import java.util.*;
import java.net.URLEncoder;


public class WxPayHelper {
    private HashMap<String, String> parameters = new HashMap<String, String>();
    private String AppId = "";
    private String AppKey = "";
    private String SignType = "sha1";
    private String PartnerKey = "";

    public void SetAppId(String str) {
        AppId = str;
    }

    public void SetAppKey(String str) {
        AppKey = str;
    }

    public void SetSignType(String str) {
        SignType = str;
    }

    public void SetPartnerKey(String str) {
        PartnerKey = str;
    }

    public void SetParameter(String key, String value) {
        parameters.put(key, value);
    }

    public String GetParameter(String key) {
        return parameters.get(key);
    }

    private Boolean CheckCftParameters() {
        if (parameters.get("bank_type") == "" || parameters.get("body") == ""
                || parameters.get("partner") == ""
                || parameters.get("out_trade_no") == ""
                || parameters.get("total_fee") == ""
                || parameters.get("fee_type") == ""
                || parameters.get("notify_url") == null
                || parameters.get("spbill_create_ip") == ""
                || parameters.get("input_charset") == "") {
            return false;
        }
        return true;
    }

    public String GetCftPackage() throws SDKRuntimeException {
        if ("" == PartnerKey) {
            throw new SDKRuntimeException("密钥不能为空！");
        }
        String unSignParaString = CommonUtil.FormatBizQueryParaMap(parameters,
                false);
        String paraString = CommonUtil.FormatBizQueryParaMap(parameters, true);
        return paraString + "&sign="
                + MD5SignUtil.Sign(unSignParaString, PartnerKey);

    }

    public String GetBizSign(HashMap<String, String> bizObj)
            throws SDKRuntimeException {
        HashMap<String, String> bizParameters = new HashMap<String, String>();

        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(
                bizObj.entrySet());

        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });

        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String, String> item = infoIds.get(i);
            if (item.getKey() != "") {
                bizParameters.put(item.getKey().toLowerCase(), item.getValue());
            }
        }

        if (AppKey == "") {
            throw new SDKRuntimeException("APPKEY为空！");
        }
        bizParameters.put("appkey", AppKey);
        String bizString = CommonUtil.FormatBizQueryParaMap(bizParameters,
                false);
        //System.out.println(bizString);

        return SHA1Util.Sha1(bizString);

    }

    // 生成app支付请求json
	/*
	 * { "appid":"wwwwb4f85f3a797777", "traceid":"crestxu",
	 * "noncestr":"111112222233333", "package":
	 * "bank_type=WX&body=XXX&fee_type=1&input_charset=GBK&notify_url=http%3a%2f%2f
	 * www
	 * .qq.com&out_trade_no=16642817866003386000&partner=1900000109&spbill_create_ip
	 * =127.0.0.1&total_fee=1&sign=BEEF37AD19575D92E191C1E4B1474CA9",
	 * "timestamp":1381405298,
	 * "app_signature":"53cca9d47b883bd4a5c85a9300df3da0cb48565c",
	 * "sign_method":"sha1" }
	 */
    public String CreateAppPackage(String traceid) throws SDKRuntimeException {
        HashMap<String, String> nativeObj = new HashMap<String, String>();
        if (CheckCftParameters() == false) {
            throw new SDKRuntimeException("生成package参数缺失！");
        }
        nativeObj.put("appid", AppId);
        nativeObj.put("package", GetCftPackage());
        nativeObj.put("timestamp", Long.toString(new Date().getTime()/1000));
        nativeObj.put("traceid", traceid);
        nativeObj.put("noncestr", CommonUtil.CreateNoncestr());
        nativeObj.put("app_signature", GetBizSign(nativeObj));
        nativeObj.put("sign_method", SignType);
        return JSON.serialize(nativeObj);
    }

    // 生成jsapi支付请求json
	/*
	 * "appId" : "wxf8b4f85f3a794e77", //公众号名称，由商户传入 "timeStamp" : "189026618",
	 * //时间戳这里随意使用了一个值 "nonceStr" : "adssdasssd13d", //随机串 "package" :
	 * "bank_type=WX&body=XXX&fee_type=1&input_charset=GBK&notify_url=http%3a%2f
	 * %2fwww.qq.com&out_trade_no=16642817866003386000&partner=1900000109&
	 * spbill_create_i
	 * p=127.0.0.1&total_fee=1&sign=BEEF37AD19575D92E191C1E4B1474CA9",
	 * //扩展字段，由商户传入 "signType" : "SHA1", //微信签名方式:sha1 "paySign" :
	 * "7717231c335a05165b1874658306fa431fe9a0de" //微信签名
	 */
    public String CreateBizPackage() throws SDKRuntimeException {
        HashMap<String, String> nativeObj = new HashMap<String, String>();
        if (CheckCftParameters() == false) {
            throw new SDKRuntimeException("生成package参数缺失！");
        }
        nativeObj.put("appId", AppId);
        nativeObj.put("package", GetCftPackage());
        nativeObj.put("timestamp", Long.toString(new Date().getTime()/1000));
        nativeObj.put("noncestr", CommonUtil.CreateNoncestr());
        nativeObj.put("paySign", GetBizSign(nativeObj));
        nativeObj.put("signType", SignType);
        return JSON.serialize(nativeObj);

    }

    // 生成原生支付url
	/*
	 * weixin://wxpay/bizpayurl?sign=XXXXX&appid=XXXXXX&productid=XXXXXX&timestamp
	 * =XXXXXX&noncestr=XXXXXX
	 */
    public String CreateNativeUrl(String productid) throws SDKRuntimeException {
        String bizString = "";
        try {
            HashMap<String, String> nativeObj = new HashMap<String, String>();
            nativeObj.put("appid", AppId);
            nativeObj.put("productid", URLEncoder.encode(productid, "utf-8"));
            nativeObj.put("timestamp", Long.toString(new Date().getTime()/1000));
            nativeObj.put("noncestr", CommonUtil.CreateNoncestr());
            nativeObj.put("sign", GetBizSign(nativeObj));
            bizString = CommonUtil.FormatBizQueryParaMap(nativeObj, false);

        } catch (Exception e) {
            throw new SDKRuntimeException(e.getMessage());

        }
        return "weixin://wxpay/bizpayurl?" + bizString;
    }

    // 生成原生支付请求xml
	/*
	 * <xml> <AppId><![CDATA[wwwwb4f85f3a797777]]></AppId>
	 * <Package><![CDATA[a=1&url=http%3A%2F%2Fwww.qq.com]]></Package>
	 * <TimeStamp> 1369745073</TimeStamp>
	 * <NonceStr><![CDATA[iuytxA0cH6PyTAVISB28]]></NonceStr>
	 * <RetCode>0</RetCode> <RetErrMsg><![CDATA[ok]]></ RetErrMsg>
	 * <AppSignature><![CDATA[53cca9d47b883bd4a5c85a9300df3da0cb48565c]]>
	 * </AppSignature> <SignMethod><![CDATA[sha1]]></ SignMethod > </xml>
	 */
    public String CreateNativePackage(String retcode, String reterrmsg) throws SDKRuntimeException {
        HashMap<String, String> nativeObj = new HashMap<String, String>();
        if (CheckCftParameters() == false && retcode == "0") {
            throw new SDKRuntimeException("生成package参数缺失！");
        }
        nativeObj.put("AppId", AppId);
        nativeObj.put("Package", GetCftPackage());
        nativeObj.put("TimeStamp", Long.toString(new Date().getTime()/1000));
        nativeObj.put("RetCode", retcode);
        nativeObj.put("RetErrMsg", reterrmsg);
        nativeObj.put("NonceStr", CommonUtil.CreateNoncestr());
        nativeObj.put("AppSignature", GetBizSign(nativeObj));
        nativeObj.put("SignMethod", SignType);
        return CommonUtil.ArrayToXml(nativeObj);

    }
}
