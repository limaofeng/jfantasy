package com.fantasy.framework.util.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import java.io.IOException;

public class HibernateAwareSerializerFactory extends BeanSerializerFactory {

	protected HibernateAwareSerializerFactory(SerializerFactoryConfig config) {
		super(config);
	}




	/*
	private static final String FIELD_HANDLER_PROPERTY_NAME = "fieldHandler";

	@Override
	public JsonSerializer<Object> createSerializer(SerializerProvider prov, JavaType type) throws JsonMappingException  {
		Class<?> clazz = type.getRawClass();
		if (PersistentCollection.class.isAssignableFrom(clazz)) {
			return new PersistentCollectionSerializer(type);
		}
		if (HibernateProxy.class.isAssignableFrom(clazz)) {
			return new HibernateProxySerializer(type);
		}
		return super.createSerializer(prov,type);
	}*/

    /*
	@Override
	protected JsonSerializer<Object> findSerializerFromAnnotation(SerializationConfig config, Annotated annotated, BeanProperty property) throws JsonMappingException  {
		JsonSerializer<Object> serializer = super.findSerializerFromAnnotation(config, annotated, property);
		if ((Object) serializer instanceof DateSerializer && annotated instanceof AnnotatedMethod) {
			DateFormat dateFormat = ((AnnotatedMethod) annotated).getAnnotated().getAnnotation(DateFormat.class);
			((DateSerializer) (Object) serializer).setDateFormat(dateFormat != null ? dateFormat.pattern() : null);
		}
		return serializer;
	}*/

    /*
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
	}*/

    /*
	private void filterInstrumentedBeanProperties(BasicBeanDescription beanDesc, List<BeanPropertyWriter> props) {
		if (!FieldHandled.class.isAssignableFrom(beanDesc.getBeanClass())) {
			return;
		}
		for (Iterator<BeanPropertyWriter> iter = props.iterator(); iter.hasNext();) {
			if (iter.next().getName().equals(FIELD_HANDLER_PROPERTY_NAME)) {
				iter.remove();
			}
		}
	}*/

	private class PersistentCollectionSerializer extends JsonSerializer<Object> {
		private final JavaType type;
//		private final SerializationConfig config;
//		private final BeanProperty beanProperty;

		private PersistentCollectionSerializer( JavaType type) {
//			this.config = config;
			this.type = type;
//			this.beanProperty = property;
		}

		@Override
		@SuppressWarnings({ "unchecked" })
		public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
			if (!((PersistentCollection) value).wasInitialized()) {
				jgen.writeNull();
				return;
			}
            /*
			BasicBeanDescription beanDesc = config.introspect(type);
//			boolean staticTyping = false;
			JavaType elementType = type.getContentType();
			TypeSerializer elementTypeSerializer = createTypeSerializer(provider.getConfig(), elementType);

			JsonSerializer elementValueSerializer = _findContentSerializer(config, beanDesc.getClassInfo(), beanProperty);

			JsonSerializer<Object> serializer = null;
			if (type.isMapLikeType()) {
				MapLikeType mlt = (MapLikeType) type;
				JsonSerializer keySerializer = _findKeySerializer(config, beanDesc.getClassInfo(), beanProperty);
				if (mlt.isTrueMapType()) {
					serializer = (JsonSerializer<Object>) buildMapSerializer(config, (MapType) mlt, beanDesc, beanProperty, false, keySerializer, elementTypeSerializer, elementValueSerializer);
				}else {
                    serializer = (JsonSerializer<Object>) _buildMapLikeSerializer(config, mlt, beanDesc, beanProperty, false, keySerializer, elementTypeSerializer, elementValueSerializer);
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
			*/
		}

    }

	private class HibernateProxySerializer extends JsonSerializer<Object> {
		private final JavaType type;

		private HibernateProxySerializer(JavaType type) {
			this.type = type;
		}

		@Override
		public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
			LazyInitializer lazyInitializer = ((HibernateProxy) value).getHibernateLazyInitializer();
			if (lazyInitializer.isUninitialized()) {
				lazyInitializer.initialize();
			}
			BasicBeanDescription beanDesc = provider.getConfig().introspect(type);
			JsonSerializer<Object> serializer = findBeanSerializer(provider, type, beanDesc);
			serializer.serialize(value, jgen, provider);
		}
	}
}
