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

    /**
     * @api {post} /dds   添加数据项
     * @apiVersion 3.3.6
     * @apiName createDD
     * @apiGroup 数据字典
     * @apiDescription 通过该接口, 可以添加新的数据项。
     * @apiPermission admin
     * @apiParam {String}   code            编码
     * @apiParam {String}   type            分类编码
     * @apiParam {String}   name            名称
     * @apiParam {Integer}   sort           排序
     * @apiParam {String}   description     描述
     * @apiParam {String}   parent_code     上级编码
     * @apiParam {String}   parent_type     上级分类编码
     * @apiExample Example usage:
     * curl -i -X POST -d "code=sex&type=usersex..." http://localhost/dds
     * @apiUse GeneralError
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public DataDictionary save(DataDictionary dd) {
        return this.dataDictionaryService.save(dd);
    }

    /**
     * @api {post} /dds/:key   获取数据项
     * @apiVersion 3.3.6
     * @apiName getDD
     * @apiGroup 数据字典
     * @apiDescription 通过该接口, 可以添加新的数据项。
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/dds/usersex:female
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{type}:{code}", method = RequestMethod.GET)
    public DataDictionary view(@PathVariable("type") String type, @PathVariable("code") String code) {
        return this.dataDictionaryService.get(DataDictionaryKey.newInstance(code, type));
    }

    /**
     * @api {get} /dds   查询数据项
     * @apiVersion 3.3.6
     * @apiName findDDS
     * @apiGroup 数据字典
     * @apiDescription 通过该接口, 可以筛选需要的数据字典项。
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/dds?currentPage=1&EQS_code=usersex
     * @apiUse GeneralError
     */
    @RequestMapping(method = RequestMethod.GET)
    public Pager<DataDictionary> search(Pager<DataDictionary> pager, List<PropertyFilter> filters) {
        return this.dataDictionaryService.findPager(pager, filters);
    }

}
