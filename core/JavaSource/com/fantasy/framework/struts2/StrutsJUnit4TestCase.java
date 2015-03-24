package com.fantasy.framework.struts2;

import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.interceptor.annotations.After;
import com.opensymphony.xwork2.interceptor.annotations.Before;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.util.logging.jdk.JdkLoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.ApplicationMap;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.RequestMap;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.dispatcher.mapper.ActionMapper;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.util.AttributeMap;
import org.apache.struts2.util.StrutsTestCaseHelper;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.*;

import static org.junit.Assert.assertNotNull;


public abstract class StrutsJUnit4TestCase<T> extends XWorkJUnit4TestCase {
    protected MockHttpServletResponse response;
    protected MockHttpServletRequest request;
    protected MockPageContext pageContext;
    protected MockServletContext servletContext;
    protected Map<String, String> dispatcherInitParams;

    protected DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

    static {
        ConsoleHandler handler = new ConsoleHandler();
        final SimpleDateFormat df = new SimpleDateFormat("mm:ss.SSS");
        Formatter formatter = new Formatter() {
            @Override
            public String format(LogRecord record) {
                StringBuilder sb = new StringBuilder();
                sb.append(record.getLevel());
                sb.append(':');
                for (int x = 9 - record.getLevel().toString().length(); x > 0; x--) {
                    sb.append(' ');
                }
                sb.append('[');
                sb.append(df.format(new Date(record.getMillis())));
                sb.append("] ");
                sb.append(formatMessage(record));
                sb.append('\n');
                return sb.toString();
            }
        };
        handler.setFormatter(formatter);
        Logger logger = Logger.getLogger("");
        if (logger.getHandlers().length > 0)
            logger.removeHandler(logger.getHandlers()[0]);
        logger.addHandler(handler);
        logger.setLevel(Level.WARNING);
        LoggerFactory.setLoggerFactory(new JdkLoggerFactory());
    }

    /**
     * gets an object from the stack after an action is executed
     */
    protected Object findValueAfterExecute(String key) {
        return ServletActionContext.getValueStack(request).findValue(key);
    }

    /**
     * gets an object from the stack after an action is executed
     *
     * @return The executed action
     */
    @SuppressWarnings("unchecked")
    protected T getAction() {
        return (T) findValueAfterExecute("action");
    }

    protected boolean containsErrors() {
        T action = this.getAction();
        if (action instanceof ValidationAware) {
            return ((ValidationAware) action).hasActionErrors();
        }
        throw new UnsupportedOperationException("Current action does not implement ValidationAware interface");
    }

    /**
     * Executes an action and returns it's output (not the result returned from
     * execute()), but the actual output that would be written to the response.
     * For this to work the configured result for the action needs to be
     * FreeMarker, or Velocity (JSPs can be used with the Embedded JSP plugin)
     */
    protected String executeAction(String uri) throws ServletException, UnsupportedEncodingException {
        request.setRequestURI(uri);
        ActionMapping mapping = getActionMapping(request);

        assertNotNull(mapping);
        Dispatcher.getInstance().serviceAction(request, response, servletContext, mapping);

        if (response.getStatus() != HttpServletResponse.SC_OK)
            throw new ServletException("Error code [" + response.getStatus() + "], Error: ["
                    + response.getErrorMessage() + "]");

        return response.getContentAsString();
    }

    /**
     * Store state of StrutsConstants.STRUTS_LOCALE setting.
     */
    private String defaultLocale;

    /**
     * Merge all application and servlet attributes into a single <tt>HashMap</tt> to represent the entire
     * <tt>Action</tt> context.
     *
     * @param requestMap     a Map of all request attributes.
     * @param parameterMap   a Map of all request parameters.
     * @param sessionMap     a Map of all session attributes.
     * @param applicationMap a Map of all servlet context attributes.
     * @param request        the HttpServletRequest object.
     * @param response       the HttpServletResponse object.
     * @param servletContext the ServletContextmapping object.
     * @return a HashMap representing the <tt>Action</tt> context.
     */
    public HashMap<String,Object> createContextMap(Map requestMap,
                                                   Map parameterMap,
                                                   Map sessionMap,
                                                   Map applicationMap,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   ServletContext servletContext) {
        HashMap<String,Object> extraContext = new HashMap<String,Object>();
        extraContext.put(ActionContext.PARAMETERS, new HashMap(parameterMap));
        extraContext.put(ActionContext.SESSION, sessionMap);
        extraContext.put(ActionContext.APPLICATION, applicationMap);

        Locale locale;
        if (defaultLocale != null) {
            locale = LocalizedTextUtil.localeFromString(defaultLocale, request.getLocale());
        } else {
            locale = request.getLocale();
        }

        extraContext.put(ActionContext.LOCALE, locale);
        //extraContext.put(ActionContext.DEV_MODE, Boolean.valueOf(devMode));

        extraContext.put(StrutsStatics.HTTP_REQUEST, request);
        extraContext.put(StrutsStatics.HTTP_RESPONSE, response);
        extraContext.put(StrutsStatics.SERVLET_CONTEXT, servletContext);

        // helpers to get access to request/session/application scope
        extraContext.put("request", requestMap);
        extraContext.put("session", sessionMap);
        extraContext.put("application", applicationMap);
        extraContext.put("parameters", parameterMap);

        AttributeMap attrMap = new AttributeMap(extraContext);
        extraContext.put("attr", attrMap);

        return extraContext;
    }

