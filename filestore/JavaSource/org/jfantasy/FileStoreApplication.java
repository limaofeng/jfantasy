package org.jfantasy;

import org.jfantasy.framework.autoconfigure.AppConfig;
import org.jfantasy.framework.autoconfigure.MyBatisMapperScannerConfig;
import org.jfantasy.framework.autoconfigure.SpringSecurityConfig;
import org.jfantasy.framework.autoconfigure.WebMvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("org.jfantasy.filestore")
@Configuration
@EnableAutoConfiguration(exclude = {WebSocketAutoConfiguration.class,JmxAutoConfiguration.class})
@PropertySource({"classpath:props/application.properties"})
@Import({AppConfig.class,MyBatisMapperScannerConfig.class,WebMvcConfig.class, SpringSecurityConfig.class})
public class FileStoreApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FileStoreApplication.class, args);
    }

}