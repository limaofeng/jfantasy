package org.jfantasy.framework.autoconfigure;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import org.jfantasy.pay.service.PayProductConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import java.util.Properties;

@ComponentScan("org.jfantasy.pay")
@Configuration
@EntityScan("org.jfantasy.pay.bean")
public class PayAutoConfiguration {

    public final static String ONS_TAGS_TRANSACTION = "transaction";
    public final static String ONS_TAGS_TRANSACTION_KEY = "transaction";
    public final static String ONS_TAGS_PAY = "pay";
    public final static String ONS_TAGS_PAY_PAYMENTKEY = "payment";
    public static final String ONS_TAGS_PAY_REFUNDKEY = "refund";

    @Bean
    public PayProductConfiguration paymentConfiguration() {
        return new PayProductConfiguration();
    }

    @Value("${aliyun.ons.pay.accessKey}")
    private String accessKey;
    @Value("${aliyun.ons.pay.secretKey}")
    private String secretKey;

    @Bean(initMethod = "start",destroyMethod = "shutdown")
    public ProducerBean payProducer(@Value("${aliyun.ons.pay.producerId:PID-PAY}")  String producerId){
        ProducerBean producerBean = new ProducerBean();
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.ProducerId,producerId);
        properties.setProperty(PropertyKeyConst.AccessKey,accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey,secretKey);
        producerBean.setProperties(properties);
        return producerBean;
    }

}
