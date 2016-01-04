package org.jfantasy.schedule.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;

public class LogListener implements SchedulerListener {

    private final static Log LOGGER = LogFactory.getLog(LogListener.class);

    @Override
    public void jobScheduled(Trigger trigger) {
        LOGGER.debug(trigger);
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
        LOGGER.debug(triggerKey);
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
        LOGGER.debug(trigger);
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
        LOGGER.debug(triggerKey);
    }

    @Override
    public void triggersPaused(String s) {

    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {

    }

    @Override
    public void triggersResumed(String s) {

    }

    @Override
    public void jobAdded(JobDetail jobDetail) {

    }

    @Override
    public void jobDeleted(JobKey jobKey) {

    }

    @Override
    public void jobPaused(JobKey jobKey) {

    }

    @Override
    public void jobsPaused(String s) {

    }

    @Override
    public void jobResumed(JobKey jobKey) {

    }

    @Override
    public void jobsResumed(String s) {

    }

    @Override
    public void schedulerError(String s, SchedulerException e) {

    }

    @Override
    public void schedulerInStandbyMode() {

    }

    @Override
    public void schedulerStarted() {

    }

    @Override
    public void schedulerStarting() {

    }

    @Override
    public void schedulerShutdown() {

    }

    @Override
    public void schedulerShuttingdown() {

    }

    @Override
    public void schedulingDataCleared() {

    }
}
