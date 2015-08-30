package com.fantasy.payment.rest;


import com.fantasy.payment.product.PaymentProduct;
import com.fantasy.payment.service.PaymentConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/payproducts")
public class PaymentProductController {

    @Autowired
    private PaymentConfiguration paymentConfiguration;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<PaymentProduct> search() {
        return paymentConfiguration.getPaymentProducts();
    }

}
