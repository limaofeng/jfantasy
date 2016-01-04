package org.jfantasy.config;

import org.jfantasy.express.product.LogisticsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.jfantasy.express"})
public class ExpressConfig {

    @Bean
    public LogisticsConfiguration logisticsConfiguration() {
        return new LogisticsConfiguration();
    }

}
