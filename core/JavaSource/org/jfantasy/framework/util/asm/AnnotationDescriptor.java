package org.jfantasy.framework.util.asm;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationDescriptor {

    private final Class<? extends Annotation> type;

    private Map<String, Object> elements = new HashMap<String, Object>();

    public AnnotationDescriptor(Class<? extends Annotation> annotationType) {
        type = annotationType;
    }

    public void setValue(String elementName, Object value) {
        elements.put( elementName, value );
    }

    public Object valueOf(String elementName) {
        return elements.get( elementName );
    }

    public boolean containsElement(String elementName) {
        return elements.containsKey( elementName );
    }

    public Set<String> keys(){
        return elements.keySet();
    }

    public int numberOfElements() {
        return elements.size();
    }

    public Class<? extends Annotation> type() {
        return type;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Class<? extends Annotation> type) {
        return new Builder(type);
    }

    public static class Builder{

        private Class<? extends Annotation> type;
        private final Map<String, Object> elements = new HashMap<String, Object>();

        private Builder() {
        }

        private Builder(Class<? extends Annotation> type) {
            this.type = type;
        }

        public AnnotationDescriptor.Builder type(Class<? extends Annotation> clas) {
            this.type = clas;
            return this;
        }

        public AnnotationDescriptor.Builder setValue(String key, String value) {
            this.elements.put(key,value);
            return this;
        }

        public AnnotationDescriptor build() {
            AnnotationDescriptor descriptor = new AnnotationDescriptor(this.type);
            descriptor.elements = elements;
            return descriptor;
        }
    }

}
