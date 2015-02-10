package com.fantasy.schedule.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.StringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Service
public class ScheduleService {

    @Autowired(required = false)
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
        if ("".equals(cron) || cron == null){
            return str;
        }
        String[] crons = cron.split(" ");
        for (int a = 0; a < crons.length; a++) {
            if (i == a) {
                str = crons[a];
                break;
            }
        }
        return str;
    }

    public List<String> getJobGroupNames() {
        try {
            return this.scheduler.getJobGroupNames();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<String>();
        }
    }

    public List<String> getTriggerGroupNames() {
        try {
            return this.scheduler.getTriggerGroupNames();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<String>();
        }
    }

    /**
     * 获取全部jobKey
     *
     * @return list<jobkey>
     */
    public List<JobKey> getJobKeys() {
        List<JobKey> jobKeys = new ArrayList<JobKey>();
        try {
            for (String group : this.scheduler.getJobGroupNames()) {
                jobKeys.addAll(this.scheduler.getJobKeys(new GroupMatcher<JobKey>(group, StringMatcher.StringOperatorName.EQUALS) {
                }));
            }
            return jobKeys;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return jobKeys;
        }
    }

    public List<? extends Trigger> getTriggers(JobKey jobKey) {
        try {
            return this.scheduler.getTriggersOfJob(jobKey);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<Trigger>();
        }
    }

    public List<? extends TriggerKey> getTriggers() {
        List<TriggerKey> triggerKeys = new ArrayList<TriggerKey>();
        try {
            for (String group : this.scheduler.getTriggerGroupNames()) {
                triggerKeys.addAll(this.scheduler.getTriggerKeys(new GroupMatcher<TriggerKey>(group, StringMatcher.StringOperatorName.EQUALS) {
                }));
            }
            return triggerKeys;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return triggerKeys;
        }
    }

    public List<? extends TriggerKey> getTriggers(GroupMatcher<TriggerKey> matcher) {
        List<TriggerKey> triggerKeys = new ArrayList<TriggerKey>();
        try {
            triggerKeys.addAll(this.scheduler.getTriggerKeys(matcher));
            return triggerKeys;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return triggerKeys;
        }
    }

    public JobDetail getJobDetail(JobKey jobKey) {
        try {
            return this.scheduler.getJobDetail(jobKey);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public boolean checkExists(JobKey jobKey) {
        try {
            return this.scheduler.checkExists(jobKey);
        } catch (SchedulerException e) {
            return true;
        }
    }

    public boolean checkExists(TriggerKey triggerKey) {
        try {
            return this.scheduler.checkExists(triggerKey);
        } catch (SchedulerException e) {
            return false;
        }
    }

    /**
     * 添加任务
     *
     * @param jobKey   key
     * @param jobClass JobClass
     */
    public JobDetail addJob(JobKey jobKey, Class<? extends Job> jobClass) {
        try {
            JobDetail job = newJob(jobClass).withIdentity(jobKey.getName(), jobKey.getGroup()).storeDurably(true).build();
            scheduler.addJob(job, true);
            scheduler.resumeJob(jobKey);
            return job;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public JobDetail addJob(JobKey jobKey, Class<? extends Job> jobClass, Map<String, String> data) {
        try {
            if (data == null) {
                data = new HashMap<String, String>();
            }
            JobDetail job = newJob(jobClass).withIdentity(jobKey.getName(), jobKey.getGroup()).storeDurably(true).setJobData(new JobDataMap(data)).build();
            scheduler.addJob(job, true);
            scheduler.resumeJob(jobKey);
            return job;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    private static final String emptyString = "";

    public Trigger addTrigger(JobKey jobKey, TriggerKey triggerKey, String cron) {
        return addTrigger(jobKey, triggerKey, cron, emptyString, new HashMap<String, String>());
    }

    public Trigger addTrigger(JobKey jobKey, TriggerKey triggerKey, String cron, String triggerDescription) {
        return addTrigger(jobKey, triggerKey, cron, triggerDescription, new HashMap<String, String>());
    }

    public Trigger addTrigger(JobKey jobKey, TriggerKey triggerKey, String cron, Map<String, String> args) {
        return this.addTrigger(jobKey, triggerKey, cron, emptyString, args);
    }

    /**
     * 添加任务的触发器
     *
     * @param jobKey     jobKey
     * @param triggerKey triggerKey
     * @param cron       任务表达式
     * @param args       参数
     * @return Trigger
     */
    public Trigger addTrigger(JobKey jobKey, TriggerKey triggerKey, String cron, String triggerDescription, Map<String, String> args) {
        try {
            Trigger trigger = TriggerBuilder.newTrigger().forJob(jobKey).withIdentity(triggerKey).usingJobData(new JobDataMap(args)).withDescription(triggerDescription).withSchedule(cronSchedule(cron).withMisfireHandlingInstructionFireAndProceed()).build();
            this.scheduler.scheduleJob(trigger);
            this.scheduler.resumeTrigger(trigger.getKey());
            return trigger;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public Trigger addTrigger(JobKey jobKey, TriggerKey triggerKey, long interval, int times, Map<String, String> args) {
        return this.addTrigger(jobKey, triggerKey, interval, times, emptyString, args);
    }

    public Trigger addTrigger(JobKey jobKey, TriggerKey triggerKey, long interval, int times, String triggerDescription, Map<String, String> args) {
        try {
            Trigger trigger = TriggerBuilder.newTrigger().forJob(jobKey).withIdentity(triggerKey).usingJobData(new JobDataMap(args)).withDescription(triggerDescription).withSchedule(simpleSchedule().withIntervalInMilliseconds(interval).withRepeatCount(times).withMisfireHandlingInstructionFireNow()).build();
            this.scheduler.scheduleJob(trigger);
            this.scheduler.resumeTrigger(triggerKey);
            return trigger;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public Trigger addTrigger(JobKey jobKey, TriggerKey triggerKey, int interval, DateBuilder.IntervalUnit unit, int times, String triggerDescription, Map<String, String> args) {
        try {
            Trigger trigger = TriggerBuilder.newTrigger().forJob(jobKey).withIdentity(triggerKey).usingJobData(new JobDataMap(args)).withDescription(triggerDescription).withSchedule(dailyTimeIntervalSchedule().withInterval(interval, unit).withRepeatCount(times)).build();
            this.scheduler.scheduleJob(trigger);
            this.scheduler.resumeTrigger(triggerKey);
            return trigger;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public Trigger addTrigger(JobKey jobKey, TriggerKey triggerKey, int interval, DateBuilder.IntervalUnit unit, TimeOfDay timeOfDay, int times, String triggerDescription, Map<String, String> args) {
        try {
            Trigger trigger = TriggerBuilder.newTrigger().forJob(jobKey).withIdentity(triggerKey).usingJobData(new JobDataMap(args)).withDescription(triggerDescription).withSchedule(dailyTimeIntervalSchedule().withInterval(interval, unit).startingDailyAt(timeOfDay).withRepeatCount(times)).build();
            this.scheduler.scheduleJob(trigger);
            this.scheduler.resumeTrigger(triggerKey);
            return trigger;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public Trigger addTrigger(JobKey jobKey, TriggerKey triggerKey, int interval, DateBuilder.IntervalUnit unit, Map<String, String> args) {
        return this.addTrigger(jobKey, triggerKey, interval, unit, emptyString, args);
    }

    public Trigger addTrigger(JobKey jobKey, TriggerKey triggerKey, int interval, DateBuilder.IntervalUnit unit, String triggerDescription, Map<String, String> args) {
        try {
            Trigger trigger = TriggerBuilder.newTrigger().forJob(jobKey).withIdentity(triggerKey).usingJobData(new JobDataMap(args)).withDescription(triggerDescription).withSchedule(calendarIntervalSchedule().withInterval(interval, unit)).build();
            this.scheduler.scheduleJob(trigger);
            this.scheduler.resumeTrigger(triggerKey);
            return trigger;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public Trigger.TriggerState getTriggerState(TriggerKey triggerKey) {
        try {
            return this.scheduler.getTriggerState(triggerKey);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 是否启动
     *
     * @return boolean
     */
    public boolean isStartTimerTisk() {
        try {
            return this.scheduler != null && this.scheduler.isStarted();
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
            return this.scheduler != null && this.scheduler.isShutdown();
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
     * @param jobKey 任务名称
     */
    public void resumeJob(JobKey jobKey) {
        try {
            this.scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 删除指定的 job
     *
     * @param jobKey 任务名称
     * @return boolean
     */
    public boolean deleteJob(JobKey jobKey) {
        try {
            return this.scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 停止触发器
     *
     * @param triggerKey 触发器名称
     */
    public void pauseTrigger(TriggerKey triggerKey) {
        try {
            this.scheduler.pauseTrigger(triggerKey);//停止触发器
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 重启触发器
     *
     * @param triggerKey 触发器名称
     */
    public void resumeTrigger(TriggerKey triggerKey) {
        try {
            this.scheduler.resumeTrigger(triggerKey);//重启触发器
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 移除触发器
     *
     * @param triggerKey 触发器名称
     * @return boolean
     */
    public boolean removeTrigdger(TriggerKey triggerKey) {
        try {
            this.scheduler.pauseTrigger(triggerKey);//停止触发器
            return this.scheduler.unscheduleJob(triggerKey);//移除触发器
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 暂停调度中所有的job任务
     */
    public void pauseAll() {
        try {
            scheduler.pauseAll();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 恢复调度中所有的job的任务
     */
    public void resumeAll() {
        try {
            scheduler.resumeAll();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 中断TASK执行 job
     *
     * @param jobKey 触发器名称
     * @return boolean
     */
    public boolean interrupt(JobKey jobKey) {
        try {
            return scheduler.interrupt(jobKey);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 直接执行job
     *
     * @param jobKey jobkey
     */
    public void triggerJob(JobKey jobKey) {
        try {
            this.scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 直接触发job
     *
     * @param jobKey jobkey
     * @param args   执行参数
     */
    public void triggerJob(JobKey jobKey, Map<String, String> args) {
        try {
            this.scheduler.triggerJob(jobKey, new JobDataMap(args));
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void shutdown() {
        try {
            this.scheduler.shutdown();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public List<JobDetail> jobs() {
        List<JobDetail> jobDetails = new ArrayList<JobDetail>();
        for (JobKey jobKey : this.getJobKeys()) {
            try {
                jobDetails.add(scheduler.getJobDetail(jobKey));
            } catch (SchedulerException e) {
                logger.error(e.getMessage(), e);
                if (e.getCause() instanceof ClassNotFoundException) {
                    this.deleteJob(jobKey);
                }
            }
        }
        return jobDetails;
    }
}
