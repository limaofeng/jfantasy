package com.fantasy.framework.swagger;

import com.fantasy.framework.util.common.PropertiesHelper;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSwagger
public class MySwaggerConfig {

    private SpringSwaggerConfig springSwaggerConfig;

    private ApiInfo apiInfo;

    public MySwaggerConfig() {
        PropertiesHelper propertiesHelper = PropertiesHelper.load("props/application.properties");
        apiInfo = new ApiInfo(
                propertiesHelper.getProperty("swagger.api.title", "My Apps API Title"),
                propertiesHelper.getProperty("swagger.api.description", "My Apps API Description"),
                propertiesHelper.getProperty("swagger.api.service", "My Apps API terms of service"),
                propertiesHelper.getProperty("swagger.api.contact.email", "My Apps API Contact Email"),
                propertiesHelper.getProperty("swagger.api.licence.type", "My Apps API Licence Type"),
                propertiesHelper.getProperty("swagger.api.licence.url", "My Apps API License URL"));
    }

    @Autowired
    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
        this.springSwaggerConfig = springSwaggerConfig;
    }

    @Bean
    public SwaggerSpringMvcPlugin customImplementation() {
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(getApiInfo()).includePatterns(".*").apiVersion("0.0.1");
    }

    private ApiInfo getApiInfo() {
        return this.apiInfo;
    }

}