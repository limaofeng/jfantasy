package org.jfantasy.oauth.userdetails;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class OAuthUserDetailsService implements UserDetailsService {

    private final static String REDIS_ASSESS_TOKEN_PREFIX = "assess_token:";

    @Autowired
    private RedisTemplate redisTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public UserDetails loadUserByUsername(String accessToken) throws UsernameNotFoundException {
        HashOperations hashOper = redisTemplate.opsForHash();
        UserDetails userDetails = (UserDetails) hashOper.get(REDIS_ASSESS_TOKEN_PREFIX + accessToken, "user");
        if (userDetails == null) {
            throw new UsernameNotFoundException(" Token Invalid ");
        }
        return userDetails;
    }

}
