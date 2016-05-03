package org.jfantasy.framework.autoconfigure;

import org.jfantasy.rpc.server.NettyServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyServerAutoConfiguration {

    @Bean
    public NettyServer nettyServer(){
        return new NettyServer();
    }

}
