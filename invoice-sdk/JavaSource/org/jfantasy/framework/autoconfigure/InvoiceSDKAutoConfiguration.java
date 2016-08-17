package org.jfantasy.framework.autoconfigure;

import com.aliyun.openservices.ons.api.bean.ProducerBean;
import org.jfantasy.aliyun.AliyunSettings;
import org.jfantasy.invoice.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class InvoiceSDKAutoConfiguration {

    @Autowired
    private AliyunConfiguration aliyunConfiguration;

    @Resource(name = "aliyunSettings")
    private AliyunSettings aliyunSettings;

    @Bean(name = "invoice.aliyunSettings")
    @ConfigurationProperties(prefix = "aliyun.ons.invoice")
    public AliyunSettings ialiyunSettings() {
        return new AliyunSettings(aliyunSettings);
    }

    @Bean(name = "invoice.producer",initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean producer() {
        return aliyunConfiguration.producer(ialiyunSettings());
    }

    @Bean
    public OrderService invoiceOrderService() {
        return new OrderService();
    }

}
