package com.fantasy.test.testmybatis;

import com.fantasy.test.bean.TMyBatisbean;
import com.fantasy.test.service.TMyBatisbeanService;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class TMyBatisbeanServiceTest {

    @Resource
    private TMyBatisbeanService TMyBatisbeanService;

    //@Test
    public void insert() {
        TMyBatisbean testbean = new TMyBatisbean();
        testbean.setKey("111");
        testbean.setValue("111");
        int count = TMyBatisbeanService.insert(testbean);
        System.out.print("影响行数：" + count + "\n");
    }

}
