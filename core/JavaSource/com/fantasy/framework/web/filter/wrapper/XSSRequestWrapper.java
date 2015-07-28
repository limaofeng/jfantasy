package com.fantasy.framework.web.filter.wrapper;

import com.fantasy.framework.util.common.Base64Util;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.framework.util.web.WebUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;

public class XSSRequestWrapper extends HttpServletRequestWrapper {

    private static final Log LOGGER = LogFactory.getLog(XSSRequestWrapper.class);

    private Map<String, String[]> parameterMaps = new HashMap<String, String[]>();

    private boolean transform = false;

    public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public XSSRequestWrapper(HttpServletRequest request, boolean transform) {
        super(request);
        this.transform = transform;
    }

    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        return values == null || values.length == 0 ? null : values[0];
    }

    public String[] getParameterValues(String name) {
        if (parameterMaps.containsKey(name)) {
            return parameterMaps.get(name);
        }
        String[] values = super.getParameterValues(name);
        if (StringUtil.isNull(values)) {
            return values;
        }
        Object vals = ClassUtil.newInstance(String.class, values.length);
        for (int i = 0; i < values.length; i++) {
            String escapeStr = null;
            // 普通Html过滤
            if ("GET".equalsIgnoreCase(WebUtil.getMethod((HttpServletRequest) this.getRequest())) && isTransform()) {
                escapeStr = WebUtil.transformCoding(values[i], "8859_1", this.getRequest().getCharacterEncoding());
                escapeStr = StringUtil.isNotBlank(escapeStr) ? HtmlUtils.htmlEscape(escapeStr) : escapeStr;
            } else {
                escapeStr = StringUtil.isNotBlank(values[i]) ? HtmlUtils.htmlEscape(values[i]) : values[i];
            }
            // 防止链接注入 (如果以http开头的参数,同时与请求的域不相同的话,将值64位编码)
            if (RegexpUtil.find(escapeStr, "^http://") && !WebUtil.getRequestUrl((HttpServletRequest) super.getRequest()).startsWith(escapeStr)) {
                escapeStr = "base64:" + new String(Base64Util.encode(escapeStr.getBytes()));
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(name + "[" + values[i] + "]" + " => htmlEscape => [" + escapeStr + "]");
            }
            Array.set(vals, i, escapeStr);
        }
        parameterMaps.put(name, (String[]) vals);
        return parameterMaps.get(name);
    }

    private boolean isTransform() {
        return transform;
    }

    @Override
    @SuppressWarnings({"unchecked", "serial", "rawtypes"})
    public Map getParameterMap() {
        return new HashMap(super.getParameterMap()) {

            private Set<Object> entries;

            @Override
            public Object get(Object key) {
                return XSSRequestWrapper.this.getParameterValues(StringUtil.nullValue(key));
            }

            public Set entrySet() {
                if (entries == null) {
                    entries = new HashSet<Object>();
                    Enumeration enumeration = XSSRequestWrapper.this.getParameterNames();
                    while (enumeration.hasMoreElements()) {
                        final String key = enumeration.nextElement().toString();
                        final Object value = XSSRequestWrapper.this.getParameterValues(key);
                        entries.add(new Entry() {

							public boolean equals(Object obj) {
								if (!(obj instanceof Entry)) {
									return false;
								}
								Entry entry = (Entry) obj;
								return key == null ? (entry.getKey() == null) : key.equals(entry.getKey()) && value == null ? (entry.getValue() == null) : value.equals(entry.getValue());
							}

                            public int hashCode() {
                                return ((key == null) ? 0 : key.hashCode()) ^ ((value == null) ? 0 : value.hashCode());
                            }

                            public Object getKey() {
                                return key;
                            }

                            public Object getValue() {
                                return value;
                            }

                            public Object setValue(Object obj) {
                                return value;
                            }

                        });
                    }
                }
                return entries;
            }

        };
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    public Enumeration getParameterNames() {
        return XSSRequestWrapper.super.getParameterNames();
    }

}
