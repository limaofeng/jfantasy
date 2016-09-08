package org.jfantasy.framework.autoconfigure;

import com.aliyun.openservices.ons.api.bean.ProducerBean;
import org.jfantasy.aliyun.AliyunSettings;
import org.jfantasy.pay.product.PaySettings;
import org.jfantasy.pay.service.PayProductConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@ComponentScan("org.jfantasy.pay")
@Configuration
@EntityScan("org.jfantasy.pay.bean")
@EnableConfigurationProperties(PaySettings.class)
public class PayAutoConfiguration {

    public final static String ONS_TAGS_TRANSACTION_KEY = "transaction";
    public final static String ONS_TAGS_PAY_PAYMENTKEY = "payment";
    public static final String ONS_TAGS_PAY_REFUNDKEY = "refund";
    public static final String ONS_TAGS_ACCOUNT_KEY = "account";
    public static final String ONS_TAGS_POINT_KEY = "point";
    public static final String ONS_TAGS_CARDBIND_KEY = "card_bind";

    @Bean
    public PayProductConfiguration paymentConfiguration() {
        return new PayProductConfiguration();
    }

    @Autowired
    private AliyunConfiguration aliyunConfiguration;

    @Resource(name = "aliyunSettings")
    private AliyunSettings aliyunSettings;

    @Bean(name = "pay.aliyunSettings")
    @ConfigurationProperties(prefix = "aliyun.ons.pay")
    public AliyunSettings aliyunSettings() {
        return new AliyunSettings(aliyunSettings);
    }

    /**
     * 发布者
     */
    @Bean(name = "pay.producer", initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean producer() {
        return aliyunConfiguration.producer(aliyunSettings());
    }

}
