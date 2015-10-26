package com.fantasy.system.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class DDUtilsTest {

    private static Log LOG = LogFactory.getLog(DDUtilsTest.class);

    @After
    public void tearDown() throws Exception {
        DDUtils.remove("doctor:subscribe-start");
        DDUtils.remove("doctor:subscribe-end");
    }

    @Test
    public void testGet() throws Exception {
        LOG.debug(DDUtils.get("doctor:subscribe-start"));
        LOG.debug(DDUtils.get("doctor:subscribe-end"));

        LOG.debug(DDUtils.get("doctor:subscribe-start","2"));
        LOG.debug(DDUtils.get("doctor:subscribe-end","3"));

        LOG.debug(DDUtils.get("doctor:subscribe-start",int.class,4));
        LOG.debug(DDUtils.get("doctor:subscribe-end",int.class,5));

        DDUtils.set("doctor:subscribe-start",1);
        DDUtils.set("doctor:subscribe-end",8);

        LOG.debug(DDUtils.get("doctor:subscribe-start"));
        LOG.debug(DDUtils.get("doctor:subscribe-end"));

        LOG.debug(DDUtils.get("doctor:subscribe-start",int.class));
        LOG.debug(DDUtils.get("doctor:subscribe-end",int.class));
    }

}
