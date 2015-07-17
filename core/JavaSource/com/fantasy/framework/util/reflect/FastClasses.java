package com.fantasy.framework.util.reflect;

import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import net.sf.cglib.reflect.FastClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastClasses<T> implements IClass<T> {
	private static final Log logger = LogFactory.getLog(FastClasses.class);
	private Class<T> clazz;
	private FastClass fastClass;
	private BeanInfo beanInfo;
	private Map<String, Property> propertys = new HashMap<String, Property>();
	private Map<String, MethodProxy> methodProxys = new HashMap<String, MethodProxy>();
	private Map<Class<?>, Constructor<T>> constructors = new HashMap<Class<?>, Constructor<T>>();
	private Map<String, Field> fields = new HashMap<String, Field>();

	@SuppressWarnings("unchecked")
	public FastClasses(Class<T> clazz) {
		this.clazz = clazz;
		this.fastClass = FastClass.create(clazz);
		if (!clazz.isInterface()) {
			this.beanInfo = ClassUtil.getBeanInfo(clazz);
			PropertyDescriptor[] propertyDescriptors = this.beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				MethodProxy readMethodProxy = descriptor.getReadMethod() == null ? null : new MethodProxy(descriptor.getReadMethod());// this.fastClass.getMethod(descriptor.getReadMethod())
				MethodProxy writeMethodProxy = descriptor.getWriteMethod() == null ? null : new MethodProxy(descriptor.getWriteMethod(), descriptor.getPropertyType());// this.fastClass.getMethod()
				this.propertys.put(descriptor.getName(), new Property(descriptor.getName(), readMethodProxy, writeMethodProxy, descriptor.getPropertyType()));
			}
			for (Method method : this.clazz.getDeclaredMethods()) {
				Class<?>[] parameters = method.getParameterTypes();
				StringBuffer name = new StringBuffer(method.getName());
				if (parameters.length != 0) {
					for (int i = 0; i < parameters.length; i++) {
						Class<?> parameterType = parameters[i];
						name.append(i == 0 ? "(" : "");
						name.append(parameterType.getName());
						name.append(i + 1 == parameters.length ? ")" : ",");
					}
				} else {
					name.append("()");
				}
				try {
					if (method.isAccessible()) {
						this.methodProxys.put(name.toString(), new MethodProxy(this.fastClass.getMethod(method), parameters));
					} else {
						if (!method.isAccessible()) {
							method.setAccessible(true);
						}
						this.methodProxys.put(name.toString(), new MethodProxy(method, parameters));
					}
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
			for (Constructor<?> constructor : clazz.getConstructors()) {
				Class<?>[] parameterTypes = constructor.getParameterTypes();
				if (parameterTypes.length == 1) {
					this.constructors.put(parameterTypes[0], (Constructor<T>) constructor);
				}
			}
		} else {
			for (Method method : clazz.getDeclaredMethods()) {
				String name = method.getName();
				Class<?>[] parameters = method.getParameterTypes();
				for (int i = 0; i < parameters.length; i++) {
					Class<?> parameterType = parameters[i];
					name = name + (i == 0 ? "(" : "") + parameterType.getName() + (i + 1 == parameters.length ? ")" : ",");
				}
				this.methodProxys.put(name, new MethodProxy(this.fastClass.getMethod(method), parameters));
			}
		}
		for (Class<?> superClass = clazz; superClass != Object.class;) {
			for (Field field : filterFields(superClass.getDeclaredFields())) {
				if (!this.fields.containsKey(field.getName())) {
					this.fields.put(field.getName(), field);
				}
			}
			superClass = superClass.getSuperclass();
		}
	}

	private List<Field> filterFields(Field[] fields) {
		List<Field> result = new ArrayList<Field>();
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers())) {
				field.setAccessible(true);
				result.add(field);
			}
		}
		return result;
	}

	public T newInstance() {
		try {
			return this.clazz.newInstance();
		} catch (Exception e) {
			throw new IgnoreException(e.getMessage(),e);
		}
	}

	public T newInstance(Object object) {
		try {
			if (object == null){
                return newInstance();
            }
			return newInstance(object.getClass(), object);
		} catch (Exception e) {
			throw new IgnoreException(e.getMessage(),e);
		}
	}

	public T newInstance(Class<?> type, Object object) {
		try {
			return this.constructors.get(type).newInstance(new Object[] { object });
		} catch (Exception e) {
			throw new IgnoreException(e.getMessage(),e);
		}
	}

	public Property getProperty(String name) {
		if (this.propertys.containsKey(name)) {
			return this.propertys.get(name);
		}
		return null;
	}

	public Property[] getPropertys() {
		return this.propertys.values().toArray(new Property[this.propertys.size()]);
	}

	public MethodProxy getMethod(String methodName) {
		MethodProxy methodProxy = (MethodProxy) this.methodProxys.get(methodName + "()");
		if (ObjectUtil.isNotNull(methodProxy)){
            return methodProxy;
        }
		for (Map.Entry<String, MethodProxy> entry : this.methodProxys.entrySet()) {
			if (entry.getKey().equals(methodName) || entry.getKey().startsWith(methodName + "(")) {
				return entry.getValue();
			}
		}
		if (this.clazz.getSuperclass() != Object.class) {
			return ClassUtil.getMethodProxy(this.clazz.getSuperclass(), methodName);
		}
		return null;
	}

	public MethodProxy getMethod(String methodName, Class<?>... parameterTypes) {
		if (parameterTypes.length != 0) {
			for (int i = 0; i < parameterTypes.length; i++) {
				methodName = methodName + (i == 0 ? "(" : "") + parameterTypes[i].getName() + (i + 1 == parameterTypes.length ? ")" : ",");
			}
		} else {
			methodName += "()";
		}
		return this.methodProxys.get(methodName);
	}

	public void setValue(Object target, String name, Object value) {
		Field field = null;
		if (!this.fields.containsKey(name)){
            try {
                field = ClassUtil.getDeclaredField(this.fastClass.getJavaClass(), name);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                this.fields.put(name, field);
            } catch (Exception e) {
                logger.error(e);
                this.fields.put(name, field);
            }
        }else{
            field = (Field) this.fields.get(name);
        }
		try {
			if (field != null) {
                field.set(target, value);
            }else{
                throw new IgnoreException("没有找到[" + this.fastClass.getName() + "." + name + "]对应的属性!");
            }
		} catch (Exception e) {
			throw new IgnoreException(e.getMessage(),e);
		}
	}

	public Object getValue(Object target, String name) {
		Field field = null;
		if (!this.fields.containsKey(name)) {
			try {
				field = ClassUtil.getDeclaredField(this.fastClass.getJavaClass(), name);
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				this.fields.put(name, field);
			} catch (Exception e) {
				logger.error(e);
				this.fields.put(name, field);
			}
		} else {
			field = (Field) this.fields.get(name);
		}
		try {
			return field != null ? field.get(target) : null;
		} catch (Exception ex) {
			throw new IgnoreException("没有找到[" + this.fastClass.getName() + "." + name + "]对应的属性!"+ex.getMessage(),ex);
		}
	}

	public T newInstance(Class<?>[] parameterTypes, Object[] parameters) {
		if (parameterTypes.length == 0){
            return newInstance();
        }
		if (parameterTypes.length == 1) {
			return newInstance(parameterTypes[0], parameters[0]);
		}
		throw new IgnoreException("还不支持多个参数的构造器");
	}

	public Field[] getDeclaredFields() {
		return this.fields.values().toArray(new Field[this.fields.size()]);
	}

	public Field getDeclaredField(String fieldName) {
		return this.fields.get(fieldName);
	}

	public Field[] getDeclaredFields(Class<? extends Annotation> annotClass) {
		List<Field> fields = new ArrayList<Field>();
		for (Field field : this.fields.values()) {
			if (field.isAnnotationPresent(annotClass)) {
				fields.add(field);
			}
		}
		return (Field[]) fields.toArray(new Field[fields.size()]);
	}
}