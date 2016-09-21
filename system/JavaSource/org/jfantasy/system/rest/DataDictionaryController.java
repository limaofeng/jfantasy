package org.jfantasy.system.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.system.bean.DataDictionary;
import org.jfantasy.system.bean.DataDictionaryKey;
import org.jfantasy.system.service.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典
 **/
@RestController
@RequestMapping("/system/dds")
public class DataDictionaryController {

    @Autowired
    private DataDictionaryService dataDictionaryService;

    /**
     * 删除数据字典 - 删除数据字典
     **/
    @RequestMapping(value = "/{type}:{code}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("type") String type, @PathVariable("code") String code) {
        this.dataDictionaryService.delete(DataDictionaryKey.newInstance(code, type));
    }

    /**
     * 批量删除数据字典 - 删除数据字典
     **/
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@RequestBody String... keys) {
        this.dataDictionaryService.delete(keys);
    }

    /**
     * 获取数据项<br/>
     * 通过该接口, 可以添加新的数据项。
     *
     * @param type
     * @param code
     * @return
     */
    @RequestMapping(value = "/{type}:{code}", method = RequestMethod.GET)
    @ResponseBody
    public DataDictionary view(@PathVariable("type") String type, @PathVariable("code") String code) {
        return this.dataDictionaryService.get(DataDictionaryKey.newInstance(code, type));
    }

    /**
     * 查询数据字典<br/>
     * 通过该接口, 可以筛选需要的数据字典项。
     * @param pager
     * @param filters
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<DataDictionary> search(Pager<DataDictionary> pager, List<PropertyFilter> filters) {
        return this.dataDictionaryService.findPager(pager, filters);
    }

    /**
     * 更新数据项 <br/>
     * 通过该接口, 可以更新新的数据项。
     * @param type
     * @param code
     * @param dataDictionary
     * @return
     */
    @RequestMapping(value = "/{type}:{code}", method = RequestMethod.PUT)
    @ResponseBody
    public DataDictionary update(@PathVariable("type") String type, @PathVariable("code") String code, @RequestBody DataDictionary dataDictionary) {
        dataDictionary.setCode(code);
        dataDictionary.setType(type);
        return this.dataDictionaryService.update(dataDictionary);
    }

}
