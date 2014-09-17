package com.fantasy.framework.log.config;

import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.fantasy.framework.log.annotation.AnnotationLogOperationSource;
import com.fantasy.framework.log.interceptor.BeanFactoryLogOperationSourceAdvisor;
import com.fantasy.framework.log.interceptor.LogInterceptor;

public class AnnotationDrivenLogBeanDefinitionParser implements BeanDefinitionParser {

	public static final String LOG_ADVISOR_BEAN_NAME = "org.springframework.log.config.internalLogAdvisor";
	public static final String LOG_ASPECT_BEAN_NAME = "org.springframework.log.config.internalLogAspect";

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		AopAutoProxyConfigurer.configureAutoProxyCreator(element, parserContext);
		return null;
	}

	private static void parseCacheManagerProperty(Element element, BeanDefinition def) {
		def.getPropertyValues().add("logManager", new RuntimeBeanReference(LogNamespaceHandler.extractCacheManager(element)));
	}

	private static class AopAutoProxyConfigurer {

		public static void configureAutoProxyCreator(Element element, ParserContext parserContext) {
			AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(parserContext, element);

			if (!parserContext.getRegistry().containsBeanDefinition(LOG_ADVISOR_BEAN_NAME)) {
				Object eleSource = parserContext.extractSource(element);

				// Create the LogOperationSource definition.
				RootBeanDefinition sourceDef = new RootBeanDefinition(AnnotationLogOperationSource.class);
				sourceDef.setSource(eleSource);
				sourceDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
				String sourceName = parserContext.getReaderContext().registerWithGeneratedName(sourceDef);

				// Create the LogInterceptor definition.
				RootBeanDefinition interceptorDef = new RootBeanDefinition(LogInterceptor.class);
				interceptorDef.setSource(eleSource);
				interceptorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
				parseCacheManagerProperty(element, interceptorDef);
				LogNamespaceHandler.parseKeyGenerator(element, interceptorDef);
				interceptorDef.getPropertyValues().add("logOperationSources", new RuntimeBeanReference(sourceName));
				String interceptorName = parserContext.getReaderContext().registerWithGeneratedName(interceptorDef);

				// Create the LogAdvisor definition.
				RootBeanDefinition advisorDef = new RootBeanDefinition(BeanFactoryLogOperationSourceAdvisor.class);
				advisorDef.setSource(eleSource);
				advisorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
				advisorDef.getPropertyValues().add("adviceBeanName", interceptorName);
				advisorDef.getPropertyValues().add("logOperationSource", new RuntimeBeanReference(sourceName));
				parserContext.getRegistry().registerBeanDefinition(LOG_ADVISOR_BEAN_NAME, advisorDef);

				CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), eleSource);
				compositeDef.addNestedComponent(new BeanComponentDefinition(interceptorDef, interceptorName));
				compositeDef.addNestedComponent(new BeanComponentDefinition(advisorDef, LOG_ADVISOR_BEAN_NAME));
				parserContext.registerComponent(compositeDef);
			}
		}
	}

}
