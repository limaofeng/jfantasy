package com.fantasy.framework.ws.util;

import com.fantasy.framework.dao.hibernate.PropertyFilter;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WebServiceUtilTest {

    private final static Log logger = LogFactory.getLog(WebServiceUtilTest.class);

    @Test
    public void testToPager() throws Exception {

    }

    @Test
    public void testToFilters() throws Exception {
        List<PropertyFilterDTO> filterDTOs = new ArrayList<PropertyFilterDTO>();
        filterDTOs.add(new PropertyFilterDTO("EQS_username", "test"));
        filterDTOs.add(new PropertyFilterDTO("EQS_password", "test"));

        logger.debug(filterDTOs);

        List<PropertyFilter> filters = WebServiceUtil.toFilters(filterDTOs.toArray(new PropertyFilterDTO[filterDTOs.size()]));

        logger.debug(filters);

        Assert.assertEquals(filters.get(0).getFilterName(), filterDTOs.get(0).getFilterName());
        Assert.assertEquals(filters.get(1).getFilterName(), filterDTOs.get(1).getFilterName());

        filters = WebServiceUtil.toFilters(filterDTOs.toArray(new PropertyFilterDTO[filterDTOs.size()]), MappingDTO.class);

        logger.debug(filters);

        Assert.assertEquals(filters.get(0).getFilterName(), "EQS_name");
        Assert.assertEquals(filters.get(1).getFilterName(), "EQS_pwd");

    }
}