package com.fantasy.remind.tigger;

import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.remind.service.NoticeService;
import org.quartz.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动 公告
 */
public class NoticeStartJob implements Job {

    private NoticeService noticeService;

    private List<PropertyFilter> filters;

    public NoticeStartJob(){
        filters=new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQB_issue","false"));
    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail detail = context.getJobDetail();
        JobKey key = detail.getKey();
        String name = key.getName();
    }
}
