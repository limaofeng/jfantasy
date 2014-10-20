package org.springframework.context.annotation;

import junit.framework.Assert;
import org.junit.Test;

public class ConfigurationClassEnhancerTesr {

    @Test
    public void test(){
        ConfigurationClassEnhancer enhancer = new ConfigurationClassEnhancer();
        Assert.assertNotNull(enhancer);
    }

}
