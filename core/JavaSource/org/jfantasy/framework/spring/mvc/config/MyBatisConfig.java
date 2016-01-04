package org.jfantasy.framework.spring.mvc.config;


import org.jfantasy.framework.dao.mybatis.MapperScannerConfigurer;
import org.jfantasy.framework.dao.mybatis.sqlmapper.SqlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    /**
     * 因为继承了 BeanDefinitionRegistryPostProcessor 接口,所以必须与 DaoConfig 分开
     * @return
     */
    @Bean(name="mapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("org.jfantasy.framework;");
        mapperScannerConfigurer.setMarkerInterface(SqlMapper.class);
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        return mapperScannerConfigurer;
    }

}
