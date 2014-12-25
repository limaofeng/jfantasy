package com.fantasy.framework.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 动态类加载器
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-11-30 下午05:12:44
 * @version 1.0
 */
public class DynamicClassLoader extends ClassLoader {
	private static final Log logger = LogFactory.getLog(DynamicClassLoader.class);

	public DynamicClassLoader(ClassLoader parent) {
		super(parent);
	}

	public Class<?> loadClass(String classPath, String className) throws ClassNotFoundException {
		try {
			String url = classPathParser(classPath) + classNameParser(className);
			logger.debug(url);
			URL myUrl = new URL(url);
			URLConnection connection = myUrl.openConnection();
			InputStream input = connection.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data = input.read();
			while (data != -1) {
				buffer.write(data);
				data = input.read();
			}
			input.close();
			byte[] classData = buffer.toByteArray();
			return defineClass(noSuffix(className), classData, 0, classData.length);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

    public Class<?> loadClass(byte[] classData, String className) throws ClassNotFoundException {
        return defineClass(noSuffix(className), classData, 0, classData.length);
    }

	private String pathParser(String path) {
		return path.replaceAll("\\\\", "/");
	}

	private String classPathParser(String path) {
		String classPath = pathParser(path);
		if (!classPath.startsWith("file:")) {
			classPath = "file:" + classPath;
		}
		if (!classPath.endsWith("/")) {
			classPath = classPath + "/";
		}
		return classPath;
	}

	private String classNameParser(String className) {
		return className.replaceAll("\\.", "/") + ".class";
	}

	private String noSuffix(String className) {
		return className.endsWith(".class") ? className.substring(0, className.lastIndexOf(".")) : className;
	}
}