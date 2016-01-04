package org.jfantasy.website.util;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.HttpSessionHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateModel;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * TemplateModel 工具类
 */
public class TemplateModelUtils {

    private final static Logger logger = LoggerFactory.getLogger(TemplateModelUtils.class);

    private static final String ATTR_JSP_TAGLIBS_MODEL = ".freemarker.JspTaglibs";
    private static final String KEY_JSP_TAGLIBS = "JspTaglibs";
    private static ServletContext servletContext;
    private static FreemarkerServlet freemarkerServlet;

    static {
        servletContext = new MockServletContext();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, SpringContextUtil.getApplicationContext());

        freemarkerServlet = new FreemarkerServlet();
        MockServletConfig servletConfig = new MockServletConfig();
        try {
            freemarkerServlet.init(servletConfig);
        } catch (ServletException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static TemplateModel createScopesHashModel(Object root) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        BeansWrapper beanWrapper = new BeansWrapper();
        SimpleHash model = new SimpleHash();
        TaglibFactory taglibs = new TaglibFactory(servletContext);
        servletContext.setAttribute(ATTR_JSP_TAGLIBS_MODEL, taglibs);

        model.put(FreemarkerServlet.KEY_APPLICATION, new ServletContextHashModel(freemarkerServlet, beanWrapper));
        model.put(FreemarkerServlet.KEY_REQUEST, new HttpRequestHashModel(request, beanWrapper));
        model.put(FreemarkerServlet.KEY_SESSION, new HttpSessionHashModel(request.getSession(), beanWrapper));
        model.put(KEY_JSP_TAGLIBS, servletContext.getAttribute(ATTR_JSP_TAGLIBS_MODEL));

        return model;
    }

}
