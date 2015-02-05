package com.fantasy.framework.freemarker;

import com.fantasy.framework.spring.SpringContextUtil;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.HttpSessionHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.TemplateModel;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.views.DefaultTagLibrary;
import org.apache.struts2.views.freemarker.ScopesHashModel;
import org.apache.struts2.views.freemarker.StrutsBeanWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.HashMap;

/**
 * TemplateModel 工具类
 */
public class TemplateModelUtils {

    private final static Logger logger = LoggerFactory.getLogger(FreeMarkerTemplateUtils.class);

    private static final String ATTR_JSP_TAGLIBS_MODEL = ".freemarker.JspTaglibs";
    private static final String KEY_JSP_TAGLIBS = "JspTaglibs";
    private static ServletContext servletContext;
    private static Dispatcher du;
    private static StrutsBeanWrapper wrapper;
    private static FreemarkerServlet freemarkerServlet;
    private static StrutsBeanWrapper beanWrapper;

    static {
        servletContext = new MockServletContext();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, SpringContextUtil.getApplicationContext());

        Dispatcher.setInstance(new Dispatcher(servletContext, new HashMap<String, String>()));
        Dispatcher dispatcher = Dispatcher.getInstance();
        dispatcher.init();

        du = Dispatcher.getInstance();

        wrapper = new StrutsBeanWrapper(true);
        wrapper.setUseCache(false);

        freemarkerServlet = new FreemarkerServlet();
        MockServletConfig servletConfig = new MockServletConfig();
        try {
            freemarkerServlet.init(servletConfig);
        } catch (ServletException e) {
            logger.error(e.getMessage(), e);
        }
        beanWrapper = new StrutsBeanWrapper(true);
    }

    public static TemplateModel createScopesHashModel(Object root) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        ValueStack valueStack = du.getContainer().getInstance(ValueStackFactory.class).createValueStack();

        ScopesHashModel model = new ScopesHashModel(wrapper, request.getServletContext(), request, valueStack);
        request.setAttribute(ServletActionContext.STRUTS_VALUESTACK_KEY, valueStack);
        model.put("s", new DefaultTagLibrary().getFreemarkerModels(valueStack, request, response));
        TaglibFactory taglibs = new TaglibFactory(servletContext);
        servletContext.setAttribute(ATTR_JSP_TAGLIBS_MODEL, taglibs);

        model.put(FreemarkerServlet.KEY_APPLICATION, new ServletContextHashModel(freemarkerServlet, beanWrapper));
        model.put(FreemarkerServlet.KEY_REQUEST, new HttpRequestHashModel(request, beanWrapper));
        model.put(FreemarkerServlet.KEY_SESSION, new HttpSessionHashModel(request.getSession(), beanWrapper));
        model.put(KEY_JSP_TAGLIBS, servletContext.getAttribute(ATTR_JSP_TAGLIBS_MODEL));

        valueStack.getRoot().push(root);

        return model;
    }

}
