package org.jfantasy.express.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jfantasy.express.bean.Express;
import org.jfantasy.express.service.ExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "express", description = "快递公司接口")
@RestController
@RequestMapping("/expresses")
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @ApiOperation(value = "按条件检索快递公司", notes = "筛选快递公司，返回通用分页对象", response = Express[].class)
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Express> search(Pager<Express> pager, @ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.expressService.findPager(pager, filters);
    }

    @ApiOperation(value = "获取快递公司", notes = "通过该接口, 获取单篇快递公司", response = Express.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Express view(@PathVariable("id") String id) {
        return this.expressService.get(id);
    }

    @ApiOperation(value = "添加快递公司", notes = "通过该接口, 添加快递公司", response = Express.class)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Express create(@RequestBody Express express) {
        return this.expressService.save(express);
    }

    @ApiOperation(value = "更新快递公司", notes = "通过该接口, 更新快递公司")
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public Express update(@PathVariable("id") String id, @RequestBody Express express) {
        express.setId(id);
        return this.expressService.save(express);
    }

    @ApiOperation(value = "删除快递公司", notes = "通过该接口, 删除快递公司")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) {
        this.expressService.delete(id);
    }

    @ApiOperation(value = "批量删除快递公司", notes = "通过该接口, 批量删除快递公司")
    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestBody String... id) {
        this.expressService.delete(id);
    }

    @ApiOperation(value = "查询快递的物流信息", notes = "查询快递的物流信息")
    @RequestMapping(value = "/{id}/bills/{sn}", method = RequestMethod.GET)
    public void bill(@PathVariable("id") String id, @PathVariable("sn") String sn) {

    }

}
