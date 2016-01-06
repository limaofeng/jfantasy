package org.jfantasy.framework.util.reflect;

import java.util.HashMap;
import java.util.Map;

public class FastClassFactory implements IClassFactory {
    private Map<String, FastClasses<?>> classes = new HashMap<String, FastClasses<?>>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> IClass<T> getClass(Class<T> cla) {
        synchronized (this.classes) {
            if (!this.classes.containsKey(cla.getName())) {
                this.classes.put(cla.getName(), new FastClasses(cla));
            }
            return (IClass) this.classes.get(cla.getName());
        }
    }
}