//package org.jfantasy.rpc;
//
//import org.jfantasy.pay.order.OrderService;
//import org.jfantasy.rpc.client.NettyClientFactory;
//import org.jfantasy.rpc.proxy.RpcProxyFactory;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//
//
//@ComponentScan("org.jfantasy.pay.order")
//@SpringBootApplication
//public class ClientApplication {
//
//    @Bean
//    public RpcProxyFactory rpcProxyFactory() {
//        return new RpcProxyFactory(new NettyClientFactory("127.0.0.1",9090));
//    }
//
//    /**
//     * 也可以采用配置文件的方式
//     * 如果不想自己proxy,可以像dubbo那样扩展schema
//     * 或者自己scan指定包,在FactoryBean里头替换
//     *
//     * @param rpcProxyFactory rpcProxyFactory
//     * @return OrderService
//     */
//    @Bean
//    public OrderService buildHelloService(RpcProxyFactory rpcProxyFactory) {
//        return rpcProxyFactory.proxyBean(OrderService.class, 1000);
//    }
//
//    public static void main(String[] args) {
//        SpringApplication app = new SpringApplication(ClientApplication.class);
//        app.setWebEnvironment(false);
//        app.run(args);
//    }
//
//}
