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

    /**
     * @api {post} /ddts   添加分类
     * @apiVersion 3.3.3
     * @apiName createDDT
     * @apiGroup 数据字典
     * @apiDescription 通过该接口, 可以添加新的数据字典分类。
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X POST -d "param1=value1&param2=value2..." http://localhost/ddts
     * @apiUse GeneralError
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public DataDictionaryType save(DataDictionaryType ddt) {
        return this.dataDictionaryService.save(ddt);
    }

    /**
     * @api {get} /ddts/:code   获取分类
     * @apiVersion 3.3.3
     * @apiName getDDT
     * @apiGroup 数据字典
     * @apiDescription 通过该接口, 可以获取<code>code</code>对应的字典分类信息。
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/ddts/usersex
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{code}", method = RequestMethod.GET)
    public DataDictionaryType view(@PathVariable("code") String code) {
        return this.dataDictionaryService.getDataDictionaryType(code);
    }

    /**
     * @api {get} /ddts/:code/dds  获取分类下的字典项
     * @apiVersion 3.3.3
     * @apiName getDDSBYDDTS
     * @apiGroup 数据字典
     * @apiDescription 通过该接口, 可以获取<code>code</code>对应的所有字典信息。
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/ddts/usersex/dds
     * @apiExample Response (example):
     * HTTP/1.1 200
     * {
     * "message": "删除成功"
     * }
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{code}/dds", method = RequestMethod.GET)
    public List<DataDictionary> dds(@PathVariable("code") String code) {
        return this.dataDictionaryService.find(Restrictions.eq("type", code));
    }

    @RequestMapping(method = RequestMethod.GET)
    public Pager<DataDictionaryType> search(Pager<DataDictionaryType> pager, List<PropertyFilter> filters) {
        return this.dataDictionaryService.findDataDictionaryTypePager(pager, filters);
    }

    /**
     * @api {delete} /ddts/:code   删除分类
     * @apiVersion 3.3.3
     * @apiName deleteDdt
     * @apiGroup 数据字典
     * @apiDescription 通过该接口删除数据字典分类。
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/ddts/usersex
     * @apiExample Response (example):
     * HTTP/1.1 200
     * {
     * "message": "删除成功"
     * }
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{code}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("code") String code) {
        this.dataDictionaryService.deleteType(code);
    }

}
