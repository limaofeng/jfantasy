package org.jfantasy.framework.spring.config;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;


public class MyBatisConfigTest {

    @Test
    public void sqlSessionFactoryBean() throws Exception {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:org/jfantasy/**/dao/*-Mapper.xml");
        assert resources.length != 0;
    }

}