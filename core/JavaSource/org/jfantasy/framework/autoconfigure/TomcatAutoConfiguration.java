package org.jfantasy.framework.autoconfigure;

import org.jfantasy.framework.spring.config.WebMvcConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("org.jfantasy.framework.spring.mvc")
@Import(WebMvcConfig.class)
public class TomcatAutoConfiguration {

}
