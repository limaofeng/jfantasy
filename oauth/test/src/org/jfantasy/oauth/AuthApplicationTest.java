package org.jfantasy.oauth;

import org.jfantasy.framework.autoconfigure.TomcatAutoConfiguration;
import org.jfantasy.framework.swagger.SwaggerAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {WebSocketAutoConfiguration.class, JmxAutoConfiguration.class,TomcatAutoConfiguration.class, SwaggerAutoConfiguration.class})
public class AuthApplicationTest {

}
