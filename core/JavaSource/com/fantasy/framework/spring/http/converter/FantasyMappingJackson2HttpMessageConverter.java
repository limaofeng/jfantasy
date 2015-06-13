package com.fantasy.framework.spring.http.converter;


import com.fantasy.framework.util.jackson.JSON;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class FantasyMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setObjectMapper(JSON.getObjectMapper());
    }

}
