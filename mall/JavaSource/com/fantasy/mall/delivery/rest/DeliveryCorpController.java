package com.fantasy.mall.delivery.rest;

import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.mall.delivery.bean.DeliveryCorp;
import com.fantasy.mall.delivery.service.DeliveryCorpService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "delivery-corps", description = "物流公司")
@RestController
@RequestMapping("/delivery/corps")
public class DeliveryCorpController {

    @Autowired
    private DeliveryCorpService deliveryCorpService;

    @ApiOperation(value = "按条件检索物流公司", notes = "筛选物流公司，返回通用分页对象")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<DeliveryCorp> search(@ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.deliveryCorpService.find(filters);
    }

    @ApiOperation(value = "获取物流公司", notes = "通过该接口, 获取单篇物流公司", response = DeliveryCorp.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DeliveryCorp view(@PathVariable("id") Long id) {
        return this.deliveryCorpService.get(id);
    }

    @ApiOperation(value = "添加物流公司", notes = "通过该接口, 添加物流公司", response = DeliveryCorp.class)
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public DeliveryCorp create(@RequestBody DeliveryCorp corp) {
        return this.deliveryCorpService.save(corp);
    }

    @ApiOperation(value = "更新物流公司", notes = "通过该接口, 更新物流公司")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public DeliveryCorp update(@PathVariable("id") Long id, @RequestBody DeliveryCorp corp) {
        corp.setId(id);
        return this.deliveryCorpService.save(corp);
    }

    @ApiOperation(value = "删除物流公司", notes = "通过该接口, 删除物流公司")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        this.deliveryCorpService.delete(id);
    }

    @ApiOperation(value = "批量删除物流公司", notes = "通过该接口, 批量删除物流公司")
    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestBody Long... id) {
        this.deliveryCorpService.delete(id);
    }


}
