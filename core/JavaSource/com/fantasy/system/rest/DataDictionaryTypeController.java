package com.fantasy.system.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.system.bean.DataDictionary;
import com.fantasy.system.bean.DataDictionaryType;
import com.fantasy.system.service.DataDictionaryService;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ddts")
public class DataDictionaryTypeController {

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @RequestMapping(value = "/{code}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("code") String code) {
        this.dataDictionaryService.deleteType(code);
    }

    @RequestMapping(value = "/{code}/dds", method = RequestMethod.GET)
    public List<DataDictionary> dds(@PathVariable("code") String code) {
        return this.dataDictionaryService.find(Restrictions.eq("type",code));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public DataDictionaryType save(DataDictionaryType ddt) {
        return this.dataDictionaryService.save(ddt);
    }

    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public DataDictionaryType view(@PathVariable("code") String code) {
        return this.dataDictionaryService.getDataDictionaryType(code);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Pager<DataDictionaryType> search(Pager<DataDictionaryType> pager,List<PropertyFilter> filters) {
        return this.dataDictionaryService.findDataDictionaryTypePager(pager, filters);
    }

}
