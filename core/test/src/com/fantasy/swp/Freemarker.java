package com.fantasy.swp;

import com.fantasy.file.FileManager;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.HttpSessionHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.Configuration;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.views.DefaultTagLibrary;
import org.apache.struts2.views.freemarker.ScopesHashModel;
import org.apache.struts2.views.freemarker.StrutsBeanWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class Freemarker {

    @Resource
    private transient Configuration configuration;

    private static final String ATTR_JSP_TAGLIBS_MODEL = ".freemarker.JspTaglibs";
    public static final String KEY_JSP_TAGLIBS = "JspTaglibs";
    @Test
    public void writer() throws IOException, ServletException {
        FileManager fileManager = new LocalFileManager("/Users/lmf/Downloads/test.jfantasy.org");

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletContext servletContext = request.getServletContext();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, SpringContextUtil.getApplicationContext());
        //configuration.setSharedVariable("s.property",new PropertyModel(valueStack,request,response));



//
//        Map<String,TagLibrary> map = new HashMap<String,TagLibrary>();
//        Set<String> prefixes = container.getInstanceNames(TagLibrary.class);
//        for (String prefix : prefixes) {
//            map.put(prefix, container.getInstance(TagLibrary.class, prefix));
//        }
//        this.tagLibraries = Collections.unmodifiableMap(map);

        Dispatcher.setInstance(new Dispatcher(servletContext,new HashMap<String, String>()));
        Dispatcher dispatcher = Dispatcher.getInstance();
        dispatcher.init();

        Dispatcher du = Dispatcher.getInstance();
        ValueStack valueStack = du.getContainer().getInstance(ValueStackFactory.class).createValueStack();

        request.setAttribute(ServletActionContext.STRUTS_VALUESTACK_KEY,valueStack);

        StrutsBeanWrapper wrapper = new StrutsBeanWrapper(true);
        wrapper.setUseCache(false);
        ScopesHashModel model = new ScopesHashModel(wrapper, request.getServletContext(), request, valueStack);

        model.put("s",new DefaultTagLibrary().getFreemarkerModels(valueStack,request,response));

        TaglibFactory taglibs = new TaglibFactory(servletContext);

        servletContext.setAttribute(ATTR_JSP_TAGLIBS_MODEL, taglibs);

        FreemarkerServlet freemarkerServlet = new FreemarkerServlet();
        MockServletConfig servletConfig = new MockServletConfig();
        freemarkerServlet.init(servletConfig);
        StrutsBeanWrapper beanWrapper = new StrutsBeanWrapper(true);
        model.put(FreemarkerServlet.KEY_APPLICATION,new ServletContextHashModel(freemarkerServlet,beanWrapper));

        valueStack.set("art", JSON.deserialize("{title:\"测试title\",text:\"text测试\"}"));

        //request.setAttribute("art",JSON.deserialize("{title:\"测试title\",text:\"text\"}"));

        //Map<String,Object> data = new HashMap<String,Object>();
        //model.put("art", JSON.deserialize("{title:\"测试title\",text:\"text\"}"));
        model.put(FreemarkerServlet.KEY_REQUEST,new HttpRequestHashModel(request,beanWrapper));
        model.put(FreemarkerServlet.KEY_SESSION, new HttpSessionHashModel(request.getSession(),beanWrapper));
        model.put(KEY_JSP_TAGLIBS, servletContext.getAttribute(ATTR_JSP_TAGLIBS_MODEL));

//        Environment environment = Environment.getCurrentEnvironment();
//        environment.setGlobalVariable(FreemarkerServlet.KEY_APPLICATION_PRIVATE,model);
        FreeMarkerTemplateUtils.writer(model, configuration.getTemplate("template/test.ftl"), fileManager.writeFile("/test.txt"));
    }
}
