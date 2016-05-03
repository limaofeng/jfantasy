package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.pay.bean.Payment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "orders-services", description = "订单")
@RestController
@RequestMapping("/order-services")
public class OrderServiceController {

    @ApiOperation("查询支付记录")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<String> search(Pager<Payment> pager, List<PropertyFilter> filters) {
        return null;
    }

}
