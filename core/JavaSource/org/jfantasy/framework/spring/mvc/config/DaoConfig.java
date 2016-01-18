package org.jfantasy.framework.spring.mvc.config;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.log4j.Logger;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.AnnotationSessionFactoryBean;
import org.jfantasy.framework.dao.mybatis.SqlSessionFactoryBean;
import org.jfantasy.framework.dao.mybatis.interceptors.BusEntityInterceptor;
import org.jfantasy.framework.dao.mybatis.interceptors.MultiDataSourceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;

/**
 * Description: <数据源相关bean的注册>. <br>
 * <p>
 * <使用说明>
 * </p>
 * Makedate:2014年9月3日 下午2:55:14
 *
 * @author Administrator
 * @version V1.0
 */
@Configuration
//启用注解事务管理，使用CGLib代理
@EnableTransactionManagement(proxyTargetClass = true)
@Import({DataSourceConfig.class, MyBatisConfig.class})
public class DaoConfig {

    private static final Logger logger = Logger.getLogger(DaoConfig.class);

    @Resource(name = "dataSource")
    public DataSource dataSource;

    @Bean(name="sqlSessionFactoryBean")
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(this.dataSource);
        sqlSessionFactoryBean.setMybatisProperties(new HashMap<String, Object>() {
            {
                this.put("mybatis.dialect", "org.jfantasy.framework.dao.mybatis.dialect.MySQLDialect");
            }
        });

        sqlSessionFactoryBean.setMapperLocations(new ClassPathResource("org/jfantasy/framework/dao/mybatis/keygen/dao/Sequence-Mapper.xml"));

        Properties settings = new Properties();
        settings.setProperty("cacheEnabled","false");
        settings.setProperty("lazyLoadingEnabled","true");
        settings.setProperty("aggressiveLazyLoading","false");
        sqlSessionFactoryBean.setSettings(settings);

        sqlSessionFactoryBean.setTypeAliases(new Class[]{Pager.class});
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new BusEntityInterceptor(),new MultiDataSourceInterceptor()});
        return sqlSessionFactoryBean;
    }

    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean localSessionFactoryBean() {
        logger.info("sessionFactory");
        AnnotationSessionFactoryBean sessionFactory = new AnnotationSessionFactoryBean();
        sessionFactory.setDataSource(this.dataSource);
        sessionFactory.setNamingStrategy(new ImprovedNamingStrategy());
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.setProperty("hibernate.generate_statistics", "false");
        hibernateProperties.setProperty("javax.persistence.validation.mode", "none");
        hibernateProperties.setProperty("hibernate.archive.autodetection", "class");
        hibernateProperties.setProperty("hibernate.query.startup_check", "false");
        hibernateProperties.setProperty("hibernate.cglib.use_reflection_optimizer", "true");
        hibernateProperties.setProperty("hibernate.current_session_context_class", "org.springframework.orm.hibernate4.SpringSessionContext");
        hibernateProperties.setProperty("hibernate.current_session_context_class", "org.springframework.orm.hibernate4.SpringSessionContext");
        hibernateProperties.setProperty("hibernate.cache.use_query_cache", "true");
        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "true");
        hibernateProperties.setProperty("net.sf.ehcache.hibernate.cache_lock_timeout", "0");
        hibernateProperties.setProperty("net.sf.ehcache.configurationResourceName", "/cache/ehcache.xml");
        hibernateProperties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        /*
        hibernateProperties.setProperty("hibernate.cache.spring.cache_manager", "redisCacheManager");
        hibernateProperties.setProperty("hibernate.cache.region.factory_class", "org.jfantasy.framework.hibernate.cache.SpringCacheRegionFactory");
        */
        hibernateProperties.setProperty("hibernate.cache.use_structured_entries", "true");
        hibernateProperties.setProperty("hibernate.max_fetch_depth", "2");
        hibernateProperties.setProperty("hibernate.order_updates", "true");
        hibernateProperties.setProperty("hibernate.connection.autocommit", "false");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.setProperty("format_sql", "true");
        sessionFactory.setHibernateProperties(hibernateProperties);
        return sessionFactory;
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

    }

    @Bean(name = "transactionManager")
    public HibernateTransactionManager hibernateTransactionManager() {
        logger.info("transactionManager");
        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(localSessionFactoryBean().getObject());
        return hibernateTransactionManager;
    }
}
