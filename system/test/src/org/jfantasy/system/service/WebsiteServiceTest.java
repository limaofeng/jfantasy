package org.jfantasy.system.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.jackson.JSON;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class WebsiteServiceTest {

    private static Log LOG = LogFactory.getLog(WebsiteServiceTest.class);

    @Autowired
    private WebsiteService websiteService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    @Transactional
    public void testGetAll() throws Exception {
        LOG.debug(JSON.serialize(websiteService.getAll()));
    }
}