package com.test;

import com.fantasy.framework.util.common.DateUtil;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.util.Date;

/**
 * Created by yhx on 2014/8/8.
 */
public class SimplerTriggerRunner {

    public static  void main(String[] args){
        try{
            JobDetailImpl jobDetailImpl = new JobDetailImpl();
            jobDetailImpl.setName("test1");
            jobDetailImpl.setGroup("group1");
            jobDetailImpl.setJobClass(SimpleJob.class);

            SimpleTriggerImpl simpleTrigger = new SimpleTriggerImpl();
            simpleTrigger.setGroup("SimpleJob");
            simpleTrigger.setName("simpleJob1");
            simpleTrigger.setStartTime(new Date());
            simpleTrigger.setEndTime(DateUtil.parse("2014-08-08 14:43:00"));
            simpleTrigger.setRepeatCount(0);
            simpleTrigger.setRepeatInterval(2000L);

            SchedulerFactory factory =new StdSchedulerFactory();
            Scheduler scheduler =factory.getScheduler();
            scheduler.scheduleJob(jobDetailImpl,simpleTrigger);

            scheduler.start();//启动定时任务

        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
