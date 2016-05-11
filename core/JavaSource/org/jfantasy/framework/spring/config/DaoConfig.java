package org.jfantasy.framework.spring.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.id.factory.spi.MutableIdentifierGeneratorFactory;
import org.jfantasy.framework.dao.hibernate.event.PropertyGeneratorPersistEventListener;
import org.jfantasy.framework.dao.hibernate.event.PropertyGeneratorSaveOrUpdatEventListener;
import org.jfantasy.framework.dao.hibernate.generator.SequenceGenerator;
import org.jfantasy.framework.dao.hibernate.generator.SerialNumberGenerator;
import org.jfantasy.framework.lucene.dao.hibernate.EntityChangedEventListener;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.PropertiesHelper;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Description: <数据源相关bean的注册>. <br>
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(transactionManagerRef = "jpaTransactionManager", includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {JpaRepository.class})}, basePackages = "org.jfantasy.*.dao")
@Import({MyBatisConfig.class})
public class DaoConfig implements TransactionManagementConfigurer {

    private static final Log LOG = LogFactory.getLog(DaoConfig.class);

    @Autowired(required = false)
    private EntityManagerFactory entityManagerFactory;

    @Bean(name = "sessionFactory")
    public SessionFactory sessionFactory() {
        PropertiesHelper helper = PropertiesHelper.load("application.properties");

        SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);

        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);

        MutableIdentifierGeneratorFactory identifierGeneratorFactory = sessionFactory.getServiceRegistry().getService(MutableIdentifierGeneratorFactory.class);
        identifierGeneratorFactory.register("fantasy-sequence", SequenceGenerator.class);
        identifierGeneratorFactory.register("serialnumber", SerialNumberGenerator.class);

        registry.prependListeners(EventType.SAVE_UPDATE, new PropertyGeneratorSaveOrUpdatEventListener(identifierGeneratorFactory));
        registry.prependListeners(EventType.PERSIST, new PropertyGeneratorPersistEventListener(identifierGeneratorFactory));

        registry.appendListeners(EventType.POST_INSERT, new EntityChangedEventListener());

        for (String listeners : helper.getMergeProperty("spring.jpa.properties.hibernate.event.post-insert")) {
            for (String listener : StringUtil.tokenizeToStringArray(listeners)) {
                registry.appendListeners(EventType.POST_INSERT, findListener(listener, PostInsertEventListener.class));
            }
        }

        for (String listeners : helper.getMergeProperty("spring.jpa.properties.hibernate.event.post-update")) {
            for (String listener : StringUtil.tokenizeToStringArray(listeners)) {
                registry.appendListeners(EventType.POST_UPDATE, findListener(listener, PostUpdateEventListener.class));
            }
        }

        String[] postDeleteListeners = helper.getMergeProperty("spring.jpa.properties.hibernate.event.post-delete");
        for (String listeners : postDeleteListeners) {
            for (String listener : StringUtil.tokenizeToStringArray(listeners)) {
                registry.appendListeners(EventType.POST_DELETE, findListener(listener, PostDeleteEventListener.class));
            }
        }

        LOG.debug(" SessionFactory 加载成功! ");

        return sessionFactory;
    }

    private <T> T findListener(String listenerClassName, Class<T> caseClass) {
        return caseClass.cast(ClassUtil.newInstance(ClassUtil.forName(listenerClassName)));
    }

    @Bean(name = "dataSourceTransactionManager")
    public PlatformTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }

    @Bean(name = "hibernateTransactionManager")
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(sessionFactory());
        return hibernateTransactionManager;
    }

    @Bean(name = "jpaTransactionManager")
    public PlatformTransactionManager jpaTransactionManager() {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return hibernateTransactionManager();
    }


}
