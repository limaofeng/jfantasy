package org.jfantasy.framework.swagger;

import com.google.common.base.Predicate;
import org.jfantasy.framework.spring.config.WebMvcConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.mvc.*;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.RequestHandler;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.Set;

import static springfox.documentation.builders.PathSelectors.regex;

@ConditionalOnClass(EnableSwagger2.class)
@Configuration
@EnableSwagger2
@AutoConfigureAfter(WebMvcConfig.class)
public class SwaggerAutoConfiguration implements EnvironmentAware {

    private final Logger LOG = LoggerFactory.getLogger(SwaggerAutoConfiguration.class);
    private static final String DEFAULT_INCLUDE_PATTERN = "/.*";
    private static Set<Class<?>> excludes = new HashSet<Class<?>>() {
        private static final long serialVersionUID = -25359758379153662L;
        {
            this.add(BasicErrorController.class);
            this.add(HalJsonMvcEndpoint.class);
            this.add(EndpointMvcAdapter.class);
            this.add(HealthMvcEndpoint.class);
            this.add(LogFileMvcEndpoint.class);
            this.add(MetricsMvcEndpoint.class);
            this.add(EnvironmentMvcEndpoint.class);
        }
    };
    private static Predicate<RequestHandler> apis = new Predicate<RequestHandler>() {
        @Override
        public boolean apply(RequestHandler handler) {
            Class<?> beanType = handler.getHandlerMethod().getBeanType();
            return !excludes.contains(beanType);
        }
    };

    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "springfox.documentation.swagger.");
    }

    @Bean
    public Docket swaggerSpringfoxDocket() {
        LOG.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();
        Docket swaggerSpringMvcPlugin = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .apis(apis)
                .paths(regex(DEFAULT_INCLUDE_PATTERN)).build();// and by paths
        if (propertyResolver.containsProperty("host")) {
            swaggerSpringMvcPlugin.host(propertyResolver.getProperty("host"));
        }
        watch.stop();
        LOG.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return swaggerSpringMvcPlugin;
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                propertyResolver.getProperty("api.title"),
                propertyResolver.getProperty("api.description"),
                propertyResolver.getProperty("api.version"),
                propertyResolver.getProperty("api.termsOfServiceUrl"),
                new Contact(propertyResolver.getProperty("api.contact.name"), propertyResolver.getProperty("api.contact.url", ""), propertyResolver.getProperty("api.contact.email")),
                propertyResolver.getProperty("api.license"),
                propertyResolver.getProperty("api.licenseUrl")
        );
    }

}