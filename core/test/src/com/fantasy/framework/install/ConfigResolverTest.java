package com.fantasy.framework.install;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ConfigResolverTest {

    private final static Log LOG = LogFactory.getLog(ConfigResolverTest.class);

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testParseConfiguration() throws Exception {
        Resource[] resources = this.resourcePatternResolver.getResources("classpath*:/install.xml" );
        for(Resource resource : resources){
            LOG.debug(resource);
        }

    }

}