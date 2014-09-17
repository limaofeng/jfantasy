package com.opensymphony.xwork2;

import com.fantasy.framework.struts2.context.ActionConstants;
import com.fantasy.framework.struts2.interceptor.ParametersInterceptor.MethodParam;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.reflect.MethodProxy;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.InterceptorMapping;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.ExceptionHolder;
import com.opensymphony.xwork2.interceptor.PreResultListener;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;
import org.apache.struts2.interceptor.RequestAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "rawtypes"})
public class DefaultActionInvocation implements ActionInvocation {

    private static final long serialVersionUID = -585293628862447329L;

    // static {
    // if (ObjectFactory.getContinuationPackage() != null) {
    // continuationHandler = new ContinuationHandler();
    // }
    // }

    private static final Logger LOG = LoggerFactory.getLogger(DefaultActionInvocation.class);

    private static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    protected Object action;
    protected transient ActionProxy proxy;
    protected List<PreResultListener> preResultListeners;
    protected Map<String, Object> extraContext;
    protected ActionContext invocationContext;
    protected transient Iterator<InterceptorMapping> interceptors;
    protected ValueStack stack;
    protected Result result;
    protected Result explicitResult;
    protected String resultCode;
    protected boolean executed = false;
    protected boolean pushAction = true;
    protected ObjectFactory objectFactory;
    protected transient ActionEventListener actionEventListener;
    protected transient ValueStackFactory valueStackFactory;
    protected Container container;
    //private Configuration configuration;
    protected transient UnknownHandlerManager unknownHandlerManager;

    public DefaultActionInvocation(final Map<String, Object> extraContext, final boolean pushAction) {
        DefaultActionInvocation.this.extraContext = extraContext;
        DefaultActionInvocation.this.pushAction = pushAction;
    }

    @Inject
    public void setUnknownHandlerManager(UnknownHandlerManager unknownHandlerManager) {
        this.unknownHandlerManager = unknownHandlerManager;
    }

    @Inject
    public void setValueStackFactory(ValueStackFactory fac) {
        this.valueStackFactory = fac;
    }

//    @Inject  20140109 未使用到的属性
//    public void setConfiguration(Configuration configuration) {
//        this.configuration = configuration;
//    }

    @Inject
    public void setObjectFactory(ObjectFactory fac) {
        this.objectFactory = fac;
    }

    @Inject
    public void setContainer(Container cont) {
        this.container = cont;
    }

    @Inject(required = false)
    public void setActionEventListener(ActionEventListener listener) {
        this.actionEventListener = listener;
    }

    public Object getAction() {
        return action;
    }

    public boolean isExecuted() {
        return executed;
    }

    public ActionContext getInvocationContext() {
        return invocationContext;
    }

    public ActionProxy getProxy() {
        return proxy;
    }

