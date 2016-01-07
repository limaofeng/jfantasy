package org.jfantasy.pay.product;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.file.bean.FileDetail;
import org.jfantasy.framework.util.common.PathUtil;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.product.order.Order;
import org.jfantasy.pay.product.order.OrderItem;
import org.jfantasy.pay.product.order.ShipAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

/**
 * 银联支付测试
 */
public class UnionpayTest {

    private final static Log LOG = LogFactory.getLog(UnionpayTest.class);

    private Unionpay unionpay = new Unionpay();

    @Before
    public void setUp() throws Exception {
        unionpay.setDeployStatus(DeployStatus.Develop);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testApp() throws Exception {
        Order order = new Order() {
            @Override
            public String getSN() {
                return null;
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

        PayConfig config = new PayConfig();

        config.setBargainorKey("000000");

        config.setSignCert(new FileDetail());

        config.getSignCert().setAbsolutePath("/certs/PM_700000000000001_acp.pfx");

        config.setValidateCert(new FileDetail());

        config.getValidateCert().setAbsolutePath("/certs/verify_sign_acp.cer");

        payment.setPayConfig(config);

        Properties properties = new Properties();

        //######  SDK 配置文件   配置文件中日志和证书的存放路径根据实际情况配置，交易地址和证书根据PM环境、生产环境配套配置#####
        //#SDK 日志目录配置
        properties.put("log.back.rootPath", "/mnt/log");

        //#########################签名证书配置 ################################
        //######(以下配置为PM环境：入网测试环境用，生产环境配置见文档说明)#######
        //##签名证书路径
        properties.put("acpsdk.signCert.path", PathUtil.classes() +  "/certs/PM_700000000000001_acp.pfx");
        //##签名证书密码
        properties.put("acpsdk.signCert.pwd", "000000");
        //##签名证书类型
        properties.put("acpsdk.signCert.type", "PKCS12");

        //##########################加密证书配置################################
        //##密码加密证书路径
        properties.put("acpsdk.encryptCert.path", PathUtil.classes() + "/certs/encrypt.cer");
        //#acpsdk.encryptCert.path=d:\\certs\\encryptpub.cer

        //##########################验签证书配置################################
        //##验证签名证书目录
        properties.put("acpsdk.validateCert.dir", PathUtil.classes() + "/certs/");


        //##########################信用卡还款系统使用特殊化配置#################
        //##支持通过商户代码读取指定证书的配置项
        properties.put("acpsdk.signCert.dir", PathUtil.classes() +  "/certs/");

//        SDKConfig.getConfig().loadProperties(properties);

        String result = unionpay.app(order, payment);

        LOG.debug(result);
    }

}