package com.fantasy.system.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.system.bean.DataDictionary;
import com.fantasy.system.bean.DataDictionaryKey;
import com.fantasy.system.service.DataDictionaryService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
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

    /**
     * @api {post} /dds   添加数据项
     * @apiVersion 3.3.8
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
    @ApiOperation(value = "添加数据项", notes = "通过该接口, 可以添加新的数据项。", response = DataDictionary.class)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public DataDictionary save(@RequestBody DataDictionary dd) {
        return this.dataDictionaryService.save(dd);
    }

    /**
     * @api {post} /dds/:key   获取数据项
     * @apiVersion 3.3.8
     * @apiName getDD
     * @apiGroup 数据字典
     * @apiDescription 通过该接口, 可以添加新的数据项。
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/dds/usersex:female
     * @apiUse GeneralError
     */
    @ApiOperation(value = "获取数据项", notes = "通过该接口, 可以添加新的数据项。", response = DataDictionary.class)
    @RequestMapping(value = "/{type}:{code}", method = RequestMethod.GET)
    @ResponseBody
    public DataDictionary view(@PathVariable("type") String type, @PathVariable("code") String code) {
        return this.dataDictionaryService.get(DataDictionaryKey.newInstance(code, type));
    }

    /**
     * @api {get} /dds   查询数据项
     * @apiVersion 3.3.8
     * @apiName findDDS
     * @apiGroup 数据字典
     * @apiDescription 通过该接口, 可以筛选需要的数据字典项。
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/dds?currentPage=1&EQS_code=usersex
     * @apiUse GeneralError
     */
    @ApiOperation(value = "查询数据字典", notes = "通过该接口, 可以筛选需要的数据字典项。")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<DataDictionary> search(@ApiParam(value = "分页对象", name = "pager") Pager<DataDictionary> pager, @ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.dataDictionaryService.findPager(pager, filters);
    }

}
