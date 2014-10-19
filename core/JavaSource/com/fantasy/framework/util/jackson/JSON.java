package com.fantasy.framework.util.jackson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.codehaus.jackson.type.TypeReference;

import com.fantasy.framework.util.jackson.serializer.StringUnicodeSerializer;

public class JSON {

	private static final Log logger = LogFactory.getLog(JSON.class);

	private static final ObjectMapper objectMapper = new ObjectMapper();
	static {
		// 当找不到对应的序列化器时 忽略此字段
		objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		// 允许非空字段
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// 允许单引号
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		// 失败在未知属性
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		CustomSerializerFactory serializerFactory = new HibernateAwareSerializerFactory();
		serializerFactory.addSpecificMapping(String.class, new StringUnicodeSerializer());

		objectMapper.setSerializerFactory(serializerFactory);

		objectMapper.getDeserializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		objectMapper.getSerializationConfig().setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		
	}

	public static String serialize(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	public static Object deserialize(String json) {
		return deserialize(json, HashMap.class);
	}
	
	public static <T> T deserialize(String json, Class<T> classed) {
		try {
			return objectMapper.readValue(json, classed);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] deserialize(String json, T[] classed) {
		try {
			return (T[]) objectMapper.readValue(json, classed.getClass());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String json, TypeReference<T> typeReference) {
		try {
			return (T) objectMapper.readValue(json, typeReference);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
}