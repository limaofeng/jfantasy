package org.jfantasy.framework.autoconfigure;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("org.jfantasy.system")
@Configuration
@EntityScan("org.jfantasy.system.bean")
public class SystemAutoConfiguration {
}
