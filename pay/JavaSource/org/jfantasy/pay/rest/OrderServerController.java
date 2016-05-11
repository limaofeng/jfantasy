package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.pay.bean.OrderServer;
import org.jfantasy.pay.service.OrderServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "orders-server", description = "订单服务")
@RestController
@RequestMapping("/order-server")
public class OrderServerController {

    @Autowired
    private OrderServerService orderServerService;

    @ApiOperation("查询订单服务")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<OrderServer> search(Pager<OrderServer> pager, List<PropertyFilter> filters) {
        return orderServerService.findPager(pager, filters);
    }

}
