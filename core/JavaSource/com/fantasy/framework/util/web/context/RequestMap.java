package com.fantasy.framework.util.web.context;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"rawtypes" })
public class RequestMap extends AbstractMap implements Serializable {

    private static final long serialVersionUID = -7675640869293787926L;

    private Set<Object> entries;
    private transient HttpServletRequest request;

    public RequestMap(final HttpServletRequest request) {
        this.request = request;
    }

    public void clear() {
        entries = null;
        Enumeration keys = request.getAttributeNames();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            request.removeAttribute(key);
        }
    }

    public Set entrySet() {
        if (entries == null) {
            entries = new HashSet<Object>();
            Enumeration enumeration = request.getAttributeNames();
            while (enumeration.hasMoreElements()) {
                final String key = enumeration.nextElement().toString();
                final Object value = request.getAttribute(key);
                if(ActionContext.CONTAINER.equals(key)){
                    continue;
                }
                entries.add(new Entry() {
                    public boolean equals(Object obj) {
                        if (!(obj instanceof Entry)) {
                            return false;
                        }
                        Entry entry = (Entry) obj;
                        return ((key == null) ? (entry.getKey() == null) : key.equals(entry.getKey())) && ((value == null) ? (entry.getValue() == null) : value.equals(entry.getValue()));
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
                        request.setAttribute(key, obj);
                        return value;
                    }
                });
            }
        }
        return entries;
    }

    public Object get(Object key) {
        return request.getAttribute(key.toString());
    }

    public Object put(Object key, Object value) {
        Object oldValue = get(key);
        entries = null;
        request.setAttribute(key.toString(), value);
        return oldValue;
    }

    public Object remove(Object key) {
        entries = null;
        Object value = get(key);
        request.removeAttribute(key.toString());
        return value;
    }
}
