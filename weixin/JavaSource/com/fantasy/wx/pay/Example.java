package com.fantasy.wx.pay;

import com.fantasy.wx.pay.WxPayHelper;
import com.fantasy.wx.util.CommonUtil;

public class Example {
    public static void main(String args[]) {
        try {
            WxPayHelper wxPayHelper = new WxPayHelper();
            //先设置基本信息
            wxPayHelper.SetAppId("wxf8b4f85f3a794e77");
            wxPayHelper.SetAppKey("2Wozy2aksie1puXUBpWD8oZxiD1DfQuEaiC7KcRATv1Ino3mdopKaPGQQ7TtkNySuAmCaDCrw4xhPY5qKTBl7Fzm0RgR3c0WaVYIXZARsxzHV2x7iwPPzOz94dnwPWSn");
            wxPayHelper.SetPartnerKey("8934e7d15453e97507ef794cf7b0519d");
            wxPayHelper.SetSignType("sha1");
            //设置请求package信息
            wxPayHelper.SetParameter("bank_type", "WX");
            wxPayHelper.SetParameter("body", "test");
            wxPayHelper.SetParameter("partner", "1900000109");
            wxPayHelper.SetParameter("out_trade_no", CommonUtil.CreateNoncestr());
            wxPayHelper.SetParameter("total_fee", "1");
            wxPayHelper.SetParameter("fee_type", "1");
            wxPayHelper.SetParameter("notify_url", "htttp://www.baidu.com");
            wxPayHelper.SetParameter("spbill_create_ip", "127.0.0.1");
            wxPayHelper.SetParameter("input_charset", "UTF-8");

            System.out.println("生成app支付package:");
            System.out.println(wxPayHelper.CreateAppPackage("test"));
            System.out.println("生成jsapi支付package:");
            System.out.println(wxPayHelper.CreateBizPackage());
            System.out.println("生成原生支付url:");
            System.out.println(wxPayHelper.CreateNativeUrl("abc"));
            System.out.println("生成原生支付package:");
            System.out.println(wxPayHelper.CreateNativePackage("0", "ok"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
