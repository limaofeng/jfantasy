package org.jfantasy.system.interceptor;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.jfantasy.schedule.service.ScheduleService;
import org.jfantasy.system.service.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class DataDictionaryChangeInterceptor {

    @Autowired
    private ScheduleService scheduleService;

    @After("execution(public * org.jfantasy.system.service.DataDictionaryService.save(..))")
    public void onSaveOrUpdate() {
        this.scheduleService.triggerJob(DataDictionaryService.jobKey);
    }

    @After("execution(public * org.jfantasy.system.service.DataDictionaryService.delete*(..))")
    public void onDelete() {
        this.scheduleService.triggerJob(DataDictionaryService.jobKey);
    }

}
