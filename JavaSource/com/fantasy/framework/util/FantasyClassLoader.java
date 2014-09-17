package com.fantasy.framework.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Fantasy 动态类加载器。
 * 
 * @功能描述 <br/>
 *       用于封装DynamicClassLoader加载器<br/>
 *       如果一个类被多次修改并加载的话。需要不断创建新的加载器
 * @author 李茂峰
 * @since 2012-11-6 下午10:33:48
 * @version 1.0
 */
public class FantasyClassLoader extends ClassLoader {

	private Lock lock = new ReentrantLock();

	private final static FantasyClassLoader fantasyClassLoader = AccessController.doPrivileged(new PrivilegedAction<FantasyClassLoader>() {
		public FantasyClassLoader run() {
			return new FantasyClassLoader();
		}
	});

	private DynamicClassLoader classLoader = AccessController.doPrivileged(new PrivilegedAction<DynamicClassLoader>() {
		public DynamicClassLoader run() {
			return new DynamicClassLoader(DynamicClassLoader.class.getClassLoader());
		}
	});

	/**
	 * 已被加载的对象
	 */
	private static List<String> loadclass = new ArrayList<String>();

	/**
	 * 从文件加载类
	 * 
	 * @功能描述
	 * @param classpath
	 * @param classname
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Class<?> loadClass(String classpath, String classname) throws ClassNotFoundException {
		try {
			lock.lock();
			if (loadclass.contains(classname)) {// 如果对象已经被加载
				if (loadclass.contains(classname)) {
					classLoader = new DynamicClassLoader(classLoader);// 创建新的加载器
					loadclass.clear();
				}
			}
			loadclass.add(classname);
			return classLoader.loadClass(classpath, classname);
		} finally {
			lock.unlock();
		}
	}

    /**
     * 通过字节码加载类
     *
     * @功能描述
     * @param classdata
     * @param classname
     * @return
     * @throws ClassNotFoundException
     */
    public Class<?> loadClass(byte[] classdata, String classname) throws ClassNotFoundException {
        try {
            lock.lock();
            if (loadclass.contains(classname)) {// 如果对象已经被加载
                if (loadclass.contains(classname)) {
                    classLoader = new DynamicClassLoader(classLoader);// 创建新的加载器
                    loadclass.clear();
                }
            }
            loadclass.add(classname);
            return classLoader.loadClass(classdata, classname);
        } finally {
            lock.unlock();
        }
    }


	/**
	 * 自动加载类
	 * 
	 * @功能描述
	 * @param classname
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Class<?> loadClass(String classname) throws ClassNotFoundException {
		return classLoader.loadClass(classname);
	}

	public static FantasyClassLoader getClassLoader() {
		return fantasyClassLoader;
	}

}
