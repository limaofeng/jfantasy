package com.fantasy.wx.qrcode.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.qrcode.bean.QRCode;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.ArrayList;

public class QRCodeServiceTest {
    private static final Log logger = LogFactory.getLog(QRCode.class);
    @Resource
    private QRCodeService qrCodeService;
    @Before
    public void setUp() throws Exception {
        testQrCodeCreateTmpTicket();
    }

    @After
    public void tearDown() throws Exception {
        Pager<QRCode> pager=qrCodeService.findPager(new Pager<QRCode>(),new ArrayList<PropertyFilter>());
        qrCodeService.delete(pager.getPageItems().get(0).getId());
    }

    @Test
    public void testGetQRCode() throws Exception {
        Pager<QRCode> pager=qrCodeService.findPager(new Pager<QRCode>(),new ArrayList<PropertyFilter>());
        QRCode code=qrCodeService.getQRCode(pager.getPageItems().get(0).getId());
        Assert.assertNotNull(code);
        logger.debug(JSON.serialize(code));
    }

    @Test
    public void testQrCodeCreateTmpTicket() throws Exception {
        QRCode code=qrCodeService.qrCodeCreateTmpTicket();
        Assert.assertNotNull(code.getId());
    }

    @Test
    public void testQrCodeCreateLastTicket() throws Exception {
        QRCode code=qrCodeService.qrCodeCreateLastTicket();
        Assert.assertNotNull(code.getId());
    }
}