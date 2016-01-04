package org.jfantasy.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.jfantasy.framework", "org.jfantasy.attr", "org.jfantasy.album", "org.jfantasy.common", "org.jfantasy.contacts", "org.jfantasy.file", "org.jfantasy.framework", "org.jfantasy.member", "org.jfantasy.payment", "org.jfantasy.remind", "org.jfantasy.schedule", "org.jfantasy.security", "org.jfantasy.framework.swagger", "org.jfantasy.system", "org.jfantasy.sms"})
public class CoreConfig {

}
