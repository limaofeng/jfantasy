package com.fantasy.payment.ws.server;

import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.framework.ws.util.WebServiceUtil;
import com.fantasy.payment.bean.Payment;
import com.fantasy.payment.bean.PaymentConfig;
import com.fantasy.payment.error.PaymentException;
import com.fantasy.payment.service.PaymentConfigService;
import com.fantasy.payment.service.PaymentService;
import com.fantasy.payment.ws.IPaymentService;
import com.fantasy.payment.ws.dto.PaymentConfigDTO;
import com.fantasy.payment.ws.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.WebServiceException;
import java.util.List;
import java.util.Map;

@Component
public class PaymentWebService implements IPaymentService {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentConfigService paymentConfigService;

    @Override
    public PaymentConfigDTO[] find(PropertyFilterDTO[] filters) {
        return asArray(paymentConfigService.find(WebServiceUtil.toFilters(filters)));
    }

    @Override
    public String buildRequest(String orderType, String orderSn, Long paymentConfigId, String payMember, Map<String, String> parameters) {
        try {
            return paymentService.buildRequest(orderType, orderSn, paymentConfigId, payMember, parameters);
        } catch (PaymentException e) {
            throw new WebServiceException(e.getMessage(), e);
        }
    }

    @Override
    public String payreturn(String sn, Map<String, String> parameters) {
        try {
            return paymentService.payreturn(sn, parameters);
        } catch (PaymentException e) {
            throw new WebServiceException(e.getMessage(), e);
        }
    }

    @Override
    public String paynotify(String sn, Map<String, String> parameters) {
        try {
            return paymentService.paynotify(sn, parameters);
        } catch (PaymentException e) {
            throw new WebServiceException(e.getMessage(), e);
        }
    }

    @Override
    public PaymentDTO getPayment(String sn) {
        return asDto(paymentService.get(sn));
    }

    private static PaymentDTO asDto(Payment payment) {
        if (payment == null) {
            return null;
        }
        PaymentDTO dto = new PaymentDTO();
        BeanUtil.copyProperties(dto, payment);
        return dto;
    }

    private static PaymentConfigDTO asDto(PaymentConfig paymentConfig) {
        if (paymentConfig == null) {
            return null;
        }
        PaymentConfigDTO dto = new PaymentConfigDTO();
        BeanUtil.copyProperties(dto, paymentConfig);
        return dto;
    }

    private PaymentConfigDTO[] asArray(List<PaymentConfig> paymentConfigs) {
        PaymentConfigDTO[] dtos = new PaymentConfigDTO[paymentConfigs.size()];
        for (int i = 0; i < dtos.length; i++) {
            dtos[i] = asDto(paymentConfigs.get(i));
        }
        return dtos;
    }

}
