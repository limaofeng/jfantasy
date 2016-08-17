package org.jfantasy.framework.spring.validation;

import org.jfantasy.framework.jackson.JSON;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public abstract class RESTful {

    public static RestTemplate restTemplate = new RestTemplate();
    static {
        for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(JSON.getObjectMapper());
            }
        }
    }

    public interface POST {
    }

    public interface PUT {
    }

    public interface DELETE {
    }

    public interface GET {
    }

    public interface PATCH {
    }

}
