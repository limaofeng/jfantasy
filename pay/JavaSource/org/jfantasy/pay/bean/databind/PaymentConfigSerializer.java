package org.jfantasy.pay.bean.databind;


import org.jfantasy.pay.bean.PayConfig;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PaymentConfigSerializer extends JsonSerializer<PayConfig> {

    @Override
    public void serialize(PayConfig payConfig, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (payConfig == null) {
            jgen.writeNull();
        } else {
            jgen.writeNumber(payConfig.getId());
        }
    }
}
