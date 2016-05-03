package org.jfantasy.framework.autoconfigure;

import org.jfantasy.pay.service.PayProductConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("org.jfantasy.pay")
@Configuration
@EntityScan("org.jfantasy.pay.bean")
public class PayAutoConfiguration {

    @Bean
    public PayProductConfiguration paymentConfiguration() {
        return new PayProductConfiguration();
    }

}
