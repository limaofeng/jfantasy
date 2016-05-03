package org.jfantasy.pay.product;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.PathUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.order.entity.OrderDetails;
import org.jfantasy.pay.product.sign.SignUtil;
import org.jfantasy.pay.service.PayConfigService;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Map;

public class ChinapayTest {

    private static final Log LOG = LogFactory.getLog(SignUtil.class);

    private Chinapay chinapay = new Chinapay();

    private OrderDetails order;
    private Payment payment = new Payment(){
        {
            this.setSn("P0000001");
        }
    };
    @Autowired
    private PayConfigService payConfigService;

    public void setUp() throws Exception {
        //添加支付配置
        // 银联支付 1
            //上传签名证书
//            FileDetail signCert = fileUploadService.upload(new File(PathUtil.classes() + "/certs/trade.pfx"), "/certs/trade.pfx", "haolue-upload");
            //上传验签证书
//            FileDetail validateCert = fileUploadService.upload(new File(PathUtil.classes() + "/certs/cp.cer"), "/certs/cp.cer", "haolue-upload");
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
            /*
            payConfig.setSignCert(signCert);
            payConfig.setValidateCert(validateCert);
            payConfigService.save(payConfig);
            */
        // 银联支付 2
        //上传签名证书
//            FileDetail signCert = fileUploadService.upload(new File(PathUtil.classes() + "/certs/tradebc.pfx"), "/certs/tradebc.pfx", "haolue-upload");
        //上传验签证书
//            FileDetail validateCert = fileUploadService.upload(new File(PathUtil.classes() + "/certs/cp.cer"), "/certs/cp.cer", "haolue-upload");
        //保存支付配置
        payConfig = payConfigService.findUnique(Restrictions.eq("payProductId", "chinapay"), Restrictions.eq("bargainorId", "739211601120001"));
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
//            payConfig.setSignCert(signCert);
//            payConfig.setValidateCert(validateCert);
        payConfigService.save(payConfig);
        payment.setPayConfig(payConfig);
        //测试订单
        order = new OrderDetails();
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
        String result = "TranType=0001&OrderStatus=0000&TranDate=20160425&AcqDate=20160425&CurryNo=CNY&MerOrderNo=P2016042500084&OrderAmt=100&Signature=I6ASMRMJAa1brk2z8LfUFvjdrwp6e+49ETHpesF+b34e46jch0NfJcFTEkmz1lFWA2b+6ZwIkJYINOcJGZj7S60%2FIcXHfcVZ1jE3gJzPYsffYCzjOgb7mLPdT6c3VP8ytpEZ0ChhefRJC+rP+jBQ8u3xdoKh2gFG%2F+N8yQE1hbMI%3D&BusiType=0001&CompleteTime=175645&BankInstNo=700000000000017&AcqSeqId=0000000009161076&MerId=739211601040001&TranTime=175607&Version=20140728&CompleteDate=20160425";

        Payment payment = new Payment();
        payment.setSn("T00000006");
        payment.setPayConfig(payConfigService.findUnique(Restrictions.eq("payProductId", "chinapay"), Restrictions.eq("bargainorId", "739211601040001")));
        payment.setTotalAmount(BigDecimal.valueOf(0.01));
        payment.setCreateTime(DateUtil.parse("20160128131252","yyyyMMddHHmmss"));

        chinapay.payNotify(payment,result);
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

        chinapay.refund(refund);

    }

    @Test
    public void signature() throws Exception {

    }

    @Test
    public void verify() throws Exception {
        String signature = "P7kxnMLbEJ7VVqd5FFdPdwHx8v070UeIVLUHQdeTVUoSEBJwBBMZ1rx9dMuOAmxzuV90mtN7A0J7Dv5LWUUgpuQcL3FCTWNxb2fnX+APObmVnjv0sta7/gycBdRKNw6Od5M3W3eVHXqvAGu/vzk+ TtUbuZZ3j8gISJcJK9wBAvQ=";
        String result = "TranType=0001&OrderStatus=0000&TranDate=20160426&AcqDate=20160426&CurryNo=CNY&MerOrderNo=P2016042600001&OrderAmt=2000&Signature=P7kxnMLbEJ7VVqd5FFdPdwHx8v070UeIVLUHQdeTVUoSEBJwBBMZ1rx9dMuOAmxzuV90mtN7A0J7Dv5LWUUgpuQcL3FCTWNxb2fnX+APObmVnjv0sta7%2FgycBdRKNw6Od5M3W3eVHXqvAGu%2Fvzk++TtUbuZZ3j8gISJcJK9wBAvQ%3D&BusiType=0001&CompleteTime=103149&BankInstNo=700000000000017&AcqSeqId=0000000009174980&MerId=739211601040001&TranTime=103123&Version=20140728&CompleteDate=20160426";
        Map<String,String> data  = SignUtil.parseQuery(result,true);


        String resourcesPath = PathUtil.class.getResource("/application.properties").toExternalForm().replaceAll("^file:","").replaceAll("/application\\.properties$","");

//        FileItem.Metadata metadata = new FileItem.Metadata();
//        PublicKey publicKey = CertUtil.loadPublicKey(new LocalFileManager.LocalFileItem(new LocalFileManager(resourcesPath),new File(resourcesPath + "/certs/cp.cer"), metadata));

//        LOG.debug(SecureUtil.verify(SignUtil.coverMapString(data, "Signature", "CertId").getBytes("UTF-8"), SignUtil.decodeBase64(signature,"UTF-8"), publicKey, "SHA512WithRSA"));
    }

}