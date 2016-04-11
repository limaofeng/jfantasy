package org.jfantasy.framework.autoconfigure;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ConditionalOnClass(SpringContextUtil.class)
@ComponentScan({"org.jfantasy.framework.dao.mybatis.keygen", "org.jfantasy.schedule"})
@Configuration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@Import({AppConfig.class, MyBatisMapperScannerConfig.class})
public class JCoreAutoConfiguration {

    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }


}
