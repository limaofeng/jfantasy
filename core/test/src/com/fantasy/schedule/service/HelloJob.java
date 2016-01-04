package org.jfantasy.schedule.service;

import org.jfantasy.framework.util.common.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;

import java.util.Date;
import java.util.Map;

public class HelloJob implements Job {

    private final static Log LOG = LogFactory.getLog(HelloJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getMergedJobDataMap();
        StringBuilder log = new StringBuilder();
        LOG.debug("触发时间:"+DateUtil.format("yyyy-MM-dd HH:mm:ss"));
        log.append("\n\n-===============Hello World! - ").append(new Date()).append("==================-").append("\n");
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            log.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        log.append("-===============打印详细的job信息==================-").append("\n");
        print(context.getJobDetail(), context.getTrigger(),log);
        LOG.debug(log.toString());
    }

    public void print(JobDetail jobDetail, Trigger trigger,StringBuilder log) {
        JobKey jobKey = jobDetail.getKey();
        log.append("jobKey:\t").append(jobKey.getGroup()).append(".").append(jobKey.getName()).append("\n");
        log.append("jobClass:\t").append(jobDetail.getJobClass()).append("\n");
        log.append("jobDataMap:").append("\n");
        for (Map.Entry<String, Object> entry : jobDetail.getJobDataMap().entrySet()) {
            log.append("\t").append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        log.append("trigger:").append(trigger.getKey()).append("\n");
        log.append("previousFireTime:").append(DateUtil.format(trigger.getPreviousFireTime(), "yyyy-MM-dd HH:mm:ss")).append("\n");
        log.append("nextFireTime:").append(DateUtil.format(trigger.getNextFireTime(), "yyyy-MM-dd HH:mm:ss")).append("\n");
        log.append("EndTime:").append(DateUtil.format(trigger.getEndTime(), "yyyy-MM-dd HH:mm:ss")).append("\n");
        log.append("jobDataMap:").append("\n");
        for (Map.Entry<String, Object> entry : trigger.getJobDataMap().entrySet()) {
            log.append("\t").append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        if (trigger instanceof CronTrigger) {
            log.append("cron:").append(((CronTrigger) trigger).getCronExpression()).append("\n");
        } else if (trigger instanceof SimpleTrigger) {
            SimpleTrigger simpleTrigger = ((SimpleTrigger) trigger);
            log.append("repeatInterval:").append(simpleTrigger.getRepeatInterval()).append("\n");
            log.append("repeatCount:").append(simpleTrigger.getRepeatCount()).append("\n");
            log.append("timesTriggered:").append(simpleTrigger.getTimesTriggered()).append("\n");
        }
    }

}
