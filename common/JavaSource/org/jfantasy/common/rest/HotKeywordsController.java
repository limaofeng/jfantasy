package org.jfantasy.common.rest;

import org.jfantasy.common.bean.HotKeywords;
import org.jfantasy.common.bean.enums.TimeUnit;
import org.jfantasy.common.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** 热门关键词 **/
@RestController
@RequestMapping("/hotkeywords")
public class HotKeywordsController {

    @Autowired
    private KeywordService keywordService;

    /** 查询热词 **/
    @RequestMapping(method = {RequestMethod.GET})
    @ResponseBody
    public String[] search(@RequestParam("key") String key, @RequestParam("timeUnit") TimeUnit timeUnit, @RequestParam("time") String time, @RequestParam("size") int size) {
        return null;//keywordService.getKeywords(key, ObjectUtil.defaultValue(timeUnit,TimeUnit.week), time, ObjectUtil.defaultValue(size, 5));
    }

    /** 添加热门关键字 **/
    @RequestMapping(method = {RequestMethod.POST})
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public HotKeywords create(HotKeywords hotKeywords) {
        return null;//keywordService.addHotKeywords(hotKeywords.getKey(), hotKeywords.getKeywords());
    }

}
