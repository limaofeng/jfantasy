package com.fantasy.payment.bean.databind;


import com.fantasy.payment.bean.PaymentConfig;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PaymentConfigSerializer extends JsonSerializer<PaymentConfig> {

    @Override
    public void serialize(PaymentConfig paymentConfig, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (paymentConfig == null) {
            jgen.writeNull();
        } else {
            jgen.writeNumber(paymentConfig.getId());
        }
    }
}
