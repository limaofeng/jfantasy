package org.jfantasy.framework.spring.mvc.http.converter;


import org.jfantasy.framework.util.common.MessageDigestUtil;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.ClassUtils;
import org.springframework.util.TypeUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

public class SecurityMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter implements InitializingBean {

    private static final boolean jackson26Available = ClassUtils.hasMethod(ObjectMapper.class, "setDefaultPrettyPrinter", PrettyPrinter.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setObjectMapper(JSON.getObjectMapper());
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonGenerator generator = this.objectMapper.getFactory().createGenerator(output, encoding);
        try {
            writePrefix(generator, object);

            Class<?> serializationView = null;
            FilterProvider filters = null;
            Object value = object;
            JavaType javaType = null;
            if (object instanceof MappingJacksonValue) {
                MappingJacksonValue container = (MappingJacksonValue) object;
                value = container.getValue();
                serializationView = container.getSerializationView();
                filters = container.getFilters();
            }
            if (jackson26Available && type != null && value != null && TypeUtils.isAssignable(type, value.getClass())) {
                javaType = getJavaType(type, null);
            }
            ObjectWriter objectWriter;
            if (serializationView != null) {
                objectWriter = this.objectMapper.writerWithView(serializationView);
            } else if (filters != null) {
                objectWriter = this.objectMapper.writer(filters);
            } else {
                objectWriter = this.objectMapper.writer();
            }
            if (javaType != null && javaType.isContainerType()) {
                objectWriter = objectWriter.withType(javaType);
            }
            objectWriter.writeValue(generator, value);

            writeSuffix(generator, object);
            generator.flush();

            byte[] bytes;
            InputStream input = new ByteArrayInputStream(bytes = output.toByteArray());
            String sign = MessageDigestUtil.getInstance().bufferToHex(MessageDigestUtil.getInstance().get(bytes));
            System.out.println(new String(bytes) + "\n\n" + sign);
            input.reset();
            outputMessage.getHeaders().set("sign", sign.toUpperCase());
            StreamUtil.copy(input, outputMessage.getBody());
            //为了防止签名伪造  text+key + 加密
        } catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write content: " + ex.getMessage(), ex);
        }
    }


}
