package org.jfantasy.framework.spring.config;

import org.jfantasy.framework.dao.mybatis.sqlmapper.SqlMapper;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(MyBatisConfig.class)
public class MyBatisMapperScannerConfig {

    /**
     * 因为继承了 BeanDefinitionRegistryPostProcessor 接口,所以必须与 DaoConfig 分开
     *
     * @return MapperScannerConfigurer
     */
    @Bean(name = "mapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("org.jfantasy.framework;");
        mapperScannerConfigurer.setMarkerInterface(SqlMapper.class);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return mapperScannerConfigurer;
    }

}
