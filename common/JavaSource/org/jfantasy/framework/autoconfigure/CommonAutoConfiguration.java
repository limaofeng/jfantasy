package org.jfantasy.framework.autoconfigure;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("org.jfantasy.common")
@Configuration
@EntityScan("org.jfantasy.common.bean")
public class CommonAutoConfiguration {
}
