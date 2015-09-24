package com.fantasy.framework.swagger;

import com.fantasy.framework.util.common.PropertiesHelper;
import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;
import static com.google.common.base.Predicates.or;

@Configuration
@EnableSwagger2
public class MySwaggerConfig {

    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2).select().build().apiInfo(apiInfo());//.paths(paths())
    }

    private Predicate<String> paths() {
        return or(
                regex("/business.*"),
                regex("/some.*"),
                regex("/contacts.*"),
                regex("/pet.*"),
                regex("/springsRestController.*"),
                regex("/test.*"));
    }

    private ApiInfo apiInfo;

    public MySwaggerConfig() {
        PropertiesHelper propertiesHelper = PropertiesHelper.load("props/application.properties");
        apiInfo = new ApiInfo(
                propertiesHelper.getProperty("swagger.api.title", "My Apps API Title"),
                propertiesHelper.getProperty("swagger.api.description", "My Apps API Description"),
                "",
                propertiesHelper.getProperty("swagger.api.service", "My Apps API terms of service"),
                propertiesHelper.getProperty("swagger.api.contact.email", "My Apps API Contact Email"),
                propertiesHelper.getProperty("swagger.api.licence.type", "My Apps API Licence Type"),
                propertiesHelper.getProperty("swagger.api.licence.url", "My Apps API License URL"));
    }

    private ApiInfo apiInfo() {
        return this.apiInfo;
    }

}