package org.jfantasy.schedule.rest;

import org.jfantasy.schedule.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "schedule-users", description = " 任务计划 ")
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "获取全部任务", notes = "通过该接口, 返回全部的任务调度")
    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    @ResponseBody
    public List<JobDetail> jobs() throws Exception {
        return this.scheduleService.jobs();
    }

    @ApiOperation(value = "执行任务", notes = "立即执行key对应的任务")
    @RequestMapping(value = "/jobs/{key}/run", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void runJob(@PathVariable("key") String key) {
        JobKey jobKey = key.contains(".") ? JobKey.jobKey(key.split("\\.")[1], key.split("\\.")[0]) : JobKey.jobKey(key);
        this.scheduleService.triggerJob(jobKey);
    }

    @ApiOperation(value = "获取任务的触发器", notes = "返回任务对应的触发器")
    @RequestMapping(value = "/jobs/{key}/triggers", method = RequestMethod.GET)
    public List<Trigger> triggers(@PathVariable("key") String key) {
        JobKey jobKey = key.contains(".") ? JobKey.jobKey(key.split("\\.")[1], key.split("\\.")[0]) : JobKey.jobKey(key);
        return this.scheduleService.getTriggers(jobKey);
    }

    @ApiOperation(value = "添加任务", notes = "添加新的任务。新添加的任务并不会执行，得添加触发器才行")
    @RequestMapping(value = "/jobs", method = RequestMethod.POST)
    @ResponseBody
    public JobDetail createJob() throws Exception {
        return null;
    }

}
