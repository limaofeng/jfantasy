package org.jfantasy.pay.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jfantasy.framework.httpclient.HttpClientUtil;
import org.jfantasy.framework.httpclient.Response;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.framework.util.HandlebarsTemplateUtils;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.pay.order.entity.enums.PaymentStatus;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.sign.SignUtil;

import java.io.IOException;
import java.util.*;

/**
 * 支付宝（即时交易）
 */
public class Alipay extends AlipayPayProductSupport {

    private Urls urls = new Urls() {
        {
            this.paymentUrl = "https://mapi.alipay.com/gateway.do?_input_charset=" + input_charset;// 支付请求URL
            this.refundUrl = "https://mapi.alipay.com/gateway.do";//统一收单交易退款接口
        }
    };

    private final List<BankCode> creditBankCodes = new LinkedList<BankCode>();
    private final List<BankCode> debitBankCodes = new LinkedList<BankCode>();

    {
        //银行简码——混合渠道
        creditBankCodes.add(new BankCode("ICBCBTB", "中国工商银行（B2B）"));
        creditBankCodes.add(new BankCode("ABCBTB", "中国农业银行（B2B）"));
        creditBankCodes.add(new BankCode("CCBBTB", "中国建设银行（B2B）"));
        creditBankCodes.add(new BankCode("SPDBB2B", "上海浦东发展银行（B2B）"));
        creditBankCodes.add(new BankCode("BOCBTB", "中国银行（B2B）"));
        creditBankCodes.add(new BankCode("CMBBTB", "招商银行（B2B）"));
        creditBankCodes.add(new BankCode("BOCB2C", "中国银行"));
        creditBankCodes.add(new BankCode("ICBCB2C", "中国工商银行"));
        creditBankCodes.add(new BankCode("CMB", "招商银行"));
        creditBankCodes.add(new BankCode("CCB", "中国建设银行"));
        creditBankCodes.add(new BankCode("ABC", "中国农业银行"));
        creditBankCodes.add(new BankCode("SPDB", "上海浦东发展银行"));
        creditBankCodes.add(new BankCode("CIB", "兴业银行"));
        creditBankCodes.add(new BankCode("GDB", "广发银行"));
        creditBankCodes.add(new BankCode("CMBC", "中国民生银行"));
        creditBankCodes.add(new BankCode("CITIC", "中信银行"));
        creditBankCodes.add(new BankCode("HZCBB2C", "杭州银行"));
        creditBankCodes.add(new BankCode("CEBBANK", "中国光大银行"));
        creditBankCodes.add(new BankCode("SHBANK", "上海银行"));
        creditBankCodes.add(new BankCode("NBBANK", "宁波银行"));
        creditBankCodes.add(new BankCode("SPABANK", "平安银行"));
        creditBankCodes.add(new BankCode("BJRCB", "北京农村商业银行"));
        creditBankCodes.add(new BankCode("FDB", "富滇银行"));
        creditBankCodes.add(new BankCode("POSTGC", "中国邮政储蓄银行"));
        creditBankCodes.add(new BankCode("abc1003", "visa"));
        creditBankCodes.add(new BankCode("abc1004", "master"));
        //银行简码——纯借记卡渠道
        debitBankCodes.add(new BankCode("CMB-DEBIT", "招商银行"));
        debitBankCodes.add(new BankCode("CCB-DEBIT", "中国建设银行"));
        debitBankCodes.add(new BankCode("ICBC-DEBIT", "中国工商银行"));
        debitBankCodes.add(new BankCode("COMM-DEBIT", "交通银行"));
        debitBankCodes.add(new BankCode("GDB-DEBIT", "广发银行"));
        debitBankCodes.add(new BankCode("BOC-DEBIT", "中国银行"));
        debitBankCodes.add(new BankCode("CEB-DEBIT", "中国光大银行"));
        debitBankCodes.add(new BankCode("SPDB-DEBIT", "上海浦东发展银行"));
        debitBankCodes.add(new BankCode("PSBC-DEBIT", "中国邮政储蓄银行"));
        debitBankCodes.add(new BankCode("BJBANK", "北京银行"));
        debitBankCodes.add(new BankCode("SHRCB", "上海农商银行"));
        debitBankCodes.add(new BankCode("WZCBB2C-DEBIT", "温州银行"));
        debitBankCodes.add(new BankCode("COMM", "交通银行"));
    }

