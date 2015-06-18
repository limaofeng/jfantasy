package com.fantasy.system.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.system.bean.DataDictionary;
import com.fantasy.system.bean.DataDictionaryKey;
import com.fantasy.system.service.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dds")
public class DataDictionaryController {

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @RequestMapping(value = "/{type}:{code}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("type") String type, @PathVariable("code") String code) {
        this.dataDictionaryService.delete(DataDictionaryKey.newInstance(code, type));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public DataDictionary save(DataDictionary dd) {
        return this.dataDictionaryService.save(dd);
    }

    @RequestMapping(value = "/{type}:{code}", method = RequestMethod.GET)
    public DataDictionary view(@PathVariable("type") String type, @PathVariable("code") String code) {
        return this.dataDictionaryService.get(DataDictionaryKey.newInstance(code, type));
    }

    @RequestMapping(method = RequestMethod.GET)
    public Pager<DataDictionary> search(Pager<DataDictionary> pager, List<PropertyFilter> filters) {
        return this.dataDictionaryService.findPager(pager, filters);
    }

}
