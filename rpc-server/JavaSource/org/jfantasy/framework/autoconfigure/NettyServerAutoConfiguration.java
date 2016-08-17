package org.jfantasy.framework.autoconfigure;

import org.jfantasy.rpc.config.NettyLocalSettings;
import org.jfantasy.rpc.server.NettyServer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyServerAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "netty.server")
    public NettyLocalSettings nettyLocalSettings() {
        return new NettyLocalSettings();
    }

    @Bean
    public NettyServer nettyServer(){
        return new NettyServer(nettyLocalSettings().getPort());
    }

}
