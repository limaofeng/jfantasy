package org.jfantasy.framework.spring.mvc.config;

import org.jfantasy.framework.dao.mybatis.keygen.util.DataBaseKeyGenerator;
import org.jfantasy.framework.lucene.BuguIndex;
import org.jfantasy.framework.service.MailSendService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;

/**
 * Description: <应用配置类>. <br>
 * <p>
 * <负责注册除Controller等web层以外的所有bean，包括aop代理，service层，dao层，缓存，等等>
 * </p>
 * Makedate:2014年9月3日 上午9:58:15
 *
 * @author Administrator
 * @version V1.0
 */
@Configuration
@ComponentScan(basePackages = "org.jfantasy.config", excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class})})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({DaoConfig.class, CachingConfig.class, QuartzConfig.class})
@PropertySource({"classpath:props/application.properties"})
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placehodlerConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

	/*
     *
	 * <!-- 激活自动代理功能 参看：web.function.aop.aspect.DemoAspect -->
	 * <aop:aspectj-autoproxy proxy-target-class="true" />
	 * 
	 * @EnableAspectJAutoProxy(proxyTargetClass=true) 与声明下面的bean作用相同
	 */
//	@Bean
//	public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
//		logger.info("AnnotationAwareAspectJAutoProxyCreator");
//		AnnotationAwareAspectJAutoProxyCreator aspectJAutoProxyCreator = new AnnotationAwareAspectJAutoProxyCreator();
//		// false:使用JDK动态代理织入增强 [基于目标类的接口] true:使用CGLib动态代理织入增强[基于目标类]
//		aspectJAutoProxyCreator.setProxyTargetClass(true);
//		return aspectJAutoProxyCreator;
//	}

    @Value("${dataBaseKey.poolSize}")
    private String dataBaseKeyPoolSize;

    @Bean
    public DataBaseKeyGenerator dataBaseKeyGenerator() {
        DataBaseKeyGenerator dataBaseKeyGenerator = new DataBaseKeyGenerator();
        dataBaseKeyGenerator.setPoolSize(Integer.valueOf(dataBaseKeyPoolSize));
        return dataBaseKeyGenerator;
    }

    @Value("${mail.hostname}")
    private String hostname;
    @Value("${mail.from}")
    private String from;
    @Value("${mail.displayName}")
    private String displayName;
    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String passWord;
    @Value("${mail.charset}")
    private String charset;

    @Bean
    public MailSendService mailSendService() {
        MailSendService mailSendService = new MailSendService();
        mailSendService.setHostname(hostname);
        mailSendService.setFrom(from);
        mailSendService.setDisplayName(displayName);
        mailSendService.setUsername(username);
        mailSendService.setPassword(passWord);
        mailSendService.setCharset(charset);
        return mailSendService;
    }

    @Bean(name = "taskExecutor")
    public SchedulingTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        //线程池所使用的缓冲队列
        poolTaskExecutor.setQueueCapacity(200);
        //线程池维护线程的最少数量
        poolTaskExecutor.setCorePoolSize(5);
        //线程池维护线程的最大数量
        poolTaskExecutor.setMaxPoolSize(1000);
        //线程池维护线程所允许的空闲时间
        poolTaskExecutor.setKeepAliveSeconds(30000);
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }

    @Bean
    public BuguIndex buguIndex() {
        BuguIndex buguIndex = new BuguIndex();
        buguIndex.setBasePackage("org.jfantasy.cms");
        buguIndex.setDirectoryPath("/index");
        buguIndex.setExecutor(taskExecutor());
        buguIndex.setRebuild(true);
        return buguIndex;
    }

//    @Bean
//    public RequestMappingHandlerMapping requestMappingHandlerMapping(){
//        return new RequestMappingHandlerMapping();
//    }

}
