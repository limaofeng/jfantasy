package com.fantasy.mall.delivery.rest;

import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.mall.delivery.bean.DeliveryType;
import com.fantasy.mall.delivery.service.DeliveryTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "delivery-types", description = "配送方式")
@RestController
@RequestMapping("/delivery/types")
public class DeliveryTypeController {

    @Autowired
    private DeliveryTypeService deliveryTypeService;

    @ApiOperation(value = "按条件检索配送方式", notes = "筛选配送方式，返回结果集")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<DeliveryType> search(@ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.deliveryTypeService.find(filters);
    }

    @ApiOperation(value = "按配送类型检索配送方式", notes = "按配送类型检索配送方式")
    @RequestMapping(value = "/{method}", method = RequestMethod.GET)
    @ResponseBody
    public List<DeliveryType> searchByMethod(@PathVariable("method") DeliveryType.DeliveryMethod method) {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQE_method", method));
        return this.deliveryTypeService.find(filters);
    }

    @ApiOperation(value = "获取配送方式", notes = "通过该接口, 获取单篇配送方式", response = DeliveryType.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DeliveryType view(@PathVariable("id") Long id) {
        return this.deliveryTypeService.get(id);
    }

    @ApiOperation(value = "添加配送方式", notes = "通过该接口, 添加配送方式", response = DeliveryType.class)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public DeliveryType create(@RequestBody DeliveryType type) {
        return this.deliveryTypeService.save(type);
    }

    @ApiOperation(value = "更新配送方式", notes = "通过该接口, 更新配送方式")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public DeliveryType update(@PathVariable("id") Long id, @RequestBody DeliveryType type) {
        type.setId(id);
        return this.deliveryTypeService.save(type);
    }

    @ApiOperation(value = "删除配送方式", notes = "通过该接口, 删除配送方式")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void delete(@PathVariable("id") Long id) {
        this.deliveryTypeService.delete(id);
    }

    @ApiOperation(value = "批量删除配送方式", notes = "通过该接口, 批量删除配送方式")
    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestBody Long... id) {
        this.deliveryTypeService.delete(id);
    }

}
