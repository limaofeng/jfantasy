package org.jfantasy.framework.autoconfigure;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("org.jfantasy.archives")
@Configuration
@EntityScan("org.jfantasy.archives.bean")
public class ArchivesAutoConfiguration {

}
