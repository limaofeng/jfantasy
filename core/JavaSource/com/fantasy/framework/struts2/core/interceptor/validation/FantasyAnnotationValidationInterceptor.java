package com.fantasy.framework.struts2.core.interceptor.validation;

import com.fantasy.framework.struts2.rest.RestActionInvocation;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.reflect.MethodProxy;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.PrefixMethodInvocationUtil;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.validator.ActionValidatorManager;

import java.lang.reflect.Method;

@SuppressWarnings("rawtypes")
public class FantasyAnnotationValidationInterceptor extends org.apache.struts2.interceptor.validation.AnnotationValidationInterceptor {

    private final static String VALIDATE_PREFIX = "validate";
    private final static String ALT_VALIDATE_PREFIX = "validateDo";

    private static final Logger LOG = LoggerFactory.getLogger(FantasyAnnotationValidationInterceptor.class);

    private static final long serialVersionUID = 1813272797367431184L;

    private boolean alwaysInvokeValidate = true;
    private boolean programmatic = true;
    private boolean declarative = true;
    private boolean validateAnnotatedMethodOnly;

    private ActionValidatorManager actionValidatorManager;

    @Override
    public void setProgrammatic(boolean programmatic) {
        super.setProgrammatic(this.programmatic = programmatic);
    }

    @Override
    public void setDeclarative(boolean declarative) {
        super.setDeclarative(this.declarative = declarative);
    }

    public void setValidateAnnotatedMethodOnly(boolean validateAnnotatedMethodOnly) {
        super.setValidateAnnotatedMethodOnly(this.validateAnnotatedMethodOnly = validateAnnotatedMethodOnly);
    }

    @Override
    public void setAlwaysInvokeValidate(String alwaysInvokeValidate) {
        this.alwaysInvokeValidate = Boolean.parseBoolean(alwaysInvokeValidate);
        super.setAlwaysInvokeValidate(alwaysInvokeValidate);
    }

    @Override
    @Inject("fantasy.actionValidatorManager")
    public void setActionValidatorManager(ActionValidatorManager mgr) {
        super.setActionValidatorManager(actionValidatorManager = mgr);
    }

    @Override
    protected void doBeforeInvocation(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        ActionProxy proxy = invocation.getProxy();

        //the action name has to be from the url, otherwise validators that use aliases, like
        //MyActio-someaction-validator.xml will not be found, see WW-3194
        //UPDATE:  see WW-3753

        String context = this.getValidationContext(proxy);
        String method = proxy.getMethod();

        if (invocation instanceof RestActionInvocation) {
            context = method;
        }

        if (log.isDebugEnabled()) {
            log.debug("Validating " + invocation.getProxy().getNamespace() + "/" + invocation.getProxy().getActionName() + " with method " + method + ".");
        }


        if (declarative) {
            if (validateAnnotatedMethodOnly) {
                actionValidatorManager.validate(action, context, method);
            } else {
                actionValidatorManager.validate(action, context);
            }
        }

        if (action instanceof Validateable && programmatic) {
            // keep exception that might occured in validateXXX or validateDoXXX
            Exception exception = null;

            Validateable validateable = (Validateable) action;
            if (LOG.isDebugEnabled()) {
                LOG.debug("Invoking validate() on action " + validateable);
            }

            try {
                PrefixMethodInvocationUtil.invokePrefixMethod(
                        invocation,
                        new String[]{VALIDATE_PREFIX, ALT_VALIDATE_PREFIX});
            } catch (Exception e) {
                // If any exception occurred while doing reflection, we want
                // validate() to be executed
                if (LOG.isWarnEnabled()) {
                    LOG.warn("an exception occured while executing the prefix method", e);
                }
                exception = e;
            }


            if (alwaysInvokeValidate) {
                validateable.validate();
            }

            if (exception != null) {
                // rethrow if something is wrong while doing validateXXX / validateDoXXX
                throw exception;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Method getActionMethod(Class actionClass, String methodName) throws NoSuchMethodException {
        Method method;
        try {
            MethodProxy methodProxy = ClassUtil.getMethodProxy(actionClass, methodName);
            if (methodProxy == null) {
                throw new NoSuchMethodException();
            }
            method = methodProxy.getMethod();
        } catch (NoSuchMethodException e) {
            // hmm -- OK, try doXxx instead
            try {
                String altMethodName = "do" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                method = actionClass.getMethod(altMethodName);
            } catch (NoSuchMethodException e1) {
                // throw the original one
                throw e;
            }
        }
        return method;
    }
}