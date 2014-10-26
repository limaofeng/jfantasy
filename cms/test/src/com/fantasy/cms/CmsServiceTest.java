package com.fantasy.cms;


import com.fantasy.attr.bean.Attribute;
import com.fantasy.cms.bean.Article;
import com.fantasy.cms.service.CmsService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.persistence.Transient;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class CmsServiceTest {

    private static Log logger = LogFactory.getLog(CmsServiceTest.class);

    @Resource
    private CmsService cmsService;

    @Test
    @Transient
    public void findPager() {
        Pager<Article> pager = new Pager<Article>();
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_test", "limaofeng1234"));
        Assert.assertNotNull(cmsService.findPager(pager, filters));
    }

    @Test
    @Transient
    public void save() {
        List<Article> articles = this.cmsService.getArticles(new Criterion[]{Restrictions.isNotNull("version")}, 1);
        if (!articles.isEmpty()) {
            Article article = this.cmsService.get(articles.get(0).getId());
            if (!article.getAttributeValues().isEmpty()) {
                Attribute attribute = article.getAttributeValues().get(0).getAttribute();
                OgnlUtil.getInstance().setValue(attribute.getCode(), article, "123456");
                article.setTitle("JUnit测试修改标题-" + DateUtil.format("yyyy-MM-dd"));
                cmsService.save(article);
            }
        }
        articles = this.cmsService.getArticles(new Criterion[]{Restrictions.isNull("version")}, 1);
        if (!articles.isEmpty()) {
            Article article = this.cmsService.get(articles.get(0).getId());
            article.setTitle("JUnit测试修改标题-" + DateUtil.format("yyyy-MM-dd"));
            cmsService.save(article);
        }
    }

    public static String getPathFromClass(Class<?> cls) {
        String path = null;
        if (cls == null) {
            throw new NullPointerException();
        }
        URL url = getClassLocationURL(cls);
        if (url != null) {
            path = url.getPath();
            if ("jar".equalsIgnoreCase(url.getProtocol())) {
                try {
                    path = new URL(path).getPath();
                } catch (MalformedURLException ignored) {
                }
                int location = path.indexOf("!/");
                if (location != -1) {
                    path = path.substring(0, location);
                }
            }
            File file = new File(path);
            try {
                path = file.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    public static URL getClassLocationURL(Class<?> cls) {
        if (cls == null)
            throw new IllegalArgumentException("null input: cls");
        URL result = null;
        String clsAsResource = cls.getName().replace('.', '/').concat(".class");
        ProtectionDomain pd = cls.getProtectionDomain();
        if (pd != null) {
            CodeSource cs = pd.getCodeSource();
            if (cs != null)
                result = cs.getLocation();
            if ((result != null) && ("file".equals(result.getProtocol())))
                try {
                    if ((result.toExternalForm().endsWith(".jar")) || (result.toExternalForm().endsWith(".zip")))
                        result = new URL("jar:".concat(result.toExternalForm()).concat("!/").concat(clsAsResource));
                    else if (new File(result.getFile()).isDirectory())
                        result = new URL(result, clsAsResource);
                } catch (MalformedURLException ignored) {
                }
        }
        if (result == null) {
            ClassLoader clsLoader = cls.getClassLoader();
            result = clsLoader != null ? clsLoader.getResource(clsAsResource) : ClassLoader.getSystemResource(clsAsResource);
        }
        return result;
    }

    @Test
    public void get() {
        List<Article> articles = this.cmsService.getArticles(new Criterion[]{Restrictions.isNotNull("version")}, 1);
        if (!articles.isEmpty()) {
            Article article = this.cmsService.get(articles.get(0).getId());
            //test not null
            Assert.assertNotNull(article);
            //test attrubuteValues not null
            Assert.assertNotNull(article.getAttributeValues());
        }
        articles = this.cmsService.getArticles(new Criterion[]{Restrictions.isNull("version")}, 1);
        if (!articles.isEmpty()) {
            Article article = this.cmsService.get(articles.get(0).getId());
            //test not null
            Assert.assertNotNull(article);
        }

    }

}
