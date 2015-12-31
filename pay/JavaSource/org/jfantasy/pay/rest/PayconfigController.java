package org.jfantasy.pay.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import io.swagger.annotations.Api;
import org.jfantasy.pay.bean.PayConfig;
import org.jfantasy.pay.error.PayException;
import org.jfantasy.pay.product.Parameters;
import org.jfantasy.pay.service.PayConfigService;
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

    @Autowired
    private PayConfigService configService;
    @Autowired
    private PaymentService paymentService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<PayConfig> search(Pager<PayConfig> pager, List<PropertyFilter> filters) {
        return this.configService.findPager(pager, filters);
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
    public PayConfig view(@PathVariable("id") Long id) {
        return this.configService.get(id);
    }

    @RequestMapping(value = "/{id}/test", method = RequestMethod.POST)
    @ResponseBody
    public String test(@PathVariable("id") Long paymentConfigId, @RequestBody Parameters parameters) throws IOException, PayException {
        return this.paymentService.test(paymentConfigId, parameters);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        this.configService.delete(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void batchDelete(@RequestBody Long... id) {
        this.configService.delete(id);
    }

}
