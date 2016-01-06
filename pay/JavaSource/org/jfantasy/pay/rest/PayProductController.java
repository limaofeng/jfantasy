package org.jfantasy.pay.rest;


import org.jfantasy.pay.product.PayProduct;
import org.jfantasy.pay.service.PayProductConfiguration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "payproducts", description = "支付产品")
@RestController
@RequestMapping("/payproducts")
public class PayProductController {

    @Autowired
    private PayProductConfiguration payProductConfiguration;

    @ApiOperation(value = "获取支付产品", notes = "通过该接口, 可以获取支持的支付产品列表")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<PayProduct> search() {
        return payProductConfiguration.getPayProducts();
    }

    @ApiOperation(value = "支付产品详情", notes = "通过该接口, 可以获取支付产品详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public PayProduct view(@PathVariable("id") String id) {
        return payProductConfiguration.loadPayProduct(id);
    }

}
