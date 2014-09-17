package com.fantasy.framework.util.jackson.serializer;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.StringUtil;

public class DateSerializer extends JsonSerializer<Date> {

	private String dateFormat;

	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeString(StringUtil.isNotBlank(dateFormat) ? DateUtil.format(value, dateFormat) : provider.getConfig().getDateFormat().format(value));
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

}