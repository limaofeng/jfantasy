package org.jfantasy.framework.autoconfigure;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("org.jfantasy.member")
@Configuration
@EntityScan("org.jfantasy.member.bean")
public class MemberAutoConfiguration {

}
