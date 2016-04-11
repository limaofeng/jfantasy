package org.jfantasy.framework.hibernate.cache.redis;


import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class RedisTest {

    private final static Log LOG = LogFactory.getLog(RedisTest.class);

    @Autowired
    private RedisCacheManager redisCacheManager;

    @Test
    public void testRedisCacheManager(){
        Assert.assertNotNull(redisCacheManager);

        Cache cache = redisCacheManager.getCache("test");

        cache.evict("user:1");

//        User user = cache.get("user:1",User.class);
//
//        LOG.debug(user);
//
//        user = new User();
//        user.setUsername("myloser");
//        user.setPassword("123456");
//        cache.put("user:1",user);
//
//        user = cache.get("user:1",User.class);
//
//        Assert.assertEquals(user.getUsername(),"myloser");
//        Assert.assertEquals(user.getPassword(),"123456");
    }

}
