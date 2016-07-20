package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import org.jfantasy.pay.bean.Log;
import org.jfantasy.pay.bean.enums.OwnerType;
import org.jfantasy.pay.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志
 */
@Api(value = "logs", description = "日志")
@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @RequestMapping(value = "/{type}-{sn}", method = RequestMethod.GET)
    @ResponseBody
    public List<Log> search(@PathVariable("type") OwnerType type, @PathVariable("sn") String sn) {
        return logService.logs(type, sn);
    }

}
