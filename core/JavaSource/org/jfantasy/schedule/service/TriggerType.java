package org.jfantasy.schedule.service;


import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

public enum TriggerType {

    simple("简单规则"), cron("cron表达式");

    private String value;

    TriggerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public Trigger newTrigger(String name, String group, int rate, Integer times) {
        return TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(simpleSchedule().withIntervalInMinutes(rate).withRepeatCount(times).withMisfireHandlingInstructionFireNow()).build();
    }

    public Trigger newTrigger(String name, String group, String cronExpression) {
        return TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(cronSchedule(cronExpression).withMisfireHandlingInstructionFireAndProceed()).build();
    }

    public static TriggerType getType(Trigger trigger) {
        if (trigger instanceof CronTriggerImpl) {
            return TriggerType.cron;
        } else if (trigger instanceof SimpleTriggerImpl) {
            return TriggerType.simple;
        }
        return null;
    }

}
