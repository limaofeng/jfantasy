package org.jfantasy.pay.product;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * 银联支付测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class UnionpayTest {

    private final static Log LOG = LogFactory.getLog(UnionpayTest.class);

    private Unionpay unionpay = new Unionpay();

    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private PayConfigService payConfigService;
    private Order order;
    private PayConfig payConfig;
    @Before
    public void setUp() throws Exception {
        // 银联支付 1
        try {
            //上传签名证书
            FileDetail signCert = fileUploadService.upload(new File(PathUtil.classes() + "/certs/app.pfx"), "/certs/app.pfx", "haolue-upload");
            //上传验签证书
            FileDetail validateCert = fileUploadService.upload(new File(PathUtil.classes() + "/certs/acp20151027.cer"), "/certs/acp20151027.cer", "haolue-upload");
            //保存支付配置
            PayConfig payConfig = payConfigService.findUnique(Restrictions.eq("payProductId", "unionpay"), Restrictions.eq("bargainorId", "802310073920500"));
            if (payConfig == null) {
                payConfig = new PayConfig();
            }
            payConfig.setName("正本上工银联支付");
            payConfig.setPayConfigType(PayConfig.PayConfigType.online);
            payConfig.setPayProductId("unionpay");
            payConfig.setPayFeeType(PayConfig.PayFeeType.fixed);
            payConfig.setPayFee(BigDecimal.ZERO);
            payConfig.setBargainorId("802310073920500");
            payConfig.setBargainorKey("201601");
            payConfig.setSignCert(signCert);
            payConfig.setValidateCert(validateCert);
            payConfigService.save(this.payConfig = payConfig);
            unionpay.setDeployStatus(DeployStatus.Production);
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
//        this.payConfig = payConfigService.findUnique(Restrictions.eq("payProductId", "unionpay"), Restrictions.eq("bargainorId", "777290058123224"));
//        unionpay.setDeployStatus(DeployStatus.Develop);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testApp() throws Exception {

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

        Payment payment = new Payment();
        payment.setSn("T00000007");
        payment.setPayConfig(this.payConfig);
        payment.setTotalAmount(BigDecimal.valueOf(0.01));
        payment.setCreateTime(DateUtil.parse("20160128131252","yyyyMMddHHmmss"));

        Object result = unionpay.app(payment,order);

        LOG.debug(result);

    }

    @Test
    public void testQuery() throws Exception {
        Payment payment = new Payment();
        payment.setSn("T00000006");
        payment.setPayConfig(this.payConfig);
        payment.setTotalAmount(BigDecimal.valueOf(0.01));
        payment.setCreateTime(DateUtil.parse("20160128131252","yyyyMMddHHmmss"));

        String result = unionpay.query(payment);

        LOG.debug(result);
    }

    public static void main(String[] args) throws CertificateException, FileNotFoundException {
        for(File file : new File(PathUtil.classes() + "/certs/").listFiles()){
            if(!file.getName().endsWith(".cer")){
                continue;
            }
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate validateCert = (X509Certificate) cf.generateCertificate(new FileInputStream(file));
            System.out.println(validateCert.getSerialNumber().toString()+"==>"+file.getAbsolutePath());
        }
    }

    @Test
    public void testPayNotify() throws Exception {
        String body = "sign=FmKMECtwoSlT/71rmfHBBexR+SaIGVlYlcyFJJ2jlOqO98M0o8OlQQeYmiC2oIXMWv5/pt1uJ4rX3Jh/J+RsxLLT73QHsMFz78oySnngz2aA/8EgZnbuM28+/sDRnui2ShtB54TxFDwfNxWEyuj8eJQ05RlhDfN5VezflozZOCAMVIaUf3vsTLgtLI9F4LNekFxQupGsyPLh59kAsf/p5lq13fBDETg0jbpHidctra7R9Be4hGhKLBSAk62GHuLlmhXp+xwf889Gf2C2kJg8r8cG8RCvzyXHal6+PUR7smtjvNJAKBIslTnoViVwOqgAhhk7RLOcbtXqxfr5s43VIA==&data="+ URLEncoder.encode("pay_result=success&tn=201601281312525696898&cert_id=69597475696","utf-8");

        Payment payment = new Payment();
        payment.setSn("T00000006");
        payment.setPayConfig(this.payConfig);
        payment.setTotalAmount(BigDecimal.valueOf(0.01));
        payment.setCreateTime(DateUtil.parse("20160128131252","yyyyMMddHHmmss"));

        payment = unionpay.payNotify(payment,body);

        LOG.debug(payment);

    }
}