    /**
     * Create a context map containing all the wrapped request objects
     *
     * @param request The servlet request
     * @param response The servlet response
     * @param mapping The action mapping
     * @param context The servlet context
     * @return A map of context objects
     */
    public Map<String,Object> createContextMap(HttpServletRequest request, HttpServletResponse response,
                                               ActionMapping mapping, ServletContext context) {

        // request map wrapping the http request objects
        Map requestMap = new RequestMap(request);

        // parameters map wrapping the http parameters.  ActionMapping parameters are now handled and applied separately
        Map params = new HashMap(request.getParameterMap());

        // session map wrapping the http session
        Map session = new SessionMap(request);

        // application map wrapping the ServletContext
        Map application = new ApplicationMap(context);

        Map<String,Object> extraContext = createContextMap(requestMap, params, session, application, request, response, context);

        if (mapping != null) {
            extraContext.put(ServletActionContext.ACTION_MAPPING, mapping);
        }
        return extraContext;
    }

    /**
     * Creates an action proxy for a request, and sets parameters of the ActionInvocation to the passed
     * parameters. Make sure to set the request parameters in the protected "request" object before calling this method.
     */
    protected ActionProxy getActionProxy(String uri) {
        request.setRequestURI(uri);
        ActionMapping mapping = getActionMapping(request);
        String namespace = mapping.getNamespace();
        String name = mapping.getName();
        String method = mapping.getMethod();

        Map<String,Object> extraContext = createContextMap(request,response,mapping,servletContext);

        Configuration config = configurationManager.getConfiguration();
        ActionProxy proxy = config.getContainer().getInstance(ActionProxyFactory.class).createActionProxy(
                namespace, name, method, extraContext, true, false);

        ActionContext invocationContext = proxy.getInvocation().getInvocationContext();
        invocationContext.setParameters(new HashMap<String, Object>(request.getParameterMap()));
        // set the action context to the one used by the proxy
        ActionContext.setContext(invocationContext);

        // this is normaly done in onSetUp(), but we are using Struts internal
        // objects (proxy and action invocation)
        // so we have to hack around so it works
        ServletActionContext.setServletContext(servletContext);
        ServletActionContext.setRequest(request);
        ServletActionContext.setResponse(response);

        return proxy;
    }

    /**
     * Finds an ActionMapping for a given request
     */
    protected ActionMapping getActionMapping(HttpServletRequest request) {
        return Dispatcher.getInstance().getContainer().getInstance(ActionMapper.class).getMapping(request,
                Dispatcher.getInstance().getConfigurationManager());
    }

    /**
     * Finds an ActionMapping for a given url
     */
    protected ActionMapping getActionMapping(String url) {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRequestURI(url);
        return getActionMapping(req);
    }

    /**
     * Injects dependencies on an Object using Struts internal IoC container
     */
    protected void injectStrutsDependencies(Object object) {
        Dispatcher.getInstance().getContainer().inject(object);
    }


    protected void setupBeforeInitDispatcher() throws Exception {
    }

    protected void initServletMockObjects() {
        servletContext = new MockServletContext(resourceLoader);
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        pageContext = new MockPageContext(servletContext, request, response);
    }

    /**
     * Sets up the configuration settings, XWork configuration, and
     * message resources
     */
    @Before
    public void setUp() throws Exception {

        super.setUp();
        initServletMockObjects();
        setupBeforeInitDispatcher();
        initDispatcherParams();
        initDispatcher(dispatcherInitParams);
    }

    protected void initDispatcherParams() {
        if (StringUtils.isNotBlank(getConfigPath())) {
            dispatcherInitParams = new HashMap<String, String>();
            dispatcherInitParams.put("config", "struts-default.xml," + getConfigPath());
        }
    }

    protected Dispatcher initDispatcher(Map<String, String> params) {
        Dispatcher du = StrutsTestCaseHelper.initDispatcher(servletContext, params);
        configurationManager = du.getConfigurationManager();
        configuration = configurationManager.getConfiguration();
        container = configuration.getContainer();
        return du;
    }

    /**
     * Override this method to return a comma separated list of paths to a configuration
     * file.
     * <p>The default implementation simply returns <code>null</code>.
     * @return a comma separated list of config locations
     */
    protected String getConfigPath() {
        return null;
    }

    @After
    public void tearDown() throws Exception {

        super.tearDown();
        StrutsTestCaseHelper.tearDown();
    }

}