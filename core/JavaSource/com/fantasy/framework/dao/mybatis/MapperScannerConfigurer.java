package com.fantasy.framework.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

public class MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {
    private String basePackage;
    private boolean addToConfig;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSessionTemplate sqlSessionTemplate;
    private String sqlSessionTemplateBeanName;
    private String sqlSessionFactoryBeanName;
    private Class<? extends Annotation> annotationClass;
    private Class<?> markerInterface;
    private ApplicationContext applicationContext;
    private String beanName;

    public MapperScannerConfigurer() {
        this.addToConfig = true;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setAddToConfig(boolean addToConfig) {
        this.addToConfig = addToConfig;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public void setMarkerInterface(Class<?> superClass) {
        this.markerInterface = superClass;
    }

    @Deprecated
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public void setSqlSessionTemplateBeanName(String sqlSessionTemplateName) {
        this.sqlSessionTemplateBeanName = sqlSessionTemplateName;
    }

    @Deprecated
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void setSqlSessionFactoryBeanName(String sqlSessionFactoryName) {
        this.sqlSessionFactoryBeanName = sqlSessionFactoryName;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.basePackage, "Property 'basePackage' is required");
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    }

    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        processPropertyPlaceHolders();

        Scanner scanner = new Scanner(beanDefinitionRegistry);
        scanner.setResourceLoader(this.applicationContext);

        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ",; \t\n"));
    }

    private void processPropertyPlaceHolders() {
        Map<String, PropertyResourceConfigurer> prcs = this.applicationContext.getBeansOfType(PropertyResourceConfigurer.class);

        if ((!prcs.isEmpty()) && (this.applicationContext instanceof GenericApplicationContext)) {
            BeanDefinition mapperScannerBean = ((GenericApplicationContext) this.applicationContext).getBeanFactory().getBeanDefinition(this.beanName);

            DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
            factory.registerBeanDefinition(this.beanName, mapperScannerBean);

            for (PropertyResourceConfigurer prc : prcs.values()) {
                prc.postProcessBeanFactory(factory);
            }

            PropertyValues values = mapperScannerBean.getPropertyValues();

            this.basePackage = updatePropertyValue("basePackage", values);
            this.sqlSessionFactoryBeanName = updatePropertyValue("sqlSessionFactoryBeanName", values);
            this.sqlSessionTemplateBeanName = updatePropertyValue("sqlSessionTemplateBeanName", values);
        }
    }

    private String updatePropertyValue(String propertyName, PropertyValues values) {
        PropertyValue property = values.getPropertyValue(propertyName);

        if (property == null) {
            return null;
        }

        Object value = property.getValue();

        if (value == null) {
            return null;
        }
        if ((value instanceof String)) {
            return value.toString();
        }
        if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        }
        return null;
    }

    private final class Scanner extends ClassPathBeanDefinitionScanner {

        public Scanner(BeanDefinitionRegistry registry) {
            super(registry);
        }

        protected void registerDefaultFilters() {
            boolean acceptAllInterfaces = true;

            if (MapperScannerConfigurer.this.annotationClass != null) {
                addIncludeFilter(new AnnotationTypeFilter(MapperScannerConfigurer.this.annotationClass));
                acceptAllInterfaces = false;
            }

            if (MapperScannerConfigurer.this.markerInterface != null) {
                addIncludeFilter(new AssignableTypeFilter(MapperScannerConfigurer.this.markerInterface) {
                    protected boolean matchClassName(String className) {
                        return false;
                    }
                });
                acceptAllInterfaces = false;
            }

            if (acceptAllInterfaces) {
                addIncludeFilter(new TypeFilter() {
                    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                        return true;
                    }
                });
            }

            addExcludeFilter(new TypeFilter() {
                public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                    String className = metadataReader.getClassMetadata().getClassName();
                    return className.endsWith("package-info");
                }
            });
        }

        @Override
        protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
            Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

            if (beanDefinitions.isEmpty()) {
                this.logger.warn("No MyBatis mapper was found in '" + MapperScannerConfigurer.this.basePackage + "' package. Please check your configuration.");
            } else {
                for (BeanDefinitionHolder holder : beanDefinitions) {
                    GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '" + definition.getBeanClassName() + "' mapperInterface");
                    }
                    definition.getPropertyValues().add("mapperInterface", definition.getBeanClassName());
                    definition.setBeanClass(MapperFactoryBean.class);
                    definition.getPropertyValues().add("addToConfig", MapperScannerConfigurer.this.addToConfig);
                    if (StringUtils.hasLength(MapperScannerConfigurer.this.sqlSessionFactoryBeanName)) {
                        definition.getPropertyValues().add("sqlSessionFactory", new RuntimeBeanReference(MapperScannerConfigurer.this.sqlSessionFactoryBeanName));
                    } else if (MapperScannerConfigurer.this.sqlSessionFactory != null) {
                        definition.getPropertyValues().add("sqlSessionFactory", MapperScannerConfigurer.this.sqlSessionFactory);
                    }
                    if (StringUtils.hasLength(MapperScannerConfigurer.this.sqlSessionTemplateBeanName)) {
                        definition.getPropertyValues().add("sqlSessionTemplate", new RuntimeBeanReference(MapperScannerConfigurer.this.sqlSessionTemplateBeanName));
                    } else if (MapperScannerConfigurer.this.sqlSessionTemplate != null) {
                        definition.getPropertyValues().add("sqlSessionTemplate", MapperScannerConfigurer.this.sqlSessionTemplate);
                    }
                }
            }

            return beanDefinitions;
        }

        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return (beanDefinition.getMetadata().isInterface()) && (beanDefinition.getMetadata().isIndependent());
        }

        protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
            if (super.checkCandidate(beanName, beanDefinition)) {
                return true;
            }
            this.logger.warn("Skipping MapperFactoryBean with name '" + beanName + "' and '" + beanDefinition.getBeanClassName() + "' mapperInterface" + ". Bean already defined with the same name!");

            return false;
        }
    }
}