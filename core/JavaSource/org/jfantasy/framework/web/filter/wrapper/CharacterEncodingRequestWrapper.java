package org.jfantasy.framework.web.filter.wrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.web.WebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

public class CharacterEncodingRequestWrapper extends HttpServletRequestWrapper {

    protected static final Log LOG = LogFactory.getLog(CharacterEncodingRequestWrapper.class);

    private Map<String, String[]> parameterMaps = new LinkedHashMap<String, String[]>();

    public CharacterEncodingRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        return ObjectUtil.defaultValue(values, new String[0]).length == 0 ? null : values[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (parameterMaps.containsKey(name))
            return parameterMaps.get(name);
        if (values == null || values.length == 0)
            return values;
        String[] newValues = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            if (Charset.forName("ASCII").newEncoder().canEncode(values[i])) {
                newValues[i] = StringUtil.decodeURI(values[i].replaceAll("\\+","%2B"), getRequest().getCharacterEncoding());
                LOG.debug(name + " 的原始编码为[ASCII]转编码:" + values[i] + "=>" + newValues[i]);
            } else if (Charset.forName("ISO-8859-1").newEncoder().canEncode(values[i])) {
                newValues[i] = WebUtil.transformCoding(values[i], "ISO-8859-1", getRequest().getCharacterEncoding());
                LOG.debug(name + " 的原始编码为[ISO-8859-1]转编码:" + values[i] + "=>" + newValues[i]);
            } else {
                newValues[i] = values[i];
            }
        }
        parameterMaps.put(name, newValues);
        return parameterMaps.get(name);
    }

    @Override
    @SuppressWarnings("unchecked")
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
