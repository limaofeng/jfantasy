package org.jfantasy.pay;

import org.jfantasy.framework.spring.mvc.config.AppConfig;
import org.jfantasy.framework.spring.mvc.config.MyBatisMapperScannerConfig;
import org.jfantasy.framework.spring.mvc.config.SpringSecurityConfig;
import org.jfantasy.framework.spring.mvc.config.WebMvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAutoConfiguration(exclude = {WebSocketAutoConfiguration.class,JmxAutoConfiguration.class})
@PropertySource({"classpath:props/application.properties"})
@Import({AppConfig.class,MyBatisMapperScannerConfig.class,WebMvcConfig.class, SpringSecurityConfig.class})
public class Application{

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}
