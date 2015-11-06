package com.fantasy.payment.rest.form;

import com.fantasy.framework.util.jackson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

public class PayFormTest {

    private static Log LOG = LogFactory.getLog(PayFormTest.class);

    @Test
    public void testJSON(){
        PayForm payForm = new PayForm();
        payForm.setOrderType("TEST");
        payForm.setOrderSn("20151104001");
        payForm.setPayconfigId(2L);
        payForm.setPayer("test");
        payForm.addParameter("bankNo","ICBCBTB");

        String json = JSON.serialize(payForm);
        LOG.debug(json);

        payForm = JSON.deserialize(json,PayForm.class);

        Assert.assertNotNull(payForm);

        Assert.assertEquals(payForm.getParameter("bankNo"),"ICBCBTB");

    }

}