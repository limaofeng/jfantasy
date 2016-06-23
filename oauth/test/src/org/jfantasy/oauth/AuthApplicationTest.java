package org.jfantasy.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {WebSocketAutoConfiguration.class, JmxAutoConfiguration.class})//, TomcatAutoConfiguration.class, SwaggerAutoConfiguration.class
public class AuthApplicationTest {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AuthApplicationTest.class, args);
    }

}
