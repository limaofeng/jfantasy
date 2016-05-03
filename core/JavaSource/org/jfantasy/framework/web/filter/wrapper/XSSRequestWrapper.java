package org.jfantasy.framework.web.filter.wrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.util.common.Base64Util;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.lang.reflect.Array;
import java.util.*;

public class XSSRequestWrapper extends HttpServletRequestWrapper {

    private static final Log LOGGER = LogFactory.getLog(XSSRequestWrapper.class);

    private Map<String, String[]> parameterMaps = new LinkedHashMap<String, String[]>();

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
            String escapeStr;
            // 普通Html过滤
            if ("GET".equalsIgnoreCase(WebUtil.getMethod((HttpServletRequest) this.getRequest())) && isTransform()) {
                escapeStr = WebUtil.transformCoding(values[i], "8859_1", this.getRequest().getCharacterEncoding());
                escapeStr = StringUtil.isNotBlank(escapeStr) ? HtmlUtils.htmlEscape(escapeStr) : escapeStr;
            } else {
                escapeStr = StringUtil.isNotBlank(values[i]) ? HtmlUtils.htmlEscape(values[i]) : values[i];
            }
            // 防止链接注入 (如果以http开头的参数,同时与请求的域不相同的话,将值64位编码)
            if (RegexpUtil.find(escapeStr, "^http://") && !WebUtil.getServerUrl((HttpServletRequest) super.getRequest()).startsWith(escapeStr)) {
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
    public Map<String, String[]> getParameterMap() {
        Enumeration<String> enumeration = super.getParameterNames();
        if (parameterMaps.size() == super.getParameterMap().size())
            return parameterMaps;
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            if (parameterMaps.containsKey(key)) {
                continue;
            }
            parameterMaps.put(key, getParameterValues(key));
        }
        return parameterMaps;
    }

}
