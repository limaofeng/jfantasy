package com.fantasy.attr.storage.service;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 自定义表单测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CustomFormServiceTest {

    @Before
    public void setUp() throws Exception {
        this.tearDown();

        //1.定义动态表单




        //2.定义测试动态Bean
    }

    @After
    public void tearDown() throws Exception {

    }

}
