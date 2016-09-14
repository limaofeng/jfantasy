package org.jfantasy.filestore;

import org.jfantasy.framework.autoconfigure.WebStarterAutoConfiguration;
import org.jfantasy.framework.swagger.SwaggerAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {WebSocketAutoConfiguration.class, JmxAutoConfiguration.class, WebStarterAutoConfiguration.class, SwaggerAutoConfiguration.class})
public class ApplicationTest {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationTest.class, args);
    }

}
