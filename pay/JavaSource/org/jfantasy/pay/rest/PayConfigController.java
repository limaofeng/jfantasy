package org.jfantasy.pay.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.annotation.AllowProperty;
import org.jfantasy.framework.jackson.annotation.IgnoreProperty;
import org.jfantasy.framework.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.spring.mvc.hateoas.ResultResourceSupport;
import org.jfantasy.pay.bean.Order;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.Parameters;
import org.jfantasy.pay.product.PayProduct;
import org.jfantasy.pay.rest.models.assembler.PayConfigResourceAssembler;
import org.jfantasy.pay.service.PayConfigService;
import org.jfantasy.pay.service.PayProductConfiguration;
import org.jfantasy.pay.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Api(value = "payconfigs", description = "支付配置")
@RestController
@RequestMapping("/payconfigs")
public class PayConfigController {

    private PayConfigResourceAssembler assembler = new PayConfigResourceAssembler();

    @Autowired
    private PayConfigService configService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PayProductConfiguration payProductConfiguration;
    @Autowired
    private PaymentController paymentController;

    @JsonIgnoreProperties({@IgnoreProperty(pojo = PayConfig.class, name = {"properties"})})
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> search(Pager<PayConfig> pager, List<PropertyFilter> filters) {
        return assembler.toResources(this.configService.findPager(pager, filters));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PayConfig create(@RequestBody PayConfig config) {
        return this.configService.save(config);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public PayConfig update(@PathVariable("id") Long id, @RequestBody PayConfig config) {
        config.setId(id);
        return this.configService.save(config);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResultResourceSupport view(@PathVariable("id") Long id) {
        return assembler.toResource(this.configService.get(id));
    }

    @RequestMapping(value = "/{id}/payproduct", method = RequestMethod.POST)
    @ResponseBody
    public PayProduct test(@PathVariable("id") Long id) throws IOException, PayException {
        return this.payProductConfiguration.loadPayProduct(this.configService.get(id).getPayProductId());
    }

    @RequestMapping(value = "/{id}/test", method = RequestMethod.POST)
    @ResponseBody
    public String test(@PathVariable("id") Long paymentConfigId, @RequestBody Parameters parameters) throws IOException, PayException {
        return this.paymentService.test(paymentConfigId, parameters);
    }

    @ApiOperation("删除支付配置")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.configService.delete(id);
    }

    @JsonIgnoreProperties(
            value = @IgnoreProperty(pojo = Payment.class, name = {"payConfig"}),
            allow = @AllowProperty(pojo = Order.class, name = {"type", "subject", "sn"})
    )
    @ApiOperation("支付配置对应的支付记录")
    @RequestMapping(value = "/{id}/payments", method = RequestMethod.GET)
    @ResponseBody
    public Pager<ResultResourceSupport> payments(@PathVariable("id") String id, Pager<Payment> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQL_payConfig.id", id));
        return paymentController.search(pager, filters);
    }

}