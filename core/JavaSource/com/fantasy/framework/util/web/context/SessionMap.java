package com.fantasy.framework.util.web.context;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@SuppressWarnings("unchecked")
public class SessionMap<K, V> extends AbstractMap<K, V> implements Serializable {
    private static final long serialVersionUID = 4678843241638046854L;
    protected transient HttpSession session;
    protected Set<Map.Entry<K, V>> entries;
    protected transient HttpServletRequest request;

    public SessionMap(HttpServletRequest request) {
        this.request = request;
        this.session = request.getSession(false);
    }

    public void invalidate() {
        if (session == null) {
            return;
        }
        session.invalidate();
        session = null;
        entries = null;
    }

    public void clear() {
        if (session == null) {
            return;
        }
        synchronized (session) {
            entries = null;
            Enumeration<String> attributeNamesEnum = session.getAttributeNames();
            while (attributeNamesEnum.hasMoreElements()) {
                session.removeAttribute(attributeNamesEnum.nextElement());
            }
        }
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
        if (session == null) {
            return Collections.emptySet();
        }
        synchronized (session) {
            if (entries == null) {
                entries = new HashSet<Map.Entry<K, V>>();
                Enumeration<? extends Object> enumeration = session.getAttributeNames();
                while (enumeration.hasMoreElements()) {
                    final String key = enumeration.nextElement().toString();
                    final Object value = session.getAttribute(key);
                    entries.add(new Map.Entry<K, V>() {
                        public boolean equals(Object obj) {
                            if (!(obj instanceof Map.Entry)) {
                                return false;
                            }
                            Map.Entry<K, V> entry = (Map.Entry<K, V>) obj;
                            return ((key == null) ? (entry.getKey() == null) : key.equals(entry.getKey())) && ((value == null) ? (entry.getValue() == null) : value.equals(entry.getValue()));
                        }
                        public int hashCode() {
                            return ((key == null) ? 0 : key.hashCode()) ^ ((value == null) ? 0 : value.hashCode());
                        }
                        public K getKey() {
                            return (K) key;
                        }
                        public V getValue() {
                            return (V) value;
                        }
                        public V setValue(Object obj) {
                            session.setAttribute(key, obj);

                            return (V) value;
                        }
                    });
                }
            }
        }
        return entries;
    }

    public V get(Object key) {
        if (session == null) {
            return null;
        }
        synchronized (session) {
            return (V) session.getAttribute(key.toString());
        }
    }

    public V put(K key, V value) {
        synchronized (this) {
            if (session == null) {
                session = request.getSession(true);
            }
        }
        synchronized (session) {
            V oldValue = get(key);
            entries = null;
            session.setAttribute(key.toString(), value);
            return oldValue;
        }
    }

    public V remove(Object key) {
        if (session == null) {
            return null;
        }

        synchronized (session) {
            entries = null;

            V value = get(key);
            session.removeAttribute(key.toString());

            return value;
        }
    }

    public boolean containsKey(Object key) {
        if (session == null) {
            return false;
        }
        synchronized (session) {
            return (session.getAttribute(key.toString()) != null);
        }
    }
}
