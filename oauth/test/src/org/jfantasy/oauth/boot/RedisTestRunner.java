package org.jfantasy.oauth.boot;

import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.oauth.AuthApplicationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AuthApplicationTest.class)
public class RedisTestRunner {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testredis() throws Exception {
//        ListOperations listOper = redisTemplate.opsForList();
//        listOper.leftPush("test_list","1");
//        listOper.leftPush("test_list","2");
//        listOper.rightPush("test_list","3");
//        System.out.println(listOper.range("test_list",0,-1));

        SetOperations setOper = redisTemplate.opsForSet();
    }

    @Test
    public void test() {
        String s = "1:role(USER,MASTER)";

        for(RegexpUtil.Group group : RegexpUtil.parseGroups(s.substring(s.indexOf(":") + 1),"(role|group|ip)\\((\\S+)\\)")){
            System.out.println(group.$(1) + group.$(2));
        }


    }

}
