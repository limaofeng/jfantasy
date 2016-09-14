package org.jfantasy.oauth;

import org.jfantasy.framework.autoconfigure.JCoreAutoConfiguration;
import org.jfantasy.framework.autoconfigure.WebStarterAutoConfiguration;
import org.jfantasy.framework.swagger.SwaggerAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("org.jfantasy.pay.order")
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class, WebStarterAutoConfiguration.class, JCoreAutoConfiguration.class, SwaggerAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class OAuthClientApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(OAuthClientApplication.class);
        app.setWebEnvironment(false);
        app.run(args);
    }

}
