package com.fantasy.schedule.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;

@Service
public class ScheduleService {

    @Resource
    private Scheduler scheduler;

    private final static Log logger = LogFactory.getLog(ScheduleService.class);

    /**
     * 返回 各时段的表达式
     *
     * @param cron 表达式
     * @param i    下标
     * @return string
     */
    public static String cron(String cron, int i) {
        String str = "";
        if ("".equals(cron) || cron == null) return str;
        String[] crons = cron.split(" ");
        for (int a = 0; a < crons.length; a++) {
            if (i == a) {
                str = crons[a];
                break;
            }

        }
        return str;
    }

    /**
     * 添加任务
     *
     * @param jobKey   key
     * @param jobClass JobClass
     */
    public void addJob(JobKey jobKey, Class<? extends Job> jobClass) {
        try {
            JobDetail job = newJob(jobClass).withIdentity(jobKey.getName(), jobKey.getGroup()).build();
            scheduler.addJob(job, true);
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 添加任务的触发器
     *
     * @param jobKey     jobKey
     * @param triggerKey triggerKey
     * @param cron       任务表达式
     * @param args       参数
     */
    public void addTrigger(JobKey jobKey, TriggerKey triggerKey, String cron, Map<String, Object> args) {
        try {
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey.getName(), triggerKey.getGroup()).withSchedule(cronSchedule(cron).withMisfireHandlingInstructionFireAndProceed()).build();
            JobDataMap map = trigger.getJobDataMap();
            if (args != null) {
                map.putAll(args);
            }
            scheduler.scheduleJob(scheduler.getJobDetail(jobKey), trigger);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 是否启动
     *
     * @return boolean
     */
    public boolean isStartTimerTisk() {
        try {
            return this.scheduler.isStarted();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 是否关闭
     *
     * @return boolean
     */
    public boolean isShutDownTimerTisk() {
        try {
            return this.scheduler.isShutdown();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 停止 job
     *
     * @param jobName   任务名称
     * @param groupName 组名称
     */
    public void pauseJob(String jobName, String groupName) {
        try {
            this.scheduler.pauseJob(JobKey.jobKey(jobName, groupName));
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 恢复 job
     *
     * @param jobName   任务名称
     * @param groupName 组名称
     */
    public void resumeJob(String jobName, String groupName) {
        try {
            this.scheduler.resumeJob(JobKey.jobKey(jobName, groupName));
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 删除指定的 job
     *
     * @param jobName   任务名称
     * @param groupName 组名称
     * @return boolean
     */
    public boolean deleteJob(String jobName, String groupName) {
        try {
            return this.scheduler.deleteJob(JobKey.jobKey(jobName, groupName));
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 停止触发器
     *
     * @param triggerName 触发器名称
     * @param group       组名称
     */
    public void pauseTrigger(String triggerName, String group) {
        try {
            this.scheduler.pauseTrigger(TriggerKey.triggerKey(triggerName, group));//停止触发器
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 重启触发器
     *
     * @param triggerName 触发器名称
     * @param group       组名称
     */
    public void resumeTrigger(String triggerName, String group) {
        try {
            this.scheduler.resumeTrigger(TriggerKey.triggerKey(triggerName, group));//重启触发器
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 移除触发器
     *
     * @param triggerName 触发器名称
     * @param group       组名称
     * @return boolean
     */
    public boolean removeTrigdger(String triggerName, String group) {
        try {
            this.scheduler.pauseTrigger(TriggerKey.triggerKey(triggerName, group));//停止触发器
            return this.scheduler.unscheduleJob(TriggerKey.triggerKey(triggerName, group));//移除触发器
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 中断TASK执行 job
     *
     * @param jobName   触发器名称
     * @param groupName 组名称
     * @return boolean
     */
    public boolean interrupt(String jobName, String groupName) {
        try {
            return scheduler.interrupt(JobKey.jobKey(jobName, groupName));
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

}
