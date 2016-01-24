package org.jfantasy.pay.product;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.file.bean.FileDetail;
import org.jfantasy.file.service.FileUploadService;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.PathUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.product.order.Order;
import org.jfantasy.pay.product.order.OrderItem;
import org.jfantasy.pay.product.order.ShipAddress;
import org.jfantasy.pay.service.PayConfigService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class ChinapayTest {

    private Logger LOG = Logger.getLogger(ChinapayTest.class);

    private Chinapay chinapay = new Chinapay();

    private Order order;
    private Payment payment = new Payment(){
        {
            this.setSn("P0000001");
        }
    };
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private PayConfigService payConfigService;

    @Before
    public void setUp() throws Exception {
        //添加支付配置
        // 银联支付 1
        try {
            //上传签名证书
            FileDetail signCert = fileUploadService.upload(new File(PathUtil.classes() + "/certs/trade.pfx"), "/certs/trade.pfx", "haolue-upload");
            //上传验签证书
            FileDetail validateCert = fileUploadService.upload(new File(PathUtil.classes() + "/certs/cp.cer"), "/certs/cp.cer", "haolue-upload");
            //保存支付配置
            PayConfig payConfig = payConfigService.findUnique(Restrictions.eq("payProductId", "chinapay"), Restrictions.eq("bargainorId", "739211601040001"));
            if (payConfig == null) {
                payConfig = new PayConfig();
            }
            payConfig.setName("正本上工银联电子支付 - 银联在线");
            payConfig.setPayConfigType(PayConfig.PayConfigType.online);
            payConfig.setPayProductId("chinapay");
            payConfig.setPayFeeType(PayConfig.PayFeeType.fixed);
            payConfig.setPayFee(BigDecimal.ZERO);
            payConfig.setBargainorId("739211601040001");
            payConfig.setBargainorKey("Zbsg2307");
            payConfig.setSignCert(signCert);
            payConfig.setValidateCert(validateCert);
            payConfigService.save(payConfig);
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        // 银联支付 2
        try {
            //上传签名证书
            FileDetail signCert = fileUploadService.upload(new File(PathUtil.classes() + "/certs/tradebc.pfx"), "/certs/tradebc.pfx", "haolue-upload");
            //上传验签证书
            FileDetail validateCert = fileUploadService.upload(new File(PathUtil.classes() + "/certs/cp.cer"), "/certs/cp.cer", "haolue-upload");
            //保存支付配置
            PayConfig payConfig = payConfigService.findUnique(Restrictions.eq("payProductId", "chinapay"), Restrictions.eq("bargainorId", "739211601120001"));
            if (payConfig == null) {
                payConfig = new PayConfig();
            }
            payConfig.setName("正本上工银联电子支付 - 网银支付");
            payConfig.setPayConfigType(PayConfig.PayConfigType.online);
            payConfig.setPayProductId("chinapay");
            payConfig.setPayFeeType(PayConfig.PayFeeType.fixed);
            payConfig.setPayFee(BigDecimal.ZERO);
            payConfig.setBargainorId("739211601120001");
            payConfig.setBargainorKey("Zbsg2307");
            payConfig.setSignCert(signCert);
            payConfig.setValidateCert(validateCert);
            payConfigService.save(payConfig);
            payment.setPayConfig(payConfig);
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        //测试订单
        order = new Order() {
            @Override
            public String getSN() {
                return "TEST" + DateUtil.format("yyyyMMddHHmmss");
            }

            @Override
            public String getType() {
                return null;
            }

            @Override
            public String getSubject() {
                return null;
            }

            @Override
            public BigDecimal getTotalFee() {
                return null;
            }

            @Override
            public BigDecimal getPayableFee() {
                return null;
            }

            @Override
            public boolean isPayment() {
                return false;
            }

            @Override
            public List<OrderItem> getOrderItems() {
                return null;
            }

            @Override
            public ShipAddress getShipAddress() {
                return null;
            }
        };
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testWeb() throws Exception {
//        System.out.println(chinapay.web(order, payment, new Properties()));
    }

    @Test
    public void testWap() throws Exception {

    }

    @Test
    public void testApp() throws Exception {

    }

    @Test
    public void testAsyncNotify() throws Exception {
        MDC.put("usr_id", "123");
        MDC.put("log_title", "网站访问记录");
        MDC.put("log_type", "记录");
        MDC.put("log_title", "网站访问记录");
        MDC.put("log_datetime", "123");
        MDC.put("log_ip", "");
        LOG.info(MDC.getContext());
    }

    @Test
    public void testSyncNotify() throws Exception {

    }

    @Test
    public void testQuery() throws Exception {
        Payment payment = new Payment();
        payment.setSn("P2016012100064");
        payment.setPayConfig(payConfigService.get(2L));
        payment.setCreateTime(DateUtil.parse("2016-01-21 14:32:30","yyyy-MM-dd HH:mm:ss"));

        String text = chinapay.query(payment);


        Map<String,String> data = WebUtil.parseQuery(text,true);

//        SecssUtil secssUtil = new SecssUtil();
//
//        secssUtil.init(PropertiesHelper.load("props/chinapay.properties").getProperties());
//
//        secssUtil.verify(data);

        //SecssUtil.cpDecryptData(SignUtil.coverMapString(data, "Signature", "CertId"));

    }


    @Test
    public void testRefund() throws Exception {
        Payment payment = new Payment();
        payment.setSn("P2016012500089");
        payment.setPayConfig(payConfigService.get(2L));
        payment.setCreateTime(DateUtil.parse("2016-01-25 04:18:01","yyyy-MM-dd HH:mm:ss"));

        Refund refund = new Refund();
        refund.setSn("RP20160125000891");
        refund.setPayConfig(payment.getPayConfig());
        refund.setTotalAmount(BigDecimal.valueOf(0.01));
        refund.setCreateTime(DateUtil.parse("2016-01-25 04:32:30","yyyy-MM-dd HH:mm:ss"));

        String text = chinapay.refund(refund,payment);

    }

}