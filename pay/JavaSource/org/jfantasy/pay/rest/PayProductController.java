package org.jfantasy.pay.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.PayProduct;
import org.jfantasy.pay.rest.form.PayForm01;
import org.jfantasy.pay.service.PayConfigService;
import org.jfantasy.pay.service.PayProductConfiguration;
import org.jfantasy.pay.service.PayService;
import org.jfantasy.pay.service.vo.ToPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "payproducts", description = "支付产品")
@RestController
@RequestMapping("/payproducts")
public class PayProductController {

    @Autowired
    private PayProductConfiguration payProductConfiguration;
    @Autowired
    private PayConfigService payConfigService;
    @Autowired
    private PayService payService;

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

    @ApiOperation(value = "适用于该支付产品的支付配置", notes = "查看产品的支付配置信息")
    @RequestMapping(value = "/{id}/payconfigs", method = RequestMethod.GET)
    @ResponseBody
    public Pager<PayConfig> payconfigs(@PathVariable("id") String id, Pager<PayConfig> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("payProductId", id));
        return this.payConfigService.findPager(pager, filters);
    }

    @ApiOperation(value = "支付", notes = "如果支付配置只有一个,可以使用该接口进行支付")
    @RequestMapping(value = "/{id}/pay", method = RequestMethod.POST)
    @ResponseBody
    public ToPayment pay(@PathVariable("id") String id, @RequestBody PayForm01 payForm) throws PayException {
        PayProduct payProduct = payProductConfiguration.loadPayProduct(id);
        if (payProduct == null) {
            throw new PayException("支付产品标示 [" + id + " ]不存在,请检测后,再调用接口. 使用 /payproducts 可以查看全部支持的支付产品");
        }
        List<PayConfig> payConfigs = this.payConfigService.find(Restrictions.eq("payProductId", id));
        if (payConfigs.size() != 1) {
            throw new PayException("支付产品 [" + payProduct.getName() + " ] " + (payConfigs.size() > 1 ? "对应多份配置,请使用 /pays 接口进行支付" : "未配置,请先配置后,再调用该接口"));
        }
        return payService.pay(payConfigs.get(0).getId(), payForm.getPayType(), payForm.getOrderType(), payForm.getOrderSn(), payForm.getPayer());
    }

}
