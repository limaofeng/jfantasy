package com.fantasy.payment.ws.dto;

import com.fantasy.framework.ws.util.WSResult;

/**
 * 提交支付DTO
 */
public class SubmitPaymentDTO extends WSResult {

    private String paymentUrl;

    private ParameterDTO[] parameterDTOs;

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public ParameterDTO[] getParameterDTOs() {
        return parameterDTOs;
    }

    public void setParameterDTOs(ParameterDTO[] parameterDTOs) {
        this.parameterDTOs = parameterDTOs;
    }
}
