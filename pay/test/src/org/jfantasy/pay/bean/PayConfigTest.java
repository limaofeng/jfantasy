package org.jfantasy.pay.bean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.pay.product.AlipayPayProductSupport;
import org.junit.Assert;
import org.junit.Test;


public class PayConfigTest {

    private final static Log LOG = LogFactory.getLog(PayConfigTest.class);

    private final static String JSON_DATA = "{\"id\":-9223372036854775808,\"name\":\"支付宝\",\"bargainorId\":\"0123123\",\"bargainorKey\":\"123123123123\",\"sellerEmail\":\"limaofeng@msm.com\"}";

    private PayConfig payConfig = new PayConfig();
    {
        payConfig.setId(Long.MIN_VALUE);
        payConfig.setName("支付宝");
        payConfig.setBargainorId("0123123");
        payConfig.setBargainorKey("123123123123");
        payConfig.set(AlipayPayProductSupport.EXT_SELLER_EMAIL,"limaofeng@msm.com");
    }

    @Test
    public void getProperties() throws Exception {
        String json = JSON.serialize(payConfig);
        LOG.debug(json);

        Assert.assertEquals(JSON_DATA,json);
    }

    @Test
    public void setProperties() throws Exception {
        PayConfig config = JSON.deserialize(JSON_DATA,PayConfig.class);

        assert config != null;
        Assert.assertEquals(config.getProperties().get(AlipayPayProductSupport.EXT_SELLER_EMAIL),"limaofeng@msm.com");
    }

}