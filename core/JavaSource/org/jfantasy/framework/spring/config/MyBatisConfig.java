package org.jfantasy.framework.spring.config;

import org.apache.ibatis.plugin.Interceptor;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.mybatis.keygen.bean.Sequence;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.PropertiesHelper;
import org.jfantasy.framework.util.common.StringUtil;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        settings.setProperty("dialectClass", "org.jfantasy.framework.dao.mybatis.dialect.MySQLDialect");
        sqlSessionFactoryBean.setConfigurationProperties(settings);

        PropertiesHelper helper = PropertiesHelper.load("application.properties");

        List<Interceptor> interceptors = new ArrayList<>();
        for (String plugins : helper.getMergeProperty("spring.mybatis.plugins")) {
            for (String plugin : StringUtil.tokenizeToStringArray(plugins)) {
                if (StringUtil.isBlank(plugin)) {
                    continue;
                }
                Interceptor interceptor = ClassUtil.newInstance(plugin.trim());
                interceptors.add(interceptor);
            }
        }
        sqlSessionFactoryBean.setPlugins(interceptors.toArray(new Interceptor[interceptors.size()]));


        sqlSessionFactoryBean.setTypeAliases(new Class[]{Pager.class, Sequence.class});

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:org/jfantasy/**/dao/*-Mapper.xml"));

        return sqlSessionFactoryBean;
    }

}
