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
        //添加数据
        queue.put(new User(){
            {
                this.setNickName("test-1");
            }
        });
        queue.put(new User(){
            {
                this.setNickName("test-2");
            }
        });
        queue.put(new User(){
            {
                this.setNickName("test-3");
            }
        });

        queue.remove(2);
        queue.remove(0);

        Iterator<User> iterator = queue.iterator();
        while (iterator.hasNext()){
            User user = iterator.next();
            logger.error(user.getNickName());
            Assert.assertEquals(user.getNickName(),"test-2");
        }
    }
}