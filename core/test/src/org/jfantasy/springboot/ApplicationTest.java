package org.jfantasy.springboot;

import org.jfantasy.framework.autoconfigure.TomcatAutoConfiguration;
import org.jfantasy.framework.swagger.SwaggerAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(value = {"org.jfantasy.springboot","org.jfantasy.async"})
@EntityScan("org.jfantasy.springboot.bean")
@Configuration
@EnableAutoConfiguration(exclude = {WebSocketAutoConfiguration.class,JmxAutoConfiguration.class, TomcatAutoConfiguration.class, SwaggerAutoConfiguration.class})
public class ApplicationTest {

}
