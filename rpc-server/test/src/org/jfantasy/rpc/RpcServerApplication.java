package org.jfantasy.rpc;

import org.jfantasy.framework.autoconfigure.JCoreAutoConfiguration;
import org.jfantasy.framework.autoconfigure.TomcatAutoConfiguration;
import org.jfantasy.framework.swagger.SwaggerAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class, TomcatAutoConfiguration.class, JCoreAutoConfiguration.class, SwaggerAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class RpcServerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RpcServerApplication.class);
        app.setWebEnvironment(false);
        app.run(args);
    }

}
