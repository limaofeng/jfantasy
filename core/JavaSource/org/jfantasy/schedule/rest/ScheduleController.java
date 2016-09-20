package org.jfantasy.schedule.rest;

import org.jfantasy.schedule.service.ScheduleService;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    @ResponseBody
    public List<JobDetail> jobs() throws Exception {
        return this.scheduleService.jobs();
    }

    @RequestMapping(value = "/jobs/{key}/run", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void runJob(@PathVariable("key") String key) {
        JobKey jobKey = key.contains(".") ? JobKey.jobKey(key.split("\\.")[1], key.split("\\.")[0]) : JobKey.jobKey(key);
        this.scheduleService.triggerJob(jobKey);
    }

    /**
     * 获取任务的触发器
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "/jobs/{key}/triggers", method = RequestMethod.GET)
    public List<Trigger> triggers(@PathVariable("key") String key) {
        JobKey jobKey = key.contains(".") ? JobKey.jobKey(key.split("\\.")[1], key.split("\\.")[0]) : JobKey.jobKey(key);
        return this.scheduleService.getTriggers(jobKey);
    }

    /**
     * 添加新的任务。新添加的任务并不会执行，得添加触发器才行
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/jobs", method = RequestMethod.POST)
    @ResponseBody
    public JobDetail createJob() throws Exception {
        return null;
    }

}
