package com.fantasy.job;

import com.fantasy.framework.util.common.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;

import java.util.Date;
import java.util.Map;

public class HelloJob implements Job {

    private final static Log _log = LogFactory.getLog(HelloJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap data = context.getMergedJobDataMap();

        System.out.println("\n\n-===============Hello World! - " + new Date() + "==================-");

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

        System.out.println("-===============打印详细的job信息==================-");
        print(context.getJobDetail(), context.getTrigger());

    }

    public void print(JobDetail jobDetail, Trigger trigger) {
        JobKey jobKey = jobDetail.getKey();
        System.out.println("jobKey:\t" + jobKey.getGroup() + "." + jobKey.getName());
        System.out.println("jobClass:\t" + jobDetail.getJobClass());
        System.out.println("jobDataMap:");
        for (Map.Entry<String, Object> entry : jobDetail.getJobDataMap().entrySet()) {
            System.out.println("\t" + entry.getKey() + ":" + entry.getValue());
        }
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
            System.out.println("repeatCount:" + (simpleTrigger.getRepeatCount() + 1));
            System.out.println("timesTriggered:" + simpleTrigger.getTimesTriggered());
        }
    }

}
