package org.jfantasy.pay.service;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.jackson.JSON;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;


public class PayProductConfigurationTest {

    private PayProductConfiguration configuration = new PayProductConfiguration();

    @Before
    public void setUp() throws Exception {
        configuration.afterPropertiesSet();
    }

    private static final Log LOG = LogFactory.getLog(PayProductConfigurationTest.class);

    @Test
    public void loadPayProduct() throws Exception {

    }

    @Test
    public void getPayProducts() throws Exception {
        LOG.debug(JSON.serialize(configuration.getPayProducts()));

        ObjectMapper objectMapper = JSON.getObjectMapper();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonGenerator generator = objectMapper.getFactory().createGenerator(output, JsonEncoding.UTF8);

        objectMapper.writeValue(generator, configuration.getPayProducts());
        generator.flush();

        LOG.debug(output.toString("utf-8"));

    }

}