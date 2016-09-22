package org.jfantasy.framework.autoconfigure;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.spring.config.AppConfig;
import org.jfantasy.framework.spring.config.MyBatisMapperScannerConfig;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ConditionalOnClass(SpringContextUtil.class)
@ComponentScan({
        "org.jfantasy.framework.lucene.dao.hibernate",
        "org.jfantasy.framework.dao.mybatis.keygen",
        "org.jfantasy.schedule"
})
@Configuration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@Import({AppConfig.class, MyBatisMapperScannerConfig.class})
@EntityScan("org.jfantasy.framework.dao.mybatis.keygen.bean")
public class JCoreAutoConfiguration {

    @Bean
    public SpringContextUtil springContextUtil(ApplicationContext applicationContext,BeanDefinitionRegistry registry) {
        SpringContextUtil.setApplicationContext(applicationContext);
        SpringContextUtil.setRegistry(registry);

        return new SpringContextUtil();
    }

}
