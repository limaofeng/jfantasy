package org.jfantasy.pay.product;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.pay.bean.ExtProperty;
import org.junit.Test;


public class PayProductSupportTest {

    private final static Log LOG = LogFactory.getLog(PayProductSupportTest.class);

    @Test
    public void getExtPropertys() throws Exception {
        Alipay alipay = new Alipay();
        alipay.setId("alipayDirect");
        alipay.setName("支付宝");
        alipay.setBargainorIdName("合作身份者ID");
        alipay.setBargainorKeyName("安全校验码");
        alipay.setCurrencyTypes(new CurrencyType[]{CurrencyType.CNY});
        alipay.setLogoPath("/template/tocer/images/payment/alipay_direct_icon.gif");
        alipay.setDescription("支付宝即时交易，付款后立即到账，无预付/年费，单笔费率阶梯最低0.7%，无流量限制。 <a href=\"https://www.alipay.com/himalayas/practicality_customer.htm?customer_external_id=C4393933195131654818&market_type=from_agent_contract&pro_codes=61F99645EC0DC4380ADE569DD132AD7A\" target=\"_blank\"><span class=\"red\">立即申请</span></a>");
        alipay.set(AlipayPayProductSupport.EXT_SELLER_EMAIL,ExtProperty.Builder.property(AlipayPayProductSupport.EXT_SELLER_EMAIL, "支付宝账号").build());

        LOG.debug(JSON.serialize(alipay));
    }

    @Test
    public void setExtPropertys() throws Exception {

    }

}