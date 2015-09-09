package com.fantasy.mall.delivery.rest;

import com.fantasy.common.order.Order;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.mall.delivery.bean.DeliveryCorp;
import com.fantasy.mall.delivery.bean.DeliveryItem;
import com.fantasy.mall.delivery.bean.DeliveryType;
import com.fantasy.mall.delivery.bean.Shipping;
import com.fantasy.mall.delivery.service.ShippingService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "delivery-shippings", description = "送货信息")
@RestController
@RequestMapping("/delivery/shippings")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @ApiOperation(value = "按条件检索送货信息", notes = "筛选送货信息，返回通用分页对象")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<Shipping> search(@ApiParam(value = "分页对象", name = "pager") Pager<Shipping> pager, @ApiParam(value = "过滤条件", name = "filters") List<PropertyFilter> filters) {
        return this.shippingService.findPager(pager, filters);
    }

    @ApiOperation(value = "获取送货信息", notes = "通过该接口, 获取单篇送货信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Shipping view(@PathVariable("id") Long id) {
        return this.shippingService.get(id);
    }

    @ApiOperation(value = "送货信息的详细物流项", notes = "送货信息的详细物流项")
    @RequestMapping(value = "/{id}/items", method = RequestMethod.GET)
    @ResponseBody
    public List<DeliveryItem> deliveryItems(@PathVariable("id") Long id) {
        return this.shippingService.get(id).getDeliveryItems();
    }

    @ApiOperation(value = "送货信息对应的订单信息", notes = "送货信息对应的订单信息")
    @RequestMapping(value = "/{id}/order", method = RequestMethod.GET)
    @ResponseBody
    public Order order(@PathVariable("id") Long id) {
        return this.shippingService.getOrder(id);
    }

    @ApiOperation(value = "送货信息的配送方式", notes = "送货信息的配送方式")
    @RequestMapping(value = "/{id}/type", method = RequestMethod.GET)
    @ResponseBody
    public DeliveryType deliveryType(@PathVariable("id") Long id) {
        return this.shippingService.get(id).getDeliveryType();
    }

    @ApiOperation(value = "送货信息的配送物流公司", notes = "送货信息的配送物流公司")
    @RequestMapping(value = "/{id}/corp", method = RequestMethod.GET)
    @ResponseBody
    public DeliveryCorp deliveryCorp(@PathVariable("id") Long id) {
        return this.shippingService.get(id).getDeliveryType().getDefaultDeliveryCorp();
    }

    @ApiOperation(value = "添加送货信息", notes = "通过该接口, 添加送货信息")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Shipping create(@RequestBody Shipping shipping) {
        return this.shippingService.save(shipping);
    }

    @ApiOperation(value = "删除送货信息", notes = "通过该接口, 删除送货信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable("id") Long id) {
        this.shippingService.delete(id);
    }

    @ApiOperation(value = "批量删除送货信息", notes = "通过该接口, 批量删除送货信息")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@RequestBody Long... id) {
        this.shippingService.delete(id);
    }

}
