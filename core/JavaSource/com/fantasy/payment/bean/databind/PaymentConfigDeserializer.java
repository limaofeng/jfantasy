package com.fantasy.payment.bean.databind;


import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.payment.bean.PaymentConfig;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class PaymentConfigDeserializer extends JsonDeserializer<PaymentConfig> {

    @Override
    public PaymentConfig deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Long value = jp.getValueAsLong();
        if (StringUtil.isBlank(value)) {
            return null;
        }
        return new PaymentConfig(value);
    }

}
