package com.fantasy.common.rest;

import com.fantasy.common.bean.HotKeywords;
import com.fantasy.common.bean.enums.TimeUnit;
import com.fantasy.common.service.KeywordService;
import com.fantasy.framework.util.common.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "common-hotkeywords", description = "热门关键词")
@RestController
@RequestMapping("/common/hotkeywords")
public class HotKeywordsController {

    @Autowired
    private KeywordService keywordService;

    @ApiOperation(value = "查询热词")
    @RequestMapping(method = {RequestMethod.GET})
    @ResponseBody
    public String[] search(@RequestParam("key") String key, @RequestParam("timeUnit") TimeUnit timeUnit, @RequestParam("time") String time, int size) {
        return keywordService.getKeywords(key, timeUnit, time, ObjectUtil.defaultValue(size, 5));
    }

    @ApiOperation(value = "添加热门关键字")
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public HotKeywords create(HotKeywords hotKeywords) {
        return keywordService.addHotKeywords(hotKeywords.getKey(), hotKeywords.getKeywords());
    }

}
