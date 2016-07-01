package org.jfantasy.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {WebSocketAutoConfiguration.class, JmxAutoConfiguration.class})//TomcatAutoConfiguration.class, SwaggerAutoConfiguration.class
public class PayApplicationTest {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PayApplicationTest.class, args);
    }

}
