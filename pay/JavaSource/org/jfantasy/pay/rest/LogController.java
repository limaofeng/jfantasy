package org.jfantasy.pay.rest;

import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonResultFilter;
import org.jfantasy.pay.bean.Log;
import org.jfantasy.pay.bean.enums.OwnerType;
import org.jfantasy.pay.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志
 */
/** 日志 **/
@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @JsonResultFilter(
            ignore = {
                    @IgnoreProperty(pojo = Log.class, name = {"id", "ownerType", "ownerId"})
            }
    )
    @RequestMapping(value = "/{type}-{sn}", method = RequestMethod.GET)
    @ResponseBody
    public List<Log> search(@PathVariable("type") OwnerType type, @PathVariable("sn") String sn) {
        return logService.logs(type, sn);
    }

}
