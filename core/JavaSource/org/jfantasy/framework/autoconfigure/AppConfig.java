package org.jfantasy.framework.autoconfigure;

import org.jfantasy.framework.dao.mybatis.keygen.util.DataBaseKeyGenerator;
import org.jfantasy.framework.lucene.BuguIndex;
import org.jfantasy.framework.util.common.PropertiesHelper;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Description: <应用配置类>. <br>
 * <p>
 * <负责注册除Controller等web层以外的所有bean，包括aop代理，service层，dao层，缓存，等等>
 * </p>
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({DaoConfig.class, QuartzConfig.class})
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer placehodlerConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Value("${dataBaseKey.poolSize:10}")
    private String dataBaseKeyPoolSize;

    @Bean
    public DataBaseKeyGenerator dataBaseKeyGenerator() {
        DataBaseKeyGenerator dataBaseKeyGenerator = new DataBaseKeyGenerator();
        dataBaseKeyGenerator.setPoolSize(Integer.valueOf(dataBaseKeyPoolSize));
        return dataBaseKeyGenerator;
    }

    /*
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
    }*/

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
        PropertiesHelper helper = PropertiesHelper.load("props/lucene.properties");

        BuguIndex buguIndex = new BuguIndex();
        buguIndex.setBasePackage(StringUtil.join(helper.getMergeProperty("base.package"), ";"));
        buguIndex.setDirectoryPath(helper.getProperty("index.path"));
        buguIndex.setExecutor(taskExecutor());
        buguIndex.setRebuild(true);
        return buguIndex;
    }

}
