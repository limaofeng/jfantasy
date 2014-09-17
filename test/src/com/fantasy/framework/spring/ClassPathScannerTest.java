package com.fantasy.framework.spring;

import com.fantasy.framework.lucene.annotations.Indexed;
import ognl.DefaultTypeConverter;

import java.io.IOException;
import java.util.Set;

public class ClassPathScannerTest {

    public static void main(String[] args) throws IOException {

        ClassPathScanner classPathScanner = new ClassPathScanner();
//
        Set<Class<?>> classes = classPathScanner.findAnnotationedClasses("", Indexed.class);

        for (Class<?> clazz : classes) {
            System.out.println(clazz);
        }

        classes = ClassPathScanner.getInstance().findInterfaceClasses("com.fantasy", DefaultTypeConverter.class);

        for (Class<?> clazz : classes) {
            System.out.println(clazz);
        }

//		 Iterator<URL> strutsPlugin = ClassLoaderUtil.getResources("struts-plugin.xml", null, false);
//		 while (strutsPlugin.hasNext()) {
//			 URL url = strutsPlugin.next();
//			 // url.openStream();
//			 System.out.println(url);
//		 }

    }

}
