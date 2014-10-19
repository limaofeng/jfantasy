package com.fantasy.job;

import com.fantasy.schedule.service.ScheduleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext.xml"})
public class ScheduleServiceTest {

    @Resource
    private ScheduleService scheduleService;

    @Test
    public void test(){
        System.out.println(scheduleService);
    }

}
