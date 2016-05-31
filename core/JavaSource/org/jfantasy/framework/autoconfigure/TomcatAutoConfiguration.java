package org.jfantasy.framework.autoconfigure;

import org.apache.tomcat.ContextBind;
import org.jfantasy.framework.spring.config.WebMvcConfig;
import org.jfantasy.framework.tomcat.MyTomcatCustomContextCustomizer;
import org.jfantasy.framework.tomcat.MyTomcatCustomizationBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ConditionalOnClass(ContextBind.class)
@Configuration
@ComponentScan("org.jfantasy.framework.spring.mvc")
@Import(WebMvcConfig.class)
public class TomcatAutoConfiguration {

    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer() {
        return new MyTomcatCustomContextCustomizer();
    }

    @Bean
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer(){
        return new MyTomcatCustomizationBean(tomcatContextCustomizer());
    }

}
