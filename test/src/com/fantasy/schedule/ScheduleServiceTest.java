package com.fantasy.schedule;

import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.job.HelloJob;
import com.fantasy.schedule.service.ScheduleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 定时任务测试代码
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class ScheduleServiceTest {

    @Resource
    private ScheduleService scheduleService;

    @Test
    public void addJob() {
        System.out.println("-==========添加测试的job===========-");
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "limaofeng");
        data.put("time", DateUtil.format("yyyy-MM-dd HH:mm:ss"));
        this.scheduleService.addJob(JobKey.jobKey("test"), HelloJob.class, data);
    }

    @Test
    public void runTask() throws InterruptedException, IOException {
        this.addJob();
        this.addCronTrigger();
        this.print();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        do{
            Thread.sleep(1000 * 5 * 5);
        }while (!"exit".equals(reader.readLine()));

        this.print();

        this.scheduleService.removeTrigdger(TriggerKey.triggerKey("test"));
        this.scheduleService.deleteJob(JobKey.jobKey("test"));

    }

    @Test
    public void addTrigger() {
        System.out.println("-==========为job添加触发器===========-");
        this.scheduleService.removeTrigdger(TriggerKey.triggerKey("test"));
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "limaofeng-1");
        this.scheduleService.addTrigger(JobKey.jobKey("test"), TriggerKey.triggerKey("test"), 1000 * 10, 10, data);
    }

    @Test
    public void addCronTrigger() {
        System.out.println("-==========为job添加触发器===========-");
        this.scheduleService.removeTrigdger(TriggerKey.triggerKey("test"));
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "limaofeng-1");
        this.scheduleService.addTrigger(JobKey.jobKey("test"), TriggerKey.triggerKey("test"), DateUtil.format(DateUtil.add(DateUtil.now(), java.util.Calendar.SECOND, 10), "ss mm HH dd MM ? yyyy"), data);
    }

    @Test
    public void testCron() {
        String expression = DateUtil.format("ss mm HH dd MM ? yyyy");
//        String expression = "44 17 11 06 17 ? 2014";
//        String expression = "00 15 10 02 08 ? 2002";
        System.out.println(expression + " : " + CronExpression.isValidExpression(expression));
    }

    @Test
    public void print() {
        System.out.println("-==========打印全部jobkey===========-");
        for (JobKey jobKey : this.scheduleService.getJobKeys()) {
            JobDetail jobDetail = this.scheduleService.getJobDetail(jobKey);
            System.out.println("jobKey:\t" + jobKey.getGroup() + "." + jobKey.getName());
            System.out.println("jobClass:\t" + jobDetail.getJobClass());
            System.out.println("jobDataMap:");
            for (Map.Entry<String, Object> entry : jobDetail.getJobDataMap().entrySet()) {
                System.out.println("\t" + entry.getKey() + ":" + entry.getValue());
            }
            System.out.println("triggers:");
            for (Trigger trigger : this.scheduleService.getTriggers(jobKey)) {
                System.out.println("trigger:" + trigger.getKey());

                System.out.println("previousFireTime:" + DateUtil.format(trigger.getPreviousFireTime(), "yyyy-MM-dd HH:mm:ss"));
                System.out.println("nextFireTime:" + DateUtil.format(trigger.getNextFireTime(), "yyyy-MM-dd HH:mm:ss"));
                System.out.println("EndTime:" + DateUtil.format(trigger.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
                System.out.println("jobDataMap:");
                for (Map.Entry<String, Object> entry : trigger.getJobDataMap().entrySet()) {
                    System.out.println("\t" + entry.getKey() + ":" + entry.getValue());
                }
                if (trigger instanceof CronTrigger) {
                    System.out.println("cron:" + ((CronTrigger) trigger).getCronExpression());
                } else if (trigger instanceof SimpleTrigger) {
                    SimpleTrigger simpleTrigger = ((SimpleTrigger) trigger);
                    System.out.println("repeatInterval:" + simpleTrigger.getRepeatInterval());
                    System.out.println("repeatCount:" + simpleTrigger.getRepeatCount());
                    System.out.println("timesTriggered:" + simpleTrigger.getTimesTriggered());
                }
                System.out.println("TriggerState:" + this.scheduleService.getTriggerState(trigger.getKey()));
            }
            System.out.println("-=============================-");
        }
    }

}
