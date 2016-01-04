package org.jfantasy.schedule.service;


import org.quartz.Trigger;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.util.Date;

public class TriggerInfo {

    private Trigger.TriggerState state;
    private Date preFire;
    private Date nextFire;
    private String cronExpression;
    private long repeatInterval;
    private TriggerType type;

    private int repeatCount;


    public TriggerInfo(Trigger trigger) {
        this.preFire = trigger.getPreviousFireTime();
        this.nextFire = trigger.getNextFireTime();
        this.type = TriggerType.getType(trigger);
        if (TriggerType.cron == this.type) {
            this.cronExpression = ((CronTriggerImpl) trigger).getCronExpression();
        } else if (TriggerType.simple == this.type) {
            this.repeatInterval = ((SimpleTriggerImpl) trigger).getRepeatInterval();
            this.repeatCount =((SimpleTriggerImpl) trigger).getRepeatCount();
        }
    }

    public Trigger.TriggerState getState() {
        return state;
    }

    public void setState(Trigger.TriggerState state) {
        this.state = state;
    }

    public Date getPreFire() {
        return preFire;
    }

    public void setPreFire(Date preFire) {
        this.preFire = preFire;
    }

    public Date getNextFire() {
        return nextFire;
    }

    public void setNextFire(Date nextFire) {
        this.nextFire = nextFire;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public TriggerType getType() {
        return type;
    }

    public void setType(TriggerType type) {
        this.type = type;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }
}
