package com.fantasy.mall.delivery.bean.databind;


import com.fantasy.mall.delivery.bean.DeliveryType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DeliveryTypeSerializer extends JsonSerializer<DeliveryType> {

    @Override
    public void serialize(DeliveryType deliveryType, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (deliveryType == null) {
            jgen.writeNull();
        } else {
            jgen.writeNumber(deliveryType.getId());
        }
    }
}
