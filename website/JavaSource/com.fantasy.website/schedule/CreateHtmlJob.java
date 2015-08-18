package com.fantasy.website.schedule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by hebo on 2015/2/11.
 * 生成htmljob
 */
public class CreateHtmlJob implements Job {

    private final static Log LOG = LogFactory.getLog(CreateHtmlJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getMergedJobDataMap();

    }
}