    @Override
    public String web(Payment payment, Order order, Properties properties) throws PayException {
        PayConfig config = payment.getPayConfig();
        final Map<String, String> data = new TreeMap<String, String>();
        try {
            // 常规参数
            data.put("service", "create_direct_pay_by_user");// 接口类型（create_direct_pay_by_user：即时交易）
            data.put("_input_charset", input_charset);//字符编码格式
            data.put("payment_type", "1");// 支付类型（固定值：1）
            data.put("notify_url", SettingUtil.getServerUrl() + "/pays/" + payment.getSn() + "/notify");// 消息通知URL

            //data.put("anti_phishing_key", "");防钓鱼时间戳
            //data.put("exter_invoke_ip", "");客户端的IP地址

            data.put("partner", config.getBargainorId());// 合作身份者ID
            data.put("seller_id", config.getBargainorId());// 商家ID

            data.put("out_trade_no", payment.getSn());// 支付编号
            data.put("subject", order.getSubject());// 订单的名称、标题、关键字等
            data.put("body", order.getBody());// 订单描述
            data.put("total_fee", RMB_YUAN_FORMAT.format(order.getPayableFee()));// 总金额（单位：元）

            //额外参数
            if (StringUtil.isNotBlank(properties.getProperty("backUrl"))) {
                data.put("return_url", properties.getProperty("backUrl"));//同步通知
                LOG.debug("添加参数 return_url = " + data.get("MerPageUrl"));
            }
            if (StringUtil.isNotBlank(properties.getProperty("showUrl"))) {
                data.put("show_url", properties.getProperty("showUrl"));// 商品显示URL
                LOG.debug("添加参数 show_url = " + data.get("showUrl"));
            }
            if (StringUtil.isNotBlank(properties.getProperty("extra_common_param"))) {
                data.put("extra_common_param", properties.getProperty("extra_common_param"));
                LOG.debug("添加参数 extra_common_param = " + data.get("extra_common_param"));
            }
            if (StringUtil.isNotBlank(properties.getProperty("bankNo"))) {
                data.put("defaultbank", properties.getProperty("bankNo"));// 默认选择银行（当paymethod为bankPay时有效）
                LOG.debug("添加参数 defaultbank = " + data.get("defaultbank"));
                data.put("paymethod", "bankPay");// 默认支付方式（bankPay：网银、cartoon：卡通、directPay：余额、CASH：网点支付）
            } else {
                data.put("paymethod", "directPay");
            }

            // 参数处理
            data.put("sign_type", "MD5");
            data.put("sign", sign(data, config.getBargainorKey()));

            data.put("subject", WebUtil.transformCoding(order.getSubject(), "utf-8", "utf-8"));// 订单的名称、标题、关键字等
            data.put("body", WebUtil.transformCoding(order.getBody(), "utf-8", "utf-8"));// 订单描述

            //拼接支付表单
            return HandlebarsTemplateUtils.processTemplateIntoString(HandlebarsTemplateUtils.template("/org.jfantasy.pay.product.template/pay"), new HashMap<String, Object>() {
                {
                    this.put("url", urls.paymentUrl);
                    this.put("params", data.entrySet());
                }
            });
        } catch (IOException e) {
            throw new PayException("支付过程发生错误，错误信息为:" + e.getMessage());
        }
    }

