package com.fantasy.framework.spring;

import com.fantasy.framework.lucene.annotations.Indexed;
import ognl.DefaultTypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class ClassPathScannerTest {

    private static final Log logger = LogFactory.getLog(ClassPathScannerTest.class);

    private ClassPathScanner pathScanner;

    @Before
    public void setUp() throws Exception {
        pathScanner = new ClassPathScanner();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFindTargetClassNames() throws Exception {
        Set<String> classeNames = pathScanner.findTargetClassNames("com.fantasy.framework.spring");
        for (String clazz : classeNames) {
            logger.debug(clazz);
        }
    }

    @Test
    public void testFindAnnotationedClasses() throws Exception {
        Set<Class<?>> classes = pathScanner.findAnnotationedClasses("", Indexed.class);
        for (Class<?> clazz : classes) {
            logger.debug(clazz);
        }
    }

    @Test
    public void testFindInterfaceClasses() throws Exception {
        Set<Class<?>> classes = ClassPathScanner.getInstance().findInterfaceClasses("com.fantasy", DefaultTypeConverter.class);
        for (Class<?> clazz : classes) {
            logger.debug(clazz);
        }
    }
}