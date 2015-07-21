package com.fantasy.framework.dao.mybatis.proxy;

import com.fantasy.framework.dao.Pager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

/**
 * MyBatis Mapper 代理
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-10-28 下午08:33:14
 * @version 1.0
 */
public class MyBatisMapperProxy implements InvocationHandler {
	protected final Log logger = LogFactory.getLog(MyBatisMapperProxy.class);

	private SqlSession sqlSession;

	/**
	 * 不需要代理的方法
	 */
	private static final Set<String> OBJECT_METHODS = new HashSet<String>() {
		private static final long serialVersionUID = -1782950882770203583L;
		{
			add("toString");
			add("getClass");
			add("hashCode");
			add("equals");
			add("wait");
			add("notify");
			add("notifyAll");
		}
	};

	private boolean isObjectMethod(Method method) {
		return OBJECT_METHODS.contains(method.getName());
	}

	private MyBatisMapperProxy(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (isObjectMethod(method)) {
			return null;
		}
		final Class<?> declaringInterface = findDeclaringInterface(proxy, method);
		if (Pager.class.isAssignableFrom(method.getReturnType())) {
			return new MyBatisMapperMethod(getDeclaringInterface(declaringInterface, method), method, this.sqlSession).execute(args);
		}
		MapperMethod mapperMethod = new MapperMethod(getDeclaringInterface(declaringInterface, method), method, this.sqlSession.getConfiguration());
		Object result = mapperMethod.execute(this.sqlSession,args);
		if ((result == null) && (method.getReturnType().isPrimitive())) {
			throw new BindingException("Mapper method '" + method.getName() + "' (" + method.getDeclaringClass() + ") attempted to return null from a method with a primitive return type (" + method.getReturnType() + ").");
		}
		return result;
	}

	private Class<?> getDeclaringInterface(Class<?> declaringInterface, Method method) {
		if (!this.sqlSession.getConfiguration().hasStatement(declaringInterface.getName() + "." + method.getName())) {
			Class<?>[] declaringInterfaces = declaringInterface.getInterfaces();
			for (Class<?> declaringinterface : declaringInterfaces) {
				this.logger.debug("向父接口查找Mapper:" + declaringinterface.getName() + "." + method.getName());
				if (this.sqlSession.getConfiguration().hasStatement(declaringinterface.getName() + "." + method.getName())) {
					return declaringinterface;
				}
			}
			throw new RuntimeException(declaringInterface.getName() + "." + method.getName() + "未正确配置!");
		}
		return declaringInterface;
	}

	private Class<?> findDeclaringInterface(Object proxy, Method method) {
		Class<?> declaringInterface = null;
		for (Class<?> iface : proxy.getClass().getInterfaces()) {
			Method m = ReflectionUtils.findMethod(iface, method.getName(), method.getParameterTypes());
			if (m != null) {
				declaringInterface = iface;
			}
		}
		if (declaringInterface == null) {
			throw new BindingException("Could not find interface with the given method " + method);
		}
		return declaringInterface;
	}

	/**
	 * 生成代理方法
	 * 
	 * @param <T> 泛型
	 * @param mapperInterface mapper接口
	 * @param sqlSession sqlSession
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newMapperProxy(Class<T> mapperInterface, SqlSession sqlSession) {
		ClassLoader classLoader = mapperInterface.getClassLoader();
		Class<?>[] interfaces = { mapperInterface };
		MyBatisMapperProxy proxy = new MyBatisMapperProxy(sqlSession);
		return (T) Proxy.newProxyInstance(classLoader, interfaces, proxy);
	}
}