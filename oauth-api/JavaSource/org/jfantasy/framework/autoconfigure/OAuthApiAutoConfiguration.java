package org.jfantasy.framework.autoconfigure;


import org.jfantasy.framework.security.config.SpringSecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SpringSecurityConfig.class)
public class OAuthApiAutoConfiguration {
}
