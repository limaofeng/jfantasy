package com.fantasy.apidoc;


import com.fantasy.framework.spring.ClassPathScanner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

public class RestApiData {

    private final static Log LOG = LogFactory.getLog(RestApiData.class);

    @Test
    public void generate(){
        Set<Class> restClasses = ClassPathScanner.getInstance().findAnnotationedClasses("com.fantasy", RestController.class);
        LOG.debug("查找到的 REST API");
        for(Class<?> restClass : restClasses){
            LOG.debug(restClass);
        }
    }

}
