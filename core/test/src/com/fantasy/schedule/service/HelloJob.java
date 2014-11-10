package com.fantasy.schedule.service;

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
        _log.debug("\n\n-===============Hello World! - " + new Date() + "==================-");
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
        _log.debug("-===============打印详细的job信息==================-");
        print(context.getJobDetail(), context.getTrigger());
    }

    public void print(JobDetail jobDetail, Trigger trigger) {
        JobKey jobKey = jobDetail.getKey();
        _log.debug("jobKey:\t" + jobKey.getGroup() + "." + jobKey.getName());
        _log.debug("jobClass:\t" + jobDetail.getJobClass());
        _log.debug("jobDataMap:");
        for (Map.Entry<String, Object> entry : jobDetail.getJobDataMap().entrySet()) {
            _log.debug("\t" + entry.getKey() + ":" + entry.getValue());
        }
        _log.debug("trigger:" + trigger.getKey());
        _log.debug("previousFireTime:" + DateUtil.format(trigger.getPreviousFireTime(), "yyyy-MM-dd HH:mm:ss"));
        _log.debug("nextFireTime:" + DateUtil.format(trigger.getNextFireTime(), "yyyy-MM-dd HH:mm:ss"));
        _log.debug("EndTime:" + DateUtil.format(trigger.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
        _log.debug("jobDataMap:");
        for (Map.Entry<String, Object> entry : trigger.getJobDataMap().entrySet()) {
            _log.debug("\t" + entry.getKey() + ":" + entry.getValue());
        }
        if (trigger instanceof CronTrigger) {
            _log.debug("cron:" + ((CronTrigger) trigger).getCronExpression());
        } else if (trigger instanceof SimpleTrigger) {
            SimpleTrigger simpleTrigger = ((SimpleTrigger) trigger);
            _log.debug("repeatInterval:" + simpleTrigger.getRepeatInterval());
            _log.debug("repeatCount:" + (simpleTrigger.getRepeatCount() + 1));
            _log.debug("timesTriggered:" + simpleTrigger.getTimesTriggered());
        }
    }

}
