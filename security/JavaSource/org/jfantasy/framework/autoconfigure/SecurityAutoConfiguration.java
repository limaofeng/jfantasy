package org.jfantasy.framework.autoconfigure;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("org.jfantasy.security")
@Configuration
@EntityScan("org.jfantasy.security.bean")
public class SecurityAutoConfiguration {

}
