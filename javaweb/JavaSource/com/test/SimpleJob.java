package com.test;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by yhx on 2014/8/8.
 */
public class SimpleJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        System.out.println(context.getTrigger().getKey()+"trigger.time is: "+(new Date()));

    }
}
