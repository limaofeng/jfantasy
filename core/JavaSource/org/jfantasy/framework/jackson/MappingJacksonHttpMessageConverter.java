package org.jfantasy.framework.jackson;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;

public class MappingJacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public MappingJacksonHttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        try {
            super.writeInternal(object, type, outputMessage);
        }finally {
            ThreadJacksonMixInHolder.clear();
        }
    }

}
