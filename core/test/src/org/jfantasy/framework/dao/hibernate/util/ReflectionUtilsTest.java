package org.jfantasy.framework.dao.hibernate.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import java.util.Date;

public class ReflectionUtilsTest {

    private static final Log LOG = LogFactory.getLog(ReflectionUtilsTest.class);

    @Test
    public void convertStringToObject() throws Exception {
        LOG.debug(ReflectionUtils.convertStringToObject("2016-06-01",Date.class));
    }

}