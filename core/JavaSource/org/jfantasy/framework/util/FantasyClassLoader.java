package org.jfantasy.framework.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Fantasy 动态类加载器。
 *
 * @author 李茂峰
 * @version 1.0
 *          用于封装DynamicClassLoader加载器<br/>
 *          如果一个类被多次修改并加载的话。需要不断创建新的加载器
 * @since 2012-11-6 下午10:33:48
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
     * 已被卸载的对象
     */
    private static List<String> unloadclass = new ArrayList<String>();

    /**
     * 从文件加载类
     *
     * @param classpath classpath
     * @param classname classname
     * @return Class
     * @throws ClassNotFoundException
     */
    public Class loadClass(String classpath, String classname) throws ClassNotFoundException {
        try {
            lock.lock();
            if (loadclass.contains(classname)) {// 如果对象已经被加载
                classLoader = new DynamicClassLoader(classLoader);// 创建新的加载器
                loadclass.clear();
            }
            loadclass.add(classname);
            unloadclass.remove(classname);
            return classLoader.loadClass(classpath, classname);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 通过字节码加载类
     *
     * @param classdata classdata
     * @param classname classname
     * @return Class
     * @throws ClassNotFoundException
     */
    public Class loadClass(byte[] classdata, String classname) throws ClassNotFoundException {
        try {
            lock.lock();
            if (loadclass.contains(classname)) {// 如果对象已经被加载
                classLoader = new DynamicClassLoader(classLoader);// 创建新的加载器
                loadclass.clear();
            }
            loadclass.add(classname);
            unloadclass.remove(classname);
            return classLoader.loadClass(classdata, classname);
        } finally {
            lock.unlock();
        }
    }


    /**
     * 自动加载类
     *
     * @param classname 类路径
     * @return Class
     * @throws ClassNotFoundException
     */
    public Class loadClass(String classname) throws ClassNotFoundException {
        if (unloadclass.contains(classname)) {
            throw new ClassNotFoundException(classname);
        }
        return classLoader.loadClass(classname);
    }

    public static FantasyClassLoader getClassLoader() {
        return fantasyClassLoader;
    }

    public void removeClass(String className) {
        unloadclass.add(className);
    }
}
