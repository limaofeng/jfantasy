package org.jfantasy.framework.autoconfigure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"org.jfantasy.oauth", "org.jfantasy.auth", "org.jfantasy.user"})
@EntityScan("org.jfantasy.oauth.bean")
public class OAuthAutoConfiguration {
}
