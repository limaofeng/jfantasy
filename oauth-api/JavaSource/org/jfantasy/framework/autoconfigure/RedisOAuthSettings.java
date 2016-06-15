package org.jfantasy.framework.autoconfigure;

import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.oauth.redis")
public class RedisOAuthSettings {
    private String host;
    private String password;
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(Integer port) {
        if(StringUtil.isBlank(port)){
            this.port = 6379;
        }else {
            this.port = port;
        }
    }

    public boolean invalid() {
        return StringUtil.isBlank(this.host);
    }
}
