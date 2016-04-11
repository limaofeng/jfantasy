package org.jfantasy;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(value = {"org.jfantasy.springboot"})
@Configuration
@EnableAutoConfiguration(exclude = {WebSocketAutoConfiguration.class,JmxAutoConfiguration.class})
public class ApplicationTest {

}
