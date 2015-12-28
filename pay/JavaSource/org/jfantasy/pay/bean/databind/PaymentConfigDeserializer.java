package org.jfantasy.pay.bean.databind;


import com.fantasy.framework.util.common.StringUtil;
import org.jfantasy.pay.bean.PayConfig;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class PaymentConfigDeserializer extends JsonDeserializer<PayConfig> {

    @Override
    public PayConfig deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Long value = jp.getValueAsLong();
        if (StringUtil.isBlank(value)) {
            return null;
        }
        return new PayConfig(value);
    }

}
