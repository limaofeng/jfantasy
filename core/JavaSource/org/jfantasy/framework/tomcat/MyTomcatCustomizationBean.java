package org.jfantasy.framework.tomcat;


import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

public class MyTomcatCustomizationBean implements EmbeddedServletContainerCustomizer {

    private TomcatContextCustomizer tomcatContextCustomizer;

    public MyTomcatCustomizationBean(TomcatContextCustomizer tomcatContextCustomizer) {
        this.tomcatContextCustomizer = tomcatContextCustomizer;
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        ((TomcatEmbeddedServletContainerFactory) container).getTomcatContextCustomizers().add(tomcatContextCustomizer);
    }

}