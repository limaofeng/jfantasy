package com.fantasy.job;

import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.schedule.service.ScheduleService;
import junit.framework.Assert;
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
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class ScheduleServiceTest {

    @Resource
    private ScheduleService scheduleService;

    @Test
    public void testCron() {
        String expression = DateUtil.format("ss mm HH dd MM ? yyyy");
//        String expression = "44 17 11 06 17 ? 2014";
//        String expression = "00 15 10 02 08 ? 2002";
        Assert.assertTrue(CronExpression.isValidExpression(expression));
    }

    @Test
    public void addJobAndTrigger() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "limaofeng");
        data.put("time", DateUtil.format("yyyy-MM-dd HH:mm:ss"));
        // 添加 job
        scheduleService.addJob(JobKey.jobKey("junit", "test"), HelloJob.class,data);
        // 添加触发器
        Map<String, Object> _data = new HashMap<String, Object>();
        _data.put("name", "limaofeng-1");
        scheduleService.addTrigger(JobKey.jobKey("junit", "test"), TriggerKey.triggerKey("test"), 1000, 5, _data);
        try {
            Thread.sleep(1000 * 20);
        }catch (InterruptedException ignored){
        }
        this.removeJobAndTrigger();
    }

    public void removeJobAndTrigger() {
        // 删除 触发器
        scheduleService.removeTrigdger(TriggerKey.triggerKey("test"));
        // 删除 job
        scheduleService.deleteJob(JobKey.jobKey("junit", "test"));

        scheduleService.shutdown();
    }

}
