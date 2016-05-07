package org.jfantasy.framework.autoconfigure;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("org.jfantasy.sms")
@Configuration
@EntityScan("org.jfantasy.sms.bean")
public class SMSAutoConfiguration {
}