    public Result getResult() throws Exception {
        Result returnResult = result;
        while (returnResult instanceof ActionChainResult) {
            ActionProxy aProxy = ((ActionChainResult) returnResult).getProxy();

            if (aProxy != null) {
                Result proxyResult = aProxy.getInvocation().getResult();

                if ((proxyResult != null) && (aProxy.getExecuteResult())) {
                    returnResult = proxyResult;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return returnResult;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        if (isExecuted())
            throw new IllegalStateException("Result has already been executed.");

        this.resultCode = resultCode;
    }

    public ValueStack getStack() {
        return stack;
    }

    public void addPreResultListener(PreResultListener listener) {
        if (preResultListeners == null) {
            preResultListeners = new ArrayList<PreResultListener>(1);
        }
        preResultListeners.add(listener);
    }

    public Result createResult() throws Exception {
        if (explicitResult != null) {
            Result ret = explicitResult;
            explicitResult = null;

            return ret;
        }
        ActionConfig config = proxy.getConfig();
        Map<String, ResultConfig> results = config.getResults();

        ResultConfig resultConfig = null;

        try {
            resultConfig = results.get(resultCode);
        } catch (NullPointerException e) {
            // swallow
        }
        if (resultConfig == null) {
            // If no result is found for the given resultCode, try to get a wildcard '*' match.
            resultConfig = results.get("*");
        }
        if (resultConfig != null) {
            try {
                return objectFactory.buildResult(resultConfig, invocationContext.getContextMap());
            } catch (Exception e) {
                LOG.error("There was an exception while instantiating the result of type " + resultConfig.getClassName(), e);
                throw new XWorkException(e, resultConfig);
            }
        } else if (resultCode != null && !Action.NONE.equals(resultCode) && unknownHandlerManager.hasUnknownHandlers()) {
            return unknownHandlerManager.handleUnknownResult(invocationContext, proxy.getActionName(), proxy.getConfig(), resultCode);
        }
        return null;
    }

    /**
     * @throws ConfigurationException If no result can be found with the returned code
     */
    public String invoke() throws Exception {
        String profileKey = "invoke: ";
        try {
            UtilTimerStack.push(profileKey);

            if (executed) {
                throw new IllegalStateException("Action has already executed");
            }

            if (interceptors.hasNext()) {
                final InterceptorMapping interceptor = interceptors.next();
                String interceptorMsg = "interceptor: " + interceptor.getName();
                UtilTimerStack.push(interceptorMsg);
                try {
                    resultCode = interceptor.getInterceptor().intercept(DefaultActionInvocation.this);
                } finally {
                    UtilTimerStack.pop(interceptorMsg);
                }
            } else {
                resultCode = invokeActionOnly();
            }

            // this is needed because the result will be executed, then control will return to the Interceptor, which will
            // return above and flow through again
            if (!executed) {
                if (preResultListeners != null) {
                    for (Object preResultListener : preResultListeners) {
                        PreResultListener listener = (PreResultListener) preResultListener;

                        String _profileKey = "preResultListener: ";
                        try {
                            UtilTimerStack.push(_profileKey);
                            listener.beforeResult(this, resultCode);
                        } finally {
                            UtilTimerStack.pop(_profileKey);
                        }
                    }
                }

                // now execute the result, if we're supposed to
                if (proxy.getExecuteResult()) {
                    executeResult();
                }

                executed = true;
            }

            return resultCode;
        } finally {
            UtilTimerStack.pop(profileKey);
        }
    }

    public String invokeActionOnly() throws Exception {
        return invokeAction(getAction(), proxy.getConfig());
    }

    protected void createAction(Map<String, Object> contextMap) {
        // load action
        String timerKey = "actionCreate: " + (proxy != null ? proxy.getActionName() : " ActionName[??] ");
        try {
            UtilTimerStack.push(timerKey);
            if (proxy == null) {
                throw new NullPointerException("proxy is null");
            }
            action = objectFactory.buildAction(proxy.getActionName(), proxy.getNamespace(), proxy.getConfig(), contextMap);
        } catch (InstantiationException e) {
            throw new XWorkException("Unable to intantiate Action!", proxy == null ? null : e, proxy == null ? null : proxy.getConfig());
        } catch (IllegalAccessException e) {
            throw new XWorkException("Illegal access to constructor, is it public?", e, proxy != null ? proxy.getConfig() : null);
        } catch (Exception e) {
            String gripe;
            if (proxy == null) {
                gripe = "Whoa!  No ActionProxy instance found in current ActionInvocation.  This is bad ... very bad";
            } else if (proxy.getConfig() == null) {
                gripe = "Sheesh.  Where'd that ActionProxy get to?  I can't find it in the current ActionInvocation!?";
            } else if (proxy.getConfig().getClassName() == null) {
                gripe = "No Action defined for '" + proxy.getActionName() + "' in namespace '" + proxy.getNamespace() + "'";
            } else {
                gripe = "Unable to instantiate Action, " + proxy.getConfig().getClassName() + ",  defined for '" + proxy.getActionName() + "' in namespace '" + proxy.getNamespace() + "'";
            }

            gripe += (e.getMessage() != null) ? e.getMessage() : " [no message in exception]";
            throw new XWorkException(gripe, e, proxy == null ? null : proxy.getConfig());
        } finally {
            UtilTimerStack.pop(timerKey);
        }

        if (actionEventListener != null) {
            action = actionEventListener.prepare(action, stack);
        }
    }

    protected Map<String, Object> createContextMap() {
        Map<String, Object> contextMap;

        if ((extraContext != null) && (extraContext.containsKey(ActionContext.VALUE_STACK))) {
            // In case the ValueStack was passed in
            stack = (ValueStack) extraContext.get(ActionContext.VALUE_STACK);

            if (stack == null) {
                throw new IllegalStateException("There was a null Stack set into the extra params.");
            }

            contextMap = stack.getContext();
        } else {
            // create the value stack
            // this also adds the ValueStack to its context
            stack = valueStackFactory.createValueStack();

            // create the action context
            contextMap = stack.getContext();
        }

        // put extraContext in
        if (extraContext != null) {
            contextMap.putAll(extraContext);
        }

        // put this DefaultActionInvocation into the context map
        contextMap.put(ActionContext.ACTION_INVOCATION, this);
        contextMap.put(ActionContext.CONTAINER, container);

        return contextMap;
    }

    /**
     * Uses getResult to get the final Result and executes it
     *
     * @throws ConfigurationException If not result can be found with the returned code
     */
    private void executeResult() throws Exception {
        result = createResult();

        String timerKey = "executeResult: " + getResultCode();
        try {
            UtilTimerStack.push(timerKey);
            if (result != null) {
                if (!(stack.getRoot().peek() instanceof ExceptionHolder)) {
                    // 改变 stack root 的指向
                    Object action = this.getAction();
                    Object rootObject = null;
                    if (action instanceof ValidationAware) {
                        ValidationAware aware = (ValidationAware) action;
                        if (aware.hasActionErrors() || aware.hasFieldErrors()) {
                            rootObject = action;
                        }
                    }
                    if (rootObject == null) {
                        if (action instanceof RequestAware) {
                            rootObject = ClassUtil.getValue(action, "attrs");
                        }
                    }
                    // 剥离formbean
                    ValueStack stack = this.getStack();
                    while (stack.getRoot().peek() != action) {
                        stack.getRoot().pop();
                    }
                    // 如果 rootObject 不等于 action
                    if (rootObject != null && rootObject != action) {
                        stack.getRoot().push(rootObject);
                    }
                }
                result.execute(this);
            } else if (resultCode != null && !Action.NONE.equals(resultCode)) {
                throw new ConfigurationException("No result defined for action " + getAction().getClass().getName() + " and result " + getResultCode(), proxy.getConfig());
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No result returned for action " + getAction().getClass().getName() + " at " + proxy.getConfig().getLocation());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            UtilTimerStack.pop(timerKey);
        }
    }

    public void init(ActionProxy proxy) {
        this.proxy = proxy;
        Map<String, Object> contextMap = createContextMap();

        // Setting this so that other classes, like object factories, can use the ActionProxy and other
        // contextual information to operate
        ActionContext actionContext = ActionContext.getContext();

        if (actionContext != null) {
            actionContext.setActionInvocation(this);
        }

        createAction(contextMap);

        if (pushAction) {
            stack.push(action);
            contextMap.put("action", action);
        }

        invocationContext = new ActionContext(contextMap);
        invocationContext.setName(proxy.getActionName());

        // get a new List so we don't get problems with the iterator if someone changes the list
        List<InterceptorMapping> interceptorList = new ArrayList<InterceptorMapping>(proxy.getConfig().getInterceptors());
        interceptors = interceptorList.iterator();
    }

    protected String invokeAction(Object action, ActionConfig actionConfig) throws Exception {
        String methodName = proxy.getMethod();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Executing action method = " + actionConfig.getMethodName());
        }

        String timerKey = "invokeAction: " + proxy.getActionName();
        try {
            UtilTimerStack.push(timerKey);

            boolean methodCalled = false;
            Object methodResult = null;
            Method method = null;
            try {
                MethodProxy methodProxy = ClassUtil.getMethod(getAction().getClass(), methodName);
                if (methodProxy == null) {
                    throw new NoSuchMethodException();
                }
                method = methodProxy.getMethod();// getAction().getClass().getMethod(methodName, EMPTY_CLASS_ARRAY);
            } catch (NoSuchMethodException e) {
                // hmm -- OK, try doXxx instead
                try {
                    String altMethodName = "do" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                    method = getAction().getClass().getMethod(altMethodName, EMPTY_CLASS_ARRAY);
                } catch (NoSuchMethodException e1) {
                    // well, give the unknown handler a shot
                    if (unknownHandlerManager.hasUnknownHandlers()) {
                        try {
                            methodResult = unknownHandlerManager.handleUnknownMethod(action, methodName);
                            methodCalled = true;
                        } catch (NoSuchMethodException e2) {
                            // throw the original one
                            throw e;
                        }
                    } else {
                        throw e;
                    }
                }
            }

            if (!methodCalled) {
                Object params = ActionContext.getContext().get(ActionConstants.METHOD_PARAM);
                if (params != null && params instanceof MethodParam) {
                    methodResult = method.invoke(action, ((MethodParam) params).getArgs());
                } else {
                    methodResult = method.invoke(action, EMPTY_OBJECT_ARRAY);
                }
            }

            return saveResult(actionConfig, methodResult);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("The " + methodName + "() is not defined in action " + getAction().getClass() + "");
        } catch (InvocationTargetException e) {
            // We try to return the source exception.
            Throwable t = e.getTargetException();

            if (actionEventListener != null) {
                String result = actionEventListener.handleException(t, getStack());
                if (result != null) {
                    return result;
                }
            }
            if (t instanceof Exception) {
                throw (Exception) t;
            } else {
                throw e;
            }
        } finally {
            UtilTimerStack.pop(timerKey);
        }
    }

    /**
     * Save the result to be used later.
     *
     * @param actionConfig actionConfig
     * @param methodResult the result of the action.
     * @return the result code to process.
     */
    protected String saveResult(ActionConfig actionConfig, Object methodResult) {
        if (methodResult instanceof Result) {
            this.explicitResult = (Result) methodResult;

            // Wire the result automatically
            container.inject(explicitResult);
            return null;
        } else {
            return (String) methodResult;
        }
    }

}