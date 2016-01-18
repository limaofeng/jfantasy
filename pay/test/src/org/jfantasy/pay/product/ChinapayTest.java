package org.jfantasy.pay.product;

import org.hibernate.criterion.Restrictions;
import org.jfantasy.file.bean.FileDetail;
import org.jfantasy.file.service.FileUploadService;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.PathUtil;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
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
import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class ChinapayTest {

    private Chinapay chinapay = new Chinapay();

    private Order order;
    private Payment payment;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private PayConfigService payConfigService;

    @Before
    public void setUp() throws Exception {
        //上传签名证书
        FileDetail signCert = fileUploadService.upload(new File(PathUtil.classes() + "/certs/trade.pfx"), "/certs/trade.pfx", "haolue-upload");
        //上传验签证书
        FileDetail validateCert = fileUploadService.upload(new File(PathUtil.classes() +"/certs/cp.cer"), "/certs/cp.cer", "haolue-upload");
        //保存支付配置
        PayConfig payConfig = payConfigService.findUnique(Restrictions.eq("payProductId", "chinapay"), Restrictions.eq("bargainorId", "739211601040001"));
        if (payConfig == null) {
            payConfig = new PayConfig();
        }
        payConfig.setName("正本上工银联电子支付");
        payConfig.setPayConfigType(PayConfig.PayConfigType.online);
        payConfig.setPayProductId("chinapay");
        payConfig.setPayFeeType(PayConfig.PayFeeType.fixed);
        payConfig.setPayFee(BigDecimal.ZERO);
        payConfig.setBargainorId("739211601040001");
        payConfig.setBargainorKey("Zbsg2307");
        payConfig.setSignCert(signCert);
        payConfig.setValidateCert(validateCert);
        payConfigService.save(payConfig);
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
        //测试支付订单
        payment = new Payment();
        payment.setPayConfig(payConfig);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testWeb() throws Exception {
        System.out.println(chinapay.web(order, payment));
    }

    @Test
    public void testWap() throws Exception {

    }

    @Test
    public void testApp() throws Exception {

    }

    @Test
    public void testAsyncNotify() throws Exception {

    }

    @Test
    public void testSyncNotify() throws Exception {

    }

}