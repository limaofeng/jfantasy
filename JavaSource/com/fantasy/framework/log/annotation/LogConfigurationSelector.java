package com.fantasy.framework.log.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;

public class LogConfigurationSelector extends AdviceModeImportSelector<EnableLog> {

    private static final String LOG_ASPECT_CONFIGURATION_CLASS_NAME = "org.springframework.log.aspectj.AspectJCachingConfiguration";

    public String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return new String[]{AutoProxyRegistrar.class.getName(), ProxyLogConfiguration.class.getName()};
            case ASPECTJ:
                return new String[]{LOG_ASPECT_CONFIGURATION_CLASS_NAME};
            default:
                return null;
        }
    }

}