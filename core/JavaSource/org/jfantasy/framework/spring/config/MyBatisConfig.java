package org.jfantasy.framework.spring.config;

import org.apache.ibatis.plugin.Interceptor;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.mybatis.dialect.MySQLDialect;
import org.jfantasy.framework.dao.mybatis.interceptors.AutoKeyInterceptor;
import org.jfantasy.framework.dao.mybatis.interceptors.BusEntityInterceptor;
import org.jfantasy.framework.dao.mybatis.interceptors.LimitInterceptor;
import org.jfantasy.framework.dao.mybatis.interceptors.MultiDataSourceInterceptor;
import org.jfantasy.framework.dao.mybatis.keygen.bean.Sequence;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class MyBatisConfig {

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        sqlSessionFactoryBean.setDataSource(dataSource);

        Properties settings = new Properties();
        settings.setProperty("cacheEnabled", "false");
        settings.setProperty("lazyLoadingEnabled", "true");
        settings.setProperty("aggressiveLazyLoading", "false");
        sqlSessionFactoryBean.setConfigurationProperties(settings);

        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new BusEntityInterceptor(), new MultiDataSourceInterceptor(), new AutoKeyInterceptor(), new LimitInterceptor(MySQLDialect.class)});

        sqlSessionFactoryBean.setTypeAliases(new Class[]{Pager.class, Sequence.class});

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:org/jfantasy/**/dao/*-Mapper.xml"));

        return sqlSessionFactoryBean;
    }

}
