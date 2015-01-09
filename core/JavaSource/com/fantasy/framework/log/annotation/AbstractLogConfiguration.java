package com.fantasy.framework.log.annotation;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

@Configuration
public abstract class AbstractLogConfiguration implements ImportAware {

	protected AnnotationAttributes enableLog;

    private static final Logger LOGGER = Logger.getLogger(AbstractLogConfiguration.class);

	public void setImportMetadata(AnnotationMetadata importMetadata) {
		this.enableLog = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableLog.class.getName(), false));
		Assert.notNull(this.enableLog, "@EnableLog is not present on importing class " + importMetadata.getClassName());
	}

	@PostConstruct
	protected void reconcileLogManager() {
		LOGGER.debug("reconcileLogManager");
	}
}
