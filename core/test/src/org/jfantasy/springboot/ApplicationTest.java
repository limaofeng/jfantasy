package org.jfantasy.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(value = {"org.jfantasy.springboot"})
@EntityScan("org.jfantasy.springboot.bean")
@Configuration
@EnableAutoConfiguration(exclude = {WebSocketAutoConfiguration.class,JmxAutoConfiguration.class})//, TomcatAutoConfiguration.class, SwaggerAutoConfiguration.class
public class ApplicationTest {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationTest.class, args);
    }

}
