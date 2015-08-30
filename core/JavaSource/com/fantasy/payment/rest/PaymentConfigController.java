package com.fantasy.payment.rest;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.error.PaymentException;
import com.fantasy.payment.service.PaymentConfigService;
import com.fantasy.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/system/payconfigs")
public class PaymentConfigController {

    @Autowired
    private PaymentConfigService configService;
    @Autowired
    private PaymentService paymentService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Pager<PaymentConfig> search(Pager<PaymentConfig> pager, List<PropertyFilter> filters) {
        return this.configService.findPager(pager, filters);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PaymentConfig create(@RequestBody PaymentConfig config) {
        return this.configService.save(config);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public PaymentConfig update(@PathVariable("id") Long id, @RequestBody PaymentConfig config) {
        config.setId(id);
        return this.configService.save(config);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public PaymentConfig view(@PathVariable("id") Long id) {
        return this.configService.get(id);
    }

    @RequestMapping(value = "/{id}/test", method = RequestMethod.GET)
    @ResponseBody
    public String test(@PathVariable("id") Long paymentConfigId, HttpServletRequest request) throws IOException, PaymentException {
        return this.paymentService.test(paymentConfigId, WebUtil.getParameterMap(request));
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
