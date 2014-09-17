package com.opensymphony.xwork2.util;

import com.fantasy.framework.util.common.PathUtil;
import com.opensymphony.xwork2.validator.DefaultValidatorFactory;
import org.junit.Test;

import java.net.URI;
import java.net.URL;
import java.util.Iterator;

public class TestClassLoaderUtil {

    @Test
    public void testGetResources() throws Exception {
        Iterator<URL> urls = ClassLoaderUtil.getResources("", DefaultValidatorFactory.class, false);

        System.out.println(PathUtil.webinf());
        while (urls.hasNext()) {
            URL url = urls.next();
            System.out.println(url);
            URI uri = new URI(url.toExternalForm().replaceAll(" ", "%20"));
            System.out.println(uri.isOpaque() + "-" + uri.getScheme());
        }
    }

}
