package com.fantasy.framework.util.jackson;

import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.util.jackson.serializer.DateSerializer;
import com.fantasy.framework.util.ognl.typeConverter.DateFormat;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.codehaus.jackson.map.type.CollectionLikeType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapLikeType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.type.JavaType;
import org.hibernate.bytecode.internal.javassist.FieldHandled;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class HibernateAwareSerializerFactory extends CustomSerializerFactory {

	protected HibernateAwareSerializerFactory() {
	}

	private static final String FIELD_HANDLER_PROPERTY_NAME = "fieldHandler";

	@Override
	public JsonSerializer<Object> createSerializer(SerializationConfig config, JavaType type, BeanProperty property) throws JsonMappingException /*throws JsonMappingException*/ {
		Class<?> clazz = type.getRawClass();
		if (PersistentCollection.class.isAssignableFrom(clazz)) {
			return new PersistentCollectionSerializer(config, type, property);
		}
		if (HibernateProxy.class.isAssignableFrom(clazz)) {
			return new HibernateProxySerializer(config, type, property);
		}
		return super.createSerializer(config, type, property);
	}

	@Override
	protected JsonSerializer<Object> findSerializerFromAnnotation(SerializationConfig config, Annotated annotated, BeanProperty property) throws JsonMappingException /*throws JsonMappingException*/ {
		JsonSerializer<Object> serializer = super.findSerializerFromAnnotation(config, annotated, property);
		if ((Object) serializer instanceof DateSerializer && annotated instanceof AnnotatedMethod) {
			DateFormat dateFormat = ((AnnotatedMethod) annotated).getAnnotated().getAnnotation(DateFormat.class);
			((DateSerializer) (Object) serializer).setDateFormat(dateFormat != null ? dateFormat.pattern() : null);
		}
		return serializer;
	}

	@Override
	protected List<BeanPropertyWriter> filterBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyWriter> props) {
		props = super.filterBeanProperties(config, beanDesc, props);
		filterInstrumentedBeanProperties(beanDesc, props);
//		List<String> transientOnes = new ArrayList<String>();
//		// 去掉瞬态字段
//		for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(beanDesc.getBeanClass())) {
//			Method getter = pd.getReadMethod();
//			if (getter != null && AnnotationUtils.findAnnotation(getter, Transient.class) != null) {
//				transientOnes.add(pd.getName());
//			}
//		}
//		for (Iterator<BeanPropertyWriter> iter = props.iterator(); iter.hasNext();) {
//			if (transientOnes.contains(iter.next().getName())) {
//				iter.remove();
//			}
//		}
		return props;
	}

	private void filterInstrumentedBeanProperties(BasicBeanDescription beanDesc, List<BeanPropertyWriter> props) {
		if (!FieldHandled.class.isAssignableFrom(beanDesc.getBeanClass())) {
			return;
		}
		for (Iterator<BeanPropertyWriter> iter = props.iterator(); iter.hasNext();) {
			if (iter.next().getName().equals(FIELD_HANDLER_PROPERTY_NAME)) {
				iter.remove();
			}
		}
	}

	private class PersistentCollectionSerializer extends JsonSerializer<Object> {
		private final JavaType type;
		private final SerializationConfig config;
		private final BeanProperty beanProperty;

		private PersistentCollectionSerializer(SerializationConfig config, JavaType type, BeanProperty property) {
			this.config = config;
			this.type = type;
			this.beanProperty = property;
		}

		@Override
		@SuppressWarnings({ "unchecked" })
		public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
			if (!((PersistentCollection) value).wasInitialized()) {
				jgen.writeNull();
				return;
			}
			BasicBeanDescription beanDesc = config.introspect(type);
//			boolean staticTyping = false;
			JavaType elementType = type.getContentType();
			TypeSerializer elementTypeSerializer = createTypeSerializer(config, elementType, beanProperty);

			JsonSerializer elementValueSerializer = findContentSerializer(config, beanDesc.getClassInfo(), beanProperty);

			JsonSerializer<Object> serializer = null;
			if (type.isMapLikeType()) {
				MapLikeType mlt = (MapLikeType) type;
				JsonSerializer keySerializer = findKeySerializer(config, beanDesc.getClassInfo(), beanProperty);
				if (mlt.isTrueMapType()) {
					serializer = (JsonSerializer<Object>) buildMapSerializer(config, (MapType) mlt, beanDesc, beanProperty, false, keySerializer, elementTypeSerializer, elementValueSerializer);
				}else {
                    serializer = (JsonSerializer<Object>) buildMapLikeSerializer(config, mlt, beanDesc, beanProperty, false, keySerializer, elementTypeSerializer, elementValueSerializer);
                }
			} else if (type.isCollectionLikeType()) {
				CollectionLikeType clt = (CollectionLikeType) type;
				if (clt.isTrueCollectionType()) {
					serializer = (JsonSerializer<Object>) buildCollectionSerializer(config, (CollectionType) clt, beanDesc, beanProperty, false, elementTypeSerializer, elementValueSerializer);
				} else {
					serializer = (JsonSerializer<Object>) buildCollectionLikeSerializer(config, clt, beanDesc, beanProperty, false, elementTypeSerializer, elementValueSerializer);
				}
			}
			if (serializer == null) {
				throw new IgnoreException(" JsonSerializer is null !");
			}
//			Class<?> clazz = type.getRawClass();
//			JsonSerializer<Object> serializer;
//			if (PersistentMap.class.isAssignableFrom(clazz)) {
//				serializer = (JsonSerializer<Object>) buildMapSerializer(config, type, beanDesc, beanProperty);
//			} else {
//				serializer = (JsonSerializer<Object>) buildCollectionSerializer(config, type, beanDesc, beanProperty);
//			}
			serializer.serialize(value, jgen, provider);
		}


    }

	private class HibernateProxySerializer extends JsonSerializer<Object> {
		private final JavaType type;
		private final SerializationConfig config;
		private final BeanProperty beanProperty;

		private HibernateProxySerializer(SerializationConfig config, JavaType type, BeanProperty property) {
			this.config = config;
			this.type = type;
			this.beanProperty = property;
		}

		@Override
		public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
			LazyInitializer lazyInitializer = ((HibernateProxy) value).getHibernateLazyInitializer();
			if (lazyInitializer.isUninitialized()) {
				lazyInitializer.initialize();
			}
			BasicBeanDescription beanDesc = config.introspect(type);
			JsonSerializer<Object> serializer = findBeanSerializer(config, type, beanDesc, beanProperty);
			serializer.serialize(value, jgen, provider);
		}
	}
}
