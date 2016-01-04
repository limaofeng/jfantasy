package org.jfantasy.mall.delivery.bean.databind;


import org.jfantasy.mall.delivery.bean.DeliveryCorp;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DeliveryCorpSerializer extends JsonSerializer<DeliveryCorp> {

    @Override
    public void serialize(DeliveryCorp corp, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (corp == null) {
            jgen.writeNull();
        } else {
            jgen.writeNumber(corp.getId());
        }
    }
}
