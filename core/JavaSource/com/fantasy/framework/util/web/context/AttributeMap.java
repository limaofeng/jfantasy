package com.fantasy.framework.util.web.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.PageContext;

@SuppressWarnings("rawtypes")
public class AttributeMap implements Map {
	protected static final String UNSUPPORTED = "method makes no sense for a simplified map";
	private static final Object PAGE_CONTEXT = null;
	Map context;
	private final static Log LOGGER = LogFactory.getLog(AttributeMap.class);

	public AttributeMap(Map context) {
		this.context = context;
	}

	public boolean isEmpty() {
		throw new UnsupportedOperationException(UNSUPPORTED);
	}

	public void clear() {
		throw new UnsupportedOperationException(UNSUPPORTED);
	}

	public boolean containsKey(Object key) {
		return get(key) != null;
	}

	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException(UNSUPPORTED);
	}

	public Set entrySet() {
		return Collections.EMPTY_SET;
	}

	public Object get(Object key) {
		PageContext pc = getPageContext();
		if (pc == null) {
			Map request = (Map) context.get("request");
			Map session = (Map) context.get("session");
			Map application = (Map) context.get("application");
			if ((request != null) && (request.get(key) != null)) {
				return request.get(key);
			} else if ((session != null) && (session.get(key) != null)) {
				return session.get(key);
			} else if ((application != null) && (application.get(key) != null)) {
				return application.get(key);
			}
		} else {
			try {
				return pc.findAttribute(key.toString());
			} catch (NullPointerException npe) {
				LOGGER.error(npe.getMessage(),npe);
				return null;
			}
		}
		return null;
	}

	public Set keySet() {
		return Collections.EMPTY_SET;
	}

	public Object put(Object key, Object value) {
		PageContext pc = getPageContext();
		if (pc != null) {
			pc.setAttribute(key.toString(), value);
		}
		return null;
	}

	public void putAll(Map t) {
		throw new UnsupportedOperationException(UNSUPPORTED);
	}

	public Object remove(Object key) {
		throw new UnsupportedOperationException(UNSUPPORTED);
	}

	public int size() {
		throw new UnsupportedOperationException(UNSUPPORTED);
	}

	public Collection values() {
		return Collections.EMPTY_SET;
	}

	private PageContext getPageContext() {
		return (PageContext) context.get(PAGE_CONTEXT);
	}
}
