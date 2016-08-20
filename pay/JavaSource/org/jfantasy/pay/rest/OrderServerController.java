package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.pay.bean.OrderServer;
import org.jfantasy.pay.rest.models.OrderServerForm;
import org.jfantasy.pay.service.OrderServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "orders-server", description = "订单服务")
@RestController
@RequestMapping("/order-server")
public class OrderServerController {

    private final OrderServerService orderServerService;

    @Autowired
    public OrderServerController(OrderServerService orderServerService) {
        this.orderServerService = orderServerService;
    }

    @ApiOperation("查询订单服务")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<OrderServer> search(Pager<OrderServer> pager, List<PropertyFilter> filters) {
        return orderServerService.findPager(pager, filters);
    }

    @ApiOperation("保存订单服务")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public OrderServer create(@RequestBody OrderServerForm form){
        return orderServerService.save(form.getCallType(),form.getUrl(),form.getDescription(),form.getProperties());
    }

}
