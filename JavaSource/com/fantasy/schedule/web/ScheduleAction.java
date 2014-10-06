package com.fantasy.schedule.web;


import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.schedule.service.JobInfo;
import com.fantasy.schedule.service.TriggerInfo;
import com.fantasy.schedule.service.TriggerType;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.StringMatcher;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class ScheduleAction extends ActionSupport {

    @Resource
    private Scheduler scheduler;

    public String index() throws Exception {
        this.search();
        this.attrs.put("jobInfos", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }


    public String search() throws Exception {
        List<JobInfo> jobInfos = new ArrayList<JobInfo>();
        Map<String, String> executingJobsMap = new HashMap<String, String>();
        for (JobExecutionContext context : scheduler.getCurrentlyExecutingJobs()) {
            executingJobsMap.put(context.getJobDetail().getKey().getGroup() + context.getJobDetail().getKey().getName(), "1");
        }

        for (String group : scheduler.getJobGroupNames()) {
            @SuppressWarnings("serial")
            Set<JobKey> jobKeys = scheduler.getJobKeys(new GroupMatcher<JobKey>(group, StringMatcher.StringOperatorName.EQUALS) {
            });
            for (JobKey jobKey : jobKeys) {
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                JobInfo jobInfo = new JobInfo(jobDetail);

                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    TriggerInfo triggerInfo = new TriggerInfo(trigger);
                    triggerInfo.setState(scheduler.getTriggerState(trigger.getKey()));
                    jobInfo.addTriggerInfo(triggerInfo);
                }

                jobInfo.setRunning(executingJobsMap.containsKey(jobDetail.getKey().getGroup() + jobDetail.getKey().getName()));
                jobInfos.add(jobInfo);
            }
        }
        this.attrs.put(ROOT, jobInfos);
        /**
         this.attrs.put("jobs", jobInfos);
         this.attrs.put("scheduler", scheduler);
         this.attrs.put("groups", scheduler.getJobGroupNames());
         */
        return JSONDATA;
    }

    public String showAddJob() throws SchedulerException {
        String queryGroup = request.getParameter("queryGroup");
        String queryJobName = request.getParameter("queryJobName");

        List<String> jobGroups = scheduler.getJobGroupNames();

        this.attrs.put("groups", jobGroups);
        this.attrs.put("queryGroup", queryGroup);
        this.attrs.put("queryJobName", queryJobName);

        Map<String, List<String>> jobClassesMap = new HashMap<String, List<String>>();//ScheduleServlet.getJobClassesMap();
        this.attrs.put("jobClassesMap", jobClassesMap);

        String fncoddesc = "功能代码对照";/*"功能代码对照<br>[新单照会=>"+CommonConstant.NEWPOLICY;
        fncoddesc += " 变更照会=>"+CommonConstant.NOTECHANGE;
        fncoddesc += "]\n[变更完成=>"+CommonConstant.CHANGECOMPLETE;
        fncoddesc += " 核保完成=>"+CommonConstant.VALIDPOLICY;
        fncoddesc += "]\n[失效中止自动垫付=>"+CommonConstant.INVALIDPOLICY;
        fncoddesc += " 新单自动转账=>"+CommonConstant.AUTOTRANSFER;
        fncoddesc += "]\n[续期保费到期查询=>"+CommonConstant.DUEPOLICY;
        fncoddesc += " 保险款项待支付=>"+CommonConstant.WAITPAY;
        fncoddesc += " 新单退费=>"+CommonConstant.POLICYREFUND;
        fncoddesc += "]\n[等待生存证明=>"+CommonConstant.WAITLIVEPROOF;
        fncoddesc += " 理赔照会=>"+CommonConstant.CLAIMNOTE;
        fncoddesc += "]\n[理赔完成=>"+CommonConstant.CLAIMCOMPLETE;
        fncoddesc += " 客户来电=>"+CommonConstant.CUSTOMERCALL;
        fncoddesc += "]\n[新单回访=>"+CommonConstant.NEWVISIT;
        fncoddesc += " 保单回执=>"+CommonConstant.POLICYRECEIPT;
        fncoddesc += " 自动转账授权结果=>"+CommonConstant.AUTORESULT;
        fncoddesc += " 客户生日提醒=>"+CommonConstant.REMINDBRITH;
        fncoddesc += "]";*/
        this.attrs.put("fncoddesc", fncoddesc);
        return SUCCESS;
    }

    /**
     * 添加任务的处理，处理完成后返回任务列表
     *
     * @throws org.quartz.SchedulerException
     */
    public String addSave(JobInfo jobInfo, Map<String, String> args) throws SchedulerException {
        Trigger trigger = TriggerType.cron == jobInfo.getType() ? TriggerType.cron.newTrigger(jobInfo.getName(), jobInfo.getGroup(), jobInfo.getCronExpression()) : TriggerType.simple.newTrigger(jobInfo.getName(), jobInfo.getGroup(), jobInfo.getRate(), jobInfo.getTimes());
        JobDetail job = newJob(jobInfo.getJobClass()).withIdentity(jobInfo.getName(), jobInfo.getGroup()).build();
        JobDataMap map = job.getJobDataMap();
        if (args != null) {
            map.putAll(args);
        }
        scheduler.resumeAll();
        scheduler.scheduleJob(job, trigger);
        this.attrs.put("success", "1");
        this.attrs.put("msg", "添加成功");
        return JSONDATA;
    }


    /**
     * 执行某个TASK一次
     *
     * @throws org.quartz.SchedulerException
     */
    public String executeOnce() throws SchedulerException {
        String queryGroup = request.getParameter("queryGroup");
        String queryJobName = request.getParameter("queryJobName");
        if (queryGroup != null) {
            queryGroup = queryGroup.trim();
        }
        if (queryJobName != null) {
            queryJobName = queryJobName.trim();
        }
        String jobKey = request.getParameter("jobKey").trim();
        String[] keyArray = jobKey.split("\\.");
        Trigger trigger = newTrigger().
                withIdentity(keyArray[1] + UUID.randomUUID().toString(), keyArray[0]).
                withPriority(100).
                forJob(JobKey.jobKey(keyArray[1], keyArray[0])).
                build();

        scheduler.scheduleJob(trigger);
        return SUCCESS;
    }

    /**
     * 中断TASK执行
     *
     * @throws org.quartz.SchedulerException
     */
    public String interrupt() throws SchedulerException {
        String queryGroup = request.getParameter("queryGroup");
        String queryJobName = request.getParameter("queryJobName");
        String jobKey = request.getParameter("jobKey").trim();
        String[] keyArray = jobKey.split("\\.");
        JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(keyArray[1], keyArray[0]));
        jobDetail.getJobDataMap();

        scheduler.interrupt(JobKey.jobKey(keyArray[1], keyArray[0]));
        return SUCCESS;
    }

    /**
     * 显示修改页面
     *
     * @return {String}
     * @throws org.quartz.SchedulerException
     */
    public String edit() throws SchedulerException {
        String queryGroup = request.getParameter("queryGroup");
        String queryJobName = request.getParameter("queryJobName");
        String jobKey = request.getParameter("jobKey").trim();
        String[] keyArray = jobKey.split("\\.");

        JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(keyArray[1], keyArray[0]));
        Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(keyArray[1], keyArray[0]));

        this.attrs.put("jobDetail", jobDetail);
        this.attrs.put("trigger", trigger);
        this.attrs.put("queryGroup", queryGroup);
        this.attrs.put("queryJobName", queryJobName);

        if (trigger instanceof SimpleTrigger) {
            this.attrs.put("triggerType", 1);
        } else {
            this.attrs.put("triggerType", 2);
        }

        List<String> jobGroups = scheduler.getJobGroupNames();
        this.attrs.put("jobGroups", jobGroups);
        return SUCCESS;
    }


    /**
     * 修改处理
     *
     * @return {String}
     * @throws org.quartz.SchedulerException
     */
    public String editSave() throws SchedulerException {
        Map<String, String> resultMap = new HashMap<String, String>();
        String queryGroup = request.getParameter("queryGroup");
        String queryJobName = request.getParameter("queryJobName");
        if (queryGroup != null && !"".equals(queryGroup)) {
            try {
                queryGroup = URLEncoder.encode(queryGroup, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (queryJobName != null && !"".equals(queryJobName)) {
            try {
                queryJobName = URLEncoder.encode(queryJobName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String jobKey = request.getParameter("jobKey").trim();
        String triggerType = request.getParameter("triggerType").trim();
        String[] keyArray = jobKey.split("\\.");
        Trigger trigger = null;
        if ("1".equals(triggerType)) {
            String rate = request.getParameter("rate").trim();
            String times = request.getParameter("times").trim();
            Integer rateInt = new Integer(rate);
            Integer timesInt = new Integer(times);
            trigger = newTrigger().
                    withIdentity(keyArray[1], keyArray[0]).
                    withSchedule(simpleSchedule().
                            withIntervalInMinutes(rateInt).
                            withRepeatCount(timesInt).
                            withMisfireHandlingInstructionFireNow()).build();
        } else if ("2".equals(triggerType)) {
            String second = request.getParameter("secondField");
            String minute = request.getParameter("minutesField");
            String hour = request.getParameter("hourField");
            String day = request.getParameter("dayField");
            String month = request.getParameter("monthField");
            String week = request.getParameter("weekField");
            String cronExpression = String.format("%s %s %s %s %s %s", second, minute, hour, day, month, week);
            boolean isValid = CronExpression.isValidExpression(cronExpression);
            if (!isValid) {
                request.setAttribute("errorInfo", "cron表达式填写错误,您的表达式是：" + cronExpression);
                resultMap.put("success", "0");
                resultMap.put("msg", "cron表达式填写错误,您的表达式是：" + cronExpression);
                return JSONDATA;
            }
            try {
                trigger = newTrigger()
                        .withIdentity(keyArray[1], keyArray[0])
                        .withSchedule(cronSchedule(cronExpression)
                                .withMisfireHandlingInstructionFireAndProceed())
                        .build();
            } catch (Exception e) {
                resultMap.put("success", "0");
                resultMap.put("msg", "修改失败");
                return JSONDATA;
            }
        } else {
            String cronExpression = String.format("%s %s %s %s %s %s %s", "0", "*", "*", "*", "*", "?", "2099");
            boolean isValid = CronExpression.isValidExpression(cronExpression);
            if (!isValid) {
                resultMap.put("success", "0");
                resultMap.put("msg", "cron表达式填写错误,您的表达式是：" + cronExpression);
                return JSONDATA;
            }
            try {
                trigger = newTrigger()
                        .withIdentity(keyArray[1], keyArray[0])
                        .withSchedule(cronSchedule(cronExpression)
                                .withMisfireHandlingInstructionFireAndProceed())
                        .build();
            } catch (Exception e) {
                resultMap.put("success", "0");
                resultMap.put("msg", "修改失败");
                return JSONDATA;
            }
        }

        scheduler.rescheduleJob(trigger.getKey(), trigger);
        return JSONDATA;
    }


    /**
     * 删除TASK
     *
     * @throws org.quartz.SchedulerException public String deleteJob(String jobKey) throws SchedulerException {
     *                                       String[] keyArray = jobKey.split("\\.");
     *                                       scheduler.deleteJob(JobKey.jobKey(keyArray[1], keyArray[0]));
     *                                       return JSONDATA;
     *                                       }
     */

    public String deleteJob(String[] ids) throws SchedulerException {
        for (String jobKey : ids) {
            String[] keyArray = jobKey.split("\\.");
            scheduler.deleteJob(JobKey.jobKey(keyArray[1], keyArray[0]));
        }
        return JSONDATA;
    }


    /**
     * 暂停一个Trigger（没有调用)
     *
     * @throws org.quartz.SchedulerException
     */
    public String pauseJob(String jobKey) throws SchedulerException {
        String[] keyArray = jobKey.split("\\.");
        scheduler.pauseJob(JobKey.jobKey(keyArray[1], keyArray[0]));
        return JSONDATA;
    }

    /**
     * 关闭调度器（没有调用)
     *
     * @throws org.quartz.SchedulerException
     */
    public String start() throws SchedulerException {
        scheduler.start();
        return SUCCESS;
    }

    /**
     * 关闭调度器（没有调用)
     *
     * @throws org.quartz.SchedulerException
     */
    public String shutdown() throws SchedulerException {
        scheduler.shutdown(true);
        return SUCCESS;
    }

    /**
     * 重启一个Trigger（没有调用)
     *
     * @param jobKey 任务ID
     * @throws org.quartz.SchedulerException
     */
    public String resumeJob(String jobKey) throws SchedulerException {
        String[] keyArray = jobKey.split("\\.");
        scheduler.resumeJob(JobKey.jobKey(keyArray[1], keyArray[0]));
        return SUCCESS;
    }

    /**
     * （没有调用)
     *
     * @param jobsInfo
     * @throws org.quartz.SchedulerException
     */
    public String pauseOrRunAllJobs(String type, String... jobsInfo) throws SchedulerException {
        for (String jobKey : jobsInfo) {
            String[] keyArray = jobKey.split("\\.");
            if ("1".equals(type)) {
                scheduler.pauseJob(JobKey.jobKey(keyArray[1], keyArray[0]));
            }
            if ("2".equals(type)) {
                scheduler.resumeJob(JobKey.jobKey(keyArray[1], keyArray[0]));
            }
        }
        return SUCCESS;
    }

}
