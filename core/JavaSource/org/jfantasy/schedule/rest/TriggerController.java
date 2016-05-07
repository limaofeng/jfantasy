package org.jfantasy.schedule.rest;

import io.swagger.annotations.Api;
import org.jfantasy.schedule.service.ScheduleService;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 触发器控制器
 */
@Api(value = "schedule-trigger", description = " 任务 - 触发器 ")
@RestController
@RequestMapping("/triggers")
public class TriggerController {

    @Autowired
    private ScheduleService scheduleService;

    @RequestMapping(value = "/{key}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("key") String key) {
        TriggerKey triggerKey = key.contains(".") ? TriggerKey.triggerKey(key.split("\\.")[1], key.split("\\.")[0]) : TriggerKey.triggerKey(key);
        this.scheduleService.removeTrigdger(triggerKey);
    }

}
