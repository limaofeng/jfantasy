package com.fantasy.payment.ws.client;

import com.fantasy.framework.ws.axis2.WebServiceClient;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.payment.ws.IPaymentService;
import com.fantasy.payment.ws.dto.PaymentConfigDTO;
import com.fantasy.payment.ws.dto.PaymentDTO;

import java.util.Map;

/**
 * 支付 webservice
 */
public class PaymentService extends WebServiceClient implements IPaymentService {

    public PaymentService() {
        super("PaymentService");
    }

    @Override
    public PaymentConfigDTO[] find(PropertyFilterDTO[] filters) {
        return this.invokeOption("find", new Object[]{filters}, PaymentConfigDTO[].class);
    }

    @Override
    public String buildRequest(String orderType, String orderSn, Long paymentConfigId, String payMember, Map<String, String> parameters) {
        return this.invokeOption("buildRequest", new Object[]{orderType, orderSn, paymentConfigId, payMember, parameters}, String.class);
    }

    @Override
    public String payreturn(String sn, Map<String, String> parameters) {
        return this.invokeOption("payreturn", new Object[]{sn, parameters}, String.class);
    }

    @Override
    public String paynotify(String sn, Map<String, String> parameters) {
        return this.invokeOption("paynotify", new Object[]{sn, parameters}, String.class);
    }

    @Override
    public PaymentDTO getPayment(String sn) {
        return this.invokeOption("getPayment", new Object[]{sn}, PaymentDTO.class);
    }

}
