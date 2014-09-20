package com.fantasy.test.testmybatis;

import com.fantasy.test.bean.Testbean;
import com.fantasy.test.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by hebo on 2014/9/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext.xml"})
public class TestMybatis {

    @Resource
    private TestService testService;
    @Test
    public void insert()
    {
        Testbean testbean = new Testbean();
        testbean.setKey("111");
        testbean.setValue("111");
        int count = testService.insert(testbean);
        System.out.print("影响行数："+count+"\n");
    }
}
