package org.jfantasy.framework.util.jackson.serializer;

import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.StringUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

public class DateSerializer extends JsonSerializer<Date> {

	private String dateFormat;

	public DateSerializer(String dateFormat){
		this.dateFormat = dateFormat;
	}

	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeString(StringUtil.isNotBlank(dateFormat) ? DateUtil.format(value, dateFormat) : provider.getConfig().getDateFormat().format(value));
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

}