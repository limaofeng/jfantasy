package org.jfantasy.framework.autoconfigure;

import org.jfantasy.filestore.service.FileManagerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("org.jfantasy.filestore")
@Configuration
@EntityScan("org.jfantasy.filestore.bean")
public class FileStoreAutoConfiguration {

    @Bean
    public FileManagerFactory fileManagerFactory(){
        return new FileManagerFactory();
    }

}
