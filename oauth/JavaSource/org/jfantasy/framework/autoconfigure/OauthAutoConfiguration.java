package org.jfantasy.framework.autoconfigure;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.jfantasy.oauth")
@EntityScan("org.jfantasy.oauth.bean")
public class OAuthAutoConfiguration {
}
