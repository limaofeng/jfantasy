package org.jfantasy.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.jfantasy.attr", "org.jfantasy.album", "org.jfantasy.common", "org.jfantasy.contacts", "org.jfantasy.file", "org.jfantasy.member", "org.jfantasy.payment", "org.jfantasy.remind", "org.jfantasy.schedule", "org.jfantasy.security", "org.jfantasy.system", "org.jfantasy.framework.dao.mybatis.keygen", "org.jfantasy.framework.spring.mvc.http", "org.jfantasy.framework.dao.hibernate.event"})
public class CoreConfig {

}
