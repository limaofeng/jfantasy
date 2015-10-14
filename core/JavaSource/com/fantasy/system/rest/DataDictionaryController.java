package com.fantasy.system.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.system.bean.DataDictionary;
import com.fantasy.system.bean.DataDictionaryKey;
import com.fantasy.system.service.DataDictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "system-dds", description = "数据字典")
@RestController
@RequestMapping("/system/dds")
public class DataDictionaryController {

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @ApiOperation(value = "删除数据字典", notes = "删除数据字典")
    @RequestMapping(value = "/{type}:{code}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("type") String type, @PathVariable("code") String code) {
        this.dataDictionaryService.delete(DataDictionaryKey.newInstance(code, type));
    }

    @ApiOperation(value = "批量删除数据字典", notes = "删除数据字典")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String... keys) {
        this.dataDictionaryService.delete(keys);
    }

    @ApiOperation(value = "获取数据项", notes = "通过该接口, 可以添加新的数据项。", response = DataDictionary.class)
    @RequestMapping(value = "/{type}:{code}", method = RequestMethod.GET)
    @ResponseBody
    public DataDictionary view(@PathVariable("type") String type, @PathVariable("code") String code) {
        return this.dataDictionaryService.get(DataDictionaryKey.newInstance(code, type));
    }

    @ApiOperation(value = "查询数据字典", notes = "通过该接口, 可以筛选需要的数据字典项。")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<DataDictionary> search(@ApiParam(value = "分页对象", name = "pager") Pager<DataDictionary> pager, @ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.dataDictionaryService.findPager(pager, filters);
    }

    @ApiOperation(value = "更新数据项", notes = "通过该接口, 可以更新新的数据项。", response = DataDictionary.class)
    @RequestMapping(value = "/{type}:{code}", method = RequestMethod.PUT)
    @ResponseBody
    public DataDictionary update(@PathVariable("type") String type, @PathVariable("code") String code, @RequestBody DataDictionary dataDictionary) {
        dataDictionary.setCode(code);
        dataDictionary.setType(type);
        return this.dataDictionaryService.save(dataDictionary);
    }

}
