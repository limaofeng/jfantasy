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

    @Bean
    public PayProductConfiguration paymentConfiguration() {
        return new PayProductConfiguration();
    }

    @Value("${aliyun.ons.producerId:PID-20160428}")
    private String producerId;
    @Value("${aliyun.ons.accessKey:GjYnEEMsLVTomMzF}")
    private String accessKey;
    @Value("${aliyun.ons.secretKey:rYSFhN67iXR0vl0pUSatSQjEqR2e2F}")
    private String secretKey;

    @Bean(initMethod = "start",destroyMethod = "shutdown")
    public ProducerBean producer(){
        ProducerBean producerBean = new ProducerBean();
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.ProducerId,producerId);
        properties.setProperty(PropertyKeyConst.AccessKey,accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey,secretKey);
        producerBean.setProperties(properties);
        return producerBean;
    }

}
