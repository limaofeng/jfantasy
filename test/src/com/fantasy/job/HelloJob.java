package com.fantasy.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

public class HelloJob implements Job {

    private final static Log _log = LogFactory.getLog(HelloJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        _log.error("Hello World! - " + new Date());
    }

}
