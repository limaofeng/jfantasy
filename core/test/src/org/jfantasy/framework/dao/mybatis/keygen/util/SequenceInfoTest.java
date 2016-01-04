package org.jfantasy.framework.dao.mybatis.keygen.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class SequenceInfoTest {

    private static final Log LOG = LogFactory.getLog(SequenceInfoTest.class);

    @Test
    public void testNextValue() throws Exception {
        List<Thread> threads = new ArrayList<Thread>();
        final Set<Long> set = new HashSet<Long>();
        for(int i=0;i<1200;i++){
            threads.add(new Thread(new Runnable(){

                @Override
                public void run() {
                    set.add(SequenceInfo.nextValue("test"));
                    LOG.debug("next："+set.size()+"次");
                }
            }));
        }
        for(Thread thread : threads){
            thread.start();
        }
        Thread.sleep(TimeUnit.MINUTES.toMillis(2));
    }
}