    public String payNotify(Payment payment, String result) throws PayException {
        PayConfig config = payment.getPayConfig();

        Map<String, String> privateKeys = new HashMap<String, String>();
        privateKeys.put("MD5", config.getBargainorKey());
        privateKeys.put("RSA", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB");

        try {
            Map<String, String> appdata = new HashMap<String, String>();
            if (result.contains("result")) {//为手机同步支付通知
                if (RegexpUtil.isMatch(result, "^\\{(.*)\\}$")) { // ios 先去掉 首尾 的括号
                    result = RegexpUtil.parseGroup(result, "^\\{(.*)\\}$", 1);
                }
                for (String _ps : result.split(";")) {
                    String key = _ps.substring(0, _ps.indexOf("="));
                    String value = _ps.substring(_ps.indexOf("=") + 1);
                    if (RegexpUtil.isMatch(value, "^\\{(.*)\\}$")) {
                        appdata.put(key, RegexpUtil.parseGroup(value, "^\\{(.*)\\}$", 1));
                    } else {
                        appdata.put(key, value);
                    }
                }
            }

            Map<String, String> data = SignUtil.parseQuery(appdata.isEmpty() ? result : appdata.get("result"), appdata.isEmpty());
            if (appdata.isEmpty()) {
                data.put("sign", data.get("sign").replaceAll(" ", "+"));//编码的时候可能会将签名中的 '+' => '' 所以再次转换回来 * 不能使用 encodeURI 方法
                data.put("notify_id", StringUtil.encodeURI(data.get("notify_id"), input_charset));//编码的时候可能会将通知ID中的  %2F => 和 %2B => +  所以再次转换回来
            }

            if (!verifyNotifyId(config.getBargainorId(), data.get("notify_id"))) {
                throw new RestException("支付宝 notify_id 验证失败");
            }
            //验证签名
            if (!verify(data, privateKeys.get(data.get("sign_type")))) {
                throw new RestException("支付宝返回的响应签名错误");
            }
            //记录交易流水
            payment.setTradeNo(data.get("trade_no"));
            if ("WAIT_BUYER_PAY".equals(data.get("trade_status"))) {//交易创建，等待买家付款。
                //TODO 如果有支付过期定时器的话,现在可以启动了
            } else if ("TRADE_SUCCESS".equals(data.get("trade_status"))) {//交易成功，且可对该交易做操作，如：多级分润、退款等。
                payment.setStatus(PaymentStatus.success);
            } else if ("TRADE_FINISHED".equals(data.get("trade_status"))) {//交易成功且结束，即不可再做任何操作。
                payment.setStatus(PaymentStatus.finished);
            } else if ("TRADE_CLOSED".equals(data.get("trade_status"))) {//在指定时间段内未支付时关闭的交易；or 在交易完成全额退款成功时关闭的交易。
                payment.setStatus(PaymentStatus.close);
            }
            return data.containsKey("gmt_payment") ? "success" : null;
        } finally {
            this.log("in", "notify", payment, config, result);
        }
    }

    @Override
    public Refund refund(Refund refund) {
        PayConfig config = refund.getPayConfig();
        Payment payment = refund.getPayment();

        Map<String, String> data = new TreeMap<String, String>();
        try {
            data.put("service", "refund_fastpay_by_platform_pwd");
            data.put("partner", config.getBargainorId());
            data.put("_input_charset", input_charset);
            data.put("sign_type", "MD5");
            data.put("notify_url", SettingUtil.getServerUrl() + "/pays/" + refund.getSn() + "/notify");
            data.put("seller_email", config.get(EXT_SELLER_EMAIL,String.class));
            data.put("refund_date", DateUtil.format(refund.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            data.put("batch_no", refund.getSn());
            data.put("batch_num", "1");
            data.put("detail_data", payment.getSn() + "^" + RMB_YUAN_FORMAT.format(refund.getTotalAmount()) + "^none");

            data.put("sign", sign(data, config.getBargainorKey()));

            Response response = HttpClientUtil.doPost(urls.refundUrl, data);

            System.out.println(response.getBody());

            throw new PayException("暂不支持支付宝退款!");

            /*
            //公共请求参数

            data.put("app_id","2016041201289130");//支付宝分配给开发者的应用Id
            data.put("method","alipay.trade.refund");
            data.put("charset",input_charset);
            data.put("sign_type","RSA");
            data.put("timestamp", DateUtil.format("yyyy-MM-dd HH:mm:ss"));
            data.put("version","1.0");//调用的接口版本，固定为：1.0
            //data.put("app_auth_token","");//详见应用授权概述

            //请求参数
            Map<String,String> bizcontent = new TreeMap<String, String>();
            bizcontent.put("trade_no",payment.getTradeNo());//支付宝交易号
            bizcontent.put("out_trade_no",payment.getSn());//商户订单号
            bizcontent.put("refund_amount",RMB_YUAN_FORMAT.format(refund.getTotalAmount()));//需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
            //bizcontent.put("refund_reason","");//退款的原因说明
            bizcontent.put("out_request_no",refund.getSn());//标识一次退款请求，同一笔交易多次退款需要保证唯一
            //bizcontent.put("operator_id","");//商户的操作员编号
            //bizcontent.put("store_id","");//商户的门店编号
            //bizcontent.put("terminal_id","");//商户的终端编号

            data.put("biz_content",JSON.serialize(bizcontent));

            String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANesnTGjdj/aEtnZ\n" +
                    "P39jKpVEEj7cuvwc4DHJbmGpyKti235ejE9V8h2puTqX3Xof5UidjFNwiHChFZmi\n" +
                    "+EU8i8OQK09PR4OALhg9TdSBWF+mwLpJ5LeHYEXXUkWwWtK94IW12K/n4aEEVtRZ\n" +
                    "rfa9REKrclCATnT5v/Qspqp86oNdAgMBAAECgYEAoOyHDfat0M7iqfHT0zUnHOEB\n" +
                    "zC3exya0kfF+jxikRl0o8Y2Sm8/BLCjrsLCH7QvHhPspLUkWRROsjkpvfRnEHfTY\n" +
                    "d3NeccKW2YuFmcL2U7J3AHvdghSR8IE2sTA8CRorCMeS0FjHc+zgJTOIalrzgDjU\n" +
                    "U4C6KRGaSuCggy0wp4ECQQDsAkKQoLDN7Ig2D8KkhR3C9R4VgXgJBwZjJYQLHE2Q\n" +
                    "lMPTp0TnHgJJfJFcec2kM96gIULwlsoiXNLWp313GUMFAkEA6fFop4CUVFrWWw01\n" +
                    "WqhLSb3Q1aQ83XYOf7eku2SgEv5jEkZmpkJu5k3dSCRcgriXPhLw7hFIvEW6Gpy2\n" +
                    "uEZeeQJBAM0LzZ9wLQxMI6+sk7RyfwACDIgsuwhE1TTQxF8O0Qj7ZwP9gKy38s67\n" +
                    "7mME5Dh0ZEiFfW4f5DBkqz2ZuTT/eq0CQDLK1DMR6qKJ+mJYcs4VHguLp8zK1OAs\n" +
                    "Yqd+IskA5vRYwP/VwzGz2Mot+65PHrrPAx9aE29M12LxLJ/ciJtnw9kCQEVvWtNU\n" +
                    "CDLaWnH4SxTG7P9OsfNWgz//O7eT69wsGoFHb2PeN9XVDBQbr7SomrGeYa8SwqCS\n" +
                    "rU4/l+JjAg+bR7o=";

            data.put("sign",sign(data,privateKey));

            Response response = HttpClientUtil.doPost(urls.refundUrl,data);

            System.out.println(response.getBody());

            //解析数据

            //判断业务处理是否成功
            if (!"SUCCESS".equalsIgnoreCase(data.get("return_code"))) {
                throw new RestException(data.get("return_msg"));
            }

            //验证签名
            if (!verify(data, config.getBargainorKey())) {
                throw new RestException("微信返回的响应签名错误");
            }

            if ("FAIL".equals(data.get("result_code"))) {
                throw new RestException("[" + data.get("err_code") + "]" + data.get("err_code_des"));
            }

            refund.setStatus(Refund.Status.wait);
            refund.setTradeNo(data.get("refund_id"));

            */
//            return refund;
        } catch (IOException e) {
            throw new RestException("调用支付宝接口,网络错误!");
        } catch (PayException e) {
            throw new RestException(e.getMessage());
        }
    }

    /**
     * 支付宝网银直连接口已下线
     *
     * @return List<BankCode>
     */
    @JsonIgnore
    @Deprecated
    public List<BankCode> getCreditBankCodes() {
        return creditBankCodes;
    }

    /**
     * 支付宝网银直连接口已下线
     *
     * @return List<BankCode>
     */
    @JsonIgnore
    @Deprecated
    public List<BankCode> getDebitBankCodes() {
        return debitBankCodes;
    }

    @Override
    public Refund payNotify(Refund refund, String result) throws PayException {
        return null;
    }

    public static class BankCode {
        private String code;
        private String name;

        public BankCode(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

    }

    class Urls {
        /**
         * 支付接口
         */
        String paymentUrl;
        /**
         * 退款接口
         */
        String refundUrl;
    }

}