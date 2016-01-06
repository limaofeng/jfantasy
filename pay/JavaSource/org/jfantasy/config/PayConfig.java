package org.jfantasy.config;

import org.jfantasy.pay.service.PayProductConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.jfantasy.pay"})
public class PayConfig {

    @Bean
    public PayProductConfiguration paymentConfiguration() {
        return new PayProductConfiguration();
    }

}
