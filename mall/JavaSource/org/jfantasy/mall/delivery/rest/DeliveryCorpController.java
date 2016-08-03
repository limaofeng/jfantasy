package org.jfantasy.mall.delivery.rest;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.mall.delivery.bean.DeliveryCorp;
import org.jfantasy.mall.delivery.service.DeliveryCorpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "delivery-corps", description = "配送的物流公司")
@RestController
@RequestMapping("/delivery/corps")
public class DeliveryCorpController {

    @Autowired
    private DeliveryCorpService deliveryCorpService;

    @ApiOperation(value = "按条件检索物流公司", notes = "筛选物流公司，返回通用分页对象",response = DeliveryCorp[].class)
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<DeliveryCorp> search(Pager<DeliveryCorp> pager,@ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.deliveryCorpService.findPager(pager, filters);
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
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
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
