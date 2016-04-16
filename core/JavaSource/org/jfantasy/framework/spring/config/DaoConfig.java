package org.jfantasy.framework.spring.config;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.id.factory.spi.MutableIdentifierGeneratorFactory;
import org.jfantasy.framework.dao.hibernate.event.PropertyGeneratorPersistEventListener;
import org.jfantasy.framework.dao.hibernate.event.PropertyGeneratorSaveOrUpdatEventListener;
import org.jfantasy.framework.dao.hibernate.generator.SequenceGenerator;
import org.jfantasy.framework.dao.hibernate.generator.SerialNumberGenerator;
import org.jfantasy.framework.lucene.dao.hibernate.EntityChangedEventListener;
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

    private static final Logger logger = Logger.getLogger(DaoConfig.class);

    @Autowired(required = false)
    private EntityManagerFactory entityManagerFactory;

    @Bean(name = "sessionFactory")
    public SessionFactory sessionFactory() {
        SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);

        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);

        MutableIdentifierGeneratorFactory identifierGeneratorFactory = sessionFactory.getServiceRegistry().getService(MutableIdentifierGeneratorFactory.class);
        identifierGeneratorFactory.register("fantasy-sequence", SequenceGenerator.class);
        identifierGeneratorFactory.register("serialnumber", SerialNumberGenerator.class);

        registry.prependListeners(EventType.SAVE_UPDATE, new PropertyGeneratorSaveOrUpdatEventListener(identifierGeneratorFactory));
        registry.prependListeners(EventType.PERSIST,new PropertyGeneratorPersistEventListener(identifierGeneratorFactory));

        registry.appendListeners(EventType.POST_INSERT,new EntityChangedEventListener());
        return sessionFactory;
    }

    /*
    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean localSessionFactoryBean() {
        logger.info("sessionFactory");
        AnnotationSessionFactoryBean sessionFactory = new AnnotationSessionFactoryBean();
        sessionFactory.setDataSource(this.dataSource);
        sessionFactory.setNamingStrategy(new ImprovedNamingStrategy());

        String[] ignorePropertyNames = {"packages.to.scan"};

        PropertiesHelper helper = PropertiesHelper.load("props/hibernate.properties");

        sessionFactory.setHibernateProperties(helper.getProperties(ignorePropertyNames));

        sessionFactory.setPackagesToScan(helper.getMergeProperty("packages.to.scan"));

        return sessionFactory;

    }
    */
     /*
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		String[] packagesToScan = new String[] { "web.function.**.model.oracle" };
		sessionFactory.setPackagesToScan(packagesToScan);

		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		hibernateProperties.setProperty("hibernate.show_sql","true");
		hibernateProperties.setProperty(
				"hibernate.current_session_context_class",
				"org.springframework.orm.hibernate4.SpringSessionContext");
		sessionFactory.setHibernateProperties(hibernateProperties);

		return sessionFactory;*/


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
        return jpaTransactionManager();
    }


}
