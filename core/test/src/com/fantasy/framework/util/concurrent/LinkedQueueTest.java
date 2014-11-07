package com.fantasy.framework.util.concurrent;

import com.fantasy.security.bean.User;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.util.Iterator;

public class LinkedQueueTest {

    private static Log logger = LogFactory.getLog(LinkedQueueTest.class);

    @Test
    public void testRemove() throws Exception {
        LinkedQueue<User> queue = new LinkedQueue<User>();
        User user = new User();
        //添加数据
        queue.put(user);

        Assert.assertTrue(queue.remove(user));

        Assert.assertEquals(queue.size(), 0);
    }

    @Test
    public void testIterator() throws Exception {
        LinkedQueue<User> queue = new LinkedQueue<User>();
        User _u1 = new User(){
            {
                this.setNickName("test-1");
            }
        };
        User _u2 = new User(){
            {
                this.setNickName("test-2");
            }
        };
        User _u3 = new User(){
            {
                this.setNickName("test-3");
            }
        };
        //添加数据
        queue.add(_u1);
        queue.add(_u2);
        queue.add(_u3);

        queue.remove(_u3);
        queue.remove(_u1);

        logger.error("queue size : " + queue.size());

        Iterator<User> iterator = queue.iterator();
        while (iterator.hasNext()){
            User user = iterator.next();
            logger.error(user.getNickName());
            Assert.assertEquals(user.getNickName(),"test-2");
        }
    }
}