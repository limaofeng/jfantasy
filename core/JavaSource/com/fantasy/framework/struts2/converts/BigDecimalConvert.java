package com.fantasy.framework.struts2.converts;

import org.apache.struts2.util.StrutsTypeConverter;

import java.math.BigDecimal;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class BigDecimalConvert extends StrutsTypeConverter {
    
	public Object convertFromString(Map context, String[] values, Class toClass) {
	if (BigDecimal.class == toClass) {
	    String doubleStr = values[0];
	    BigDecimal b = null;
	    if (!"".equals(doubleStr)) {
		b = BigDecimal.valueOf(doubleValue(doubleStr));
	    }
	    return b;
	}
	return null;
    }

    public String convertToString(Map context, Object o) {
	if ((o instanceof BigDecimal)) {
	    BigDecimal b = ((BigDecimal) o).setScale(2, 5);
	    return b.toString();
	}
	return o.toString();
    }
}