package org.jfantasy.rpc;

import org.jfantasy.rpc.client.NettyClientFactory;
import org.jfantasy.rpc.proxy.RpcProxyFactory;
import org.jfantasy.rpc.service.HelloService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class ClientApplication {

//    @Component
//    public static class BeanScannerConfigurer implements BeanFactoryPostProcessor, ApplicationContextAware {
//
//        private ApplicationContext applicationContext;
//
//        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//            this.applicationContext = applicationContext;
//        }
//
//        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//            RpcScanner scanner = new RpcScanner((BeanDefinitionRegistry) beanFactory);
//            scanner.setResourceLoader(this.applicationContext);
//            scanner.scan("com.patterncat.rpc.");
//        }
//    }

    @Bean
    public RpcProxyFactory rpcProxyFactory(){
        return new RpcProxyFactory(new NettyClientFactory("127.0.0.1",9090));
    }

    /**
     * 也可以采用配置文件的方式
     * 如果不想自己proxy,可以像dubbo那样扩展schema
     * 或者自己scan指定包,在FactoryBean里头替换
     * @param rpcProxyFactory
     * @return
     */
    @Bean
    public HelloService buildHelloService(RpcProxyFactory rpcProxyFactory){
        return rpcProxyFactory.proxyBean(HelloService.class,10000/*timeout*/);
    }

    public static void main(String[] args){
        SpringApplication app = new SpringApplication(ClientApplication.class);
        app.setWebEnvironment(false);
        app.run(args);
    }
}
