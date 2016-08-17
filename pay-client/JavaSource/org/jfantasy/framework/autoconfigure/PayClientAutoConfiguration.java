package org.jfantasy.framework.autoconfigure;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import org.jfantasy.aliyun.AliyunSettings;
import org.jfantasy.pay.ons.PayMessageListener;
import org.jfantasy.pay.order.OrderProcessor;
import org.jfantasy.pay.order.OrderServiceRegistry;
import org.jfantasy.pay.order.OrderServiceRegistryRunner;
import org.jfantasy.rpc.client.NettyClientFactory;
import org.jfantasy.rpc.config.NettyClientSettings;
import org.jfantasy.rpc.proxy.RpcProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Import(OrderServiceRegistryRunner.class)
public class PayClientAutoConfiguration {

    @Autowired
    private AliyunConfiguration aliyunConfiguration;

    @Resource(name = "aliyunSettings")
    private AliyunSettings aliyunSettings;

    @Bean(name = "pay.aliyunSettings")
    @ConfigurationProperties(prefix = "aliyun.ons.pay")
    public AliyunSettings aliyunSettings() {
        return new AliyunSettings(aliyunSettings);
    }

    @Bean
    @ConfigurationProperties(prefix = "netty.client.pay")
    public NettyClientSettings nettyClientSettings() {
        return new NettyClientSettings();
    }

    @Bean(name = "orderServiceRegistry")
    public OrderServiceRegistry buildOrderServiceRegistry() {
        RpcProxyFactory rpcProxyFactory = new RpcProxyFactory(new NettyClientFactory(nettyClientSettings().getHost(), nettyClientSettings().getPort()));
        return rpcProxyFactory.proxyBean(OrderServiceRegistry.class, 10000);
    }

    @Bean(name = "orderProcessor")
    public OrderProcessor buildOrderProcessor() {
        RpcProxyFactory rpcProxyFactory = new RpcProxyFactory(new NettyClientFactory(nettyClientSettings().getHost(), nettyClientSettings().getPort()));
        return rpcProxyFactory.proxyBean(OrderProcessor.class, 10000);
    }

    @Value("${aliyun.ons.pay.consumerId}")
    private String consumerId;
    @Value("${aliyun.ons.pay.accessKey}")
    private String accessKey;
    @Value("${aliyun.ons.pay.secretKey}")
    private String secretKey;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean consumer() {
        Map<Subscription, MessageListener> subscriptions = new HashMap<>();
        Subscription key = new Subscription();
        key.setTopic(aliyunSettings().getTopicId());
        key.setExpression("*");
        subscriptions.put(key, payMessageListener());
        return aliyunConfiguration.consumer(aliyunSettings(), subscriptions);
    }

    @Bean
    public PayMessageListener payMessageListener() {
        return new PayMessageListener();
    }

}
