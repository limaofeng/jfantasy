package com.fantasy.schedule.service;

import com.fantasy.framework.util.common.DateUtil;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.CronExpression;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml","classpath:spring/applicationContext-schedule.xml"})
public class ScheduleServiceTest {

    @Resource
    private ScheduleService scheduleService;

    @Before
    public void setUp() throws Exception {

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "limaofeng");
        data.put("time", DateUtil.format("yyyy-MM-dd HH:mm:ss"));
        // 添加 job
        scheduleService.addJob(JobKey.jobKey("junit", "test"), HelloJob.class,data);
        // 添加触发器
        Map<String, Object> _data = new HashMap<String, Object>();
        _data.put("name", "limaofeng-1");
        scheduleService.addTrigger(JobKey.jobKey("junit", "test"), TriggerKey.triggerKey("test"), 20, 5, _data);
    }

    @After
    public void tearDown() throws Exception {
        // 删除 触发器
        scheduleService.removeTrigdger(TriggerKey.triggerKey("test"));
        // 删除 job
        scheduleService.deleteJob(JobKey.jobKey("junit", "test"));
    }

    @Test
    public void testCron() {
        String expression = DateUtil.format("ss mm HH dd MM ? yyyy");
        Assert.assertTrue(CronExpression.isValidExpression(expression));
    }


    @Test
    public void testIsStartTimerTisk() throws Exception {

    }

    @Test
    public void testIsShutDownTimerTisk() throws Exception {

    }

    @Test
    public void testPauseJob() throws Exception {

    }

    @Test
    public void testResumeJob() throws Exception {

    }

    @Test
    public void testDeleteJob() throws Exception {

    }

    @Test
    public void testPauseTrigger() throws Exception {

    }

    @Test
    public void testRemoveTrigdger() throws Exception {

    }

    @Test
    public void testPauseAll() throws Exception {

    }

    @Test
    public void testResumeAll() throws Exception {

    }

    @Test
    public void testInterrupt() throws Exception {

    }

    @Test
    public void testTriggerJob() throws Exception {

    }

    @Test
    public void testTriggerJob1() throws Exception {

    }
}