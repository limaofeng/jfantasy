package org.jfantasy.framework.dao.hibernate.spi;

import org.hibernate.jpa.spi.IdentifierGeneratorStrategyProvider;
import org.jfantasy.framework.dao.hibernate.generator.SequenceGenerator;
import org.jfantasy.framework.dao.hibernate.generator.SerialNumberGenerator;

import java.util.HashMap;
import java.util.Map;

public class CustomIdentifierGeneratorStrategyProvider implements IdentifierGeneratorStrategyProvider {

    private Map<String, Class<?>> strategies = new HashMap<String, Class<?>>();

    {
        strategies.put("fantasy-sequence", SequenceGenerator.class);
        strategies.put("serialnumber", SerialNumberGenerator.class);
    }

    @Override
    public Map<String, Class<?>> getStrategies() {
        return strategies;
    }

}
