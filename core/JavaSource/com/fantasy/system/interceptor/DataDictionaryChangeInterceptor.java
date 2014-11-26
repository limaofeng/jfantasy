package com.fantasy.system.interceptor;

import com.fantasy.schedule.service.ScheduleService;
import com.fantasy.system.service.DataDictionaryService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Aspect
public class DataDictionaryChangeInterceptor {

    @Resource
    private ScheduleService scheduleService;

    @After("execution(public * com.fantasy.system.service.DataDictionaryService.save(..))")
    public void onSaveOrUpdate() {
        this.scheduleService.triggerJob(DataDictionaryService.jobKey);
    }

    @After("execution(public * com.fantasy.system.service.DataDictionaryService.delete*(..))")
    public void onDelete() {
        this.scheduleService.triggerJob(DataDictionaryService.jobKey);
    }

}
