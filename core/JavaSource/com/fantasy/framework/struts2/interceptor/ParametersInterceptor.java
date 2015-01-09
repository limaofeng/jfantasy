package com.fantasy.framework.struts2.interceptor;

import com.fantasy.attr.DynaBean;
import com.fantasy.attr.bean.Attribute;
import com.fantasy.attr.util.VersionUtil;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.dao.hibernate.PropertyFilter.MatchType;
import com.fantasy.framework.struts2.context.ActionConstants;
import com.fantasy.framework.util.asm.AsmUtil;
import com.fantasy.framework.util.asm.Property;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.reflect.MethodProxy;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.conversion.impl.XWorkConverter;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork2.interceptor.NoParameters;
import com.opensymphony.xwork2.ognl.OgnlUtil;
import com.opensymphony.xwork2.ognl.OgnlValueStack;
import com.opensymphony.xwork2.ognl.OgnlValueStackFactory;
import com.opensymphony.xwork2.util.*;
import com.opensymphony.xwork2.util.reflection.ReflectionContextState;
import ognl.Ognl;
import ognl.OgnlException;
import ognl.TypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"rawtypes"})
public class ParametersInterceptor extends MethodFilterInterceptor {

    private static final long serialVersionUID = 6520119967486549835L;

    private static final Log logger = LogFactory.getLog(ParametersInterceptor.class);

    protected static final int PARAM_NAME_MAX_LENGTH = 100;

    private int paramNameMaxLength = PARAM_NAME_MAX_LENGTH;

    boolean ordered = false;
    Set<Pattern> excludeParams = Collections.emptySet();
    Set<Pattern> acceptParams = Collections.emptySet();
    static boolean devMode = false;

    // Allowed names of parameters
    private String acceptedParamNames = "\\w+((\\.\\w+)|(\\[\\d+\\])|(\\(\\d+\\))|(\\['\\w+'\\])|(\\('\\w+'\\)))*";
    private Pattern acceptedPattern = Pattern.compile(acceptedParamNames);

    private transient ValueStackFactory valueStackFactory;

    private transient OgnlUtil ognlUtil;

    private transient XWorkConverter xworkConverter;

    private void changeOgnlUtilXWorkConverter() {
        this.ognlUtil.setXWorkConverter(xworkConverter);
    }

    private void changeValueStackFactoryXWorkConverter() {
        if (valueStackFactory instanceof OgnlValueStackFactory) {
            ((OgnlValueStackFactory) this.valueStackFactory).setXWorkConverter(xworkConverter);
        }
    }

    @Inject
    public void setOgnlUtil(OgnlUtil ognlUtil) {
        this.ognlUtil = ognlUtil;
        if (xworkConverter != null) {
            changeOgnlUtilXWorkConverter();
        }
    }

    @Inject("fantasy.struts")
    public void setXWorkConverter(XWorkConverter conv) {
        this.xworkConverter = conv;
        if (this.ognlUtil != null) {
            changeOgnlUtilXWorkConverter();
        }
        if (this.valueStackFactory != null) {
            changeValueStackFactoryXWorkConverter();
        }
    }

    @Inject
    public void setValueStackFactory(ValueStackFactory valueStackFactory) {
        this.valueStackFactory = valueStackFactory;
        if (xworkConverter != null) {
            changeValueStackFactoryXWorkConverter();
        }
    }

    @Inject("devMode")
    public static void setDevMode(String mode) {
        devMode = "true".equals(mode);
    }

    public void setAcceptParamNames(String commaDelim) {
        Collection<String> acceptPatterns = ArrayUtils.asCollection(commaDelim);
        if (acceptPatterns != null) {
            acceptParams = new HashSet<Pattern>();
            for (String pattern : acceptPatterns) {
                acceptParams.add(Pattern.compile(pattern));
            }
        }
    }

    public void setParamNameMaxLength(int paramNameMaxLength) {
        this.paramNameMaxLength = paramNameMaxLength;
    }

    static private int countOGNLCharacters(String s) {
        int count = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c == '.' || c == '['){
                count++;
            }
        }
        return count;
    }

    static final Comparator<String> rbCollator = new Comparator<String>() {
        public int compare(String s1, String s2) {
            int l1 = countOGNLCharacters(s1), l2 = countOGNLCharacters(s2);
            return l1 < l2 ? -1 : (l2 < l1 ? 1 : s1.compareTo(s2));
        }

    };

    @Override
    public String doIntercept(ActionInvocation invocation) throws Exception {
        Object action = invocation.getAction();
        if (!(action instanceof NoParameters)) {
            ActionContext ac = invocation.getInvocationContext();
            final Map<String, Object> parameters = retrieveParameters(ac);
            if (logger.isDebugEnabled()) {
                logger.debug("Setting params " + getParameterLogMap(parameters));
            }
            if (parameters != null) {
                Map<String, Object> contextMap = ac.getContextMap();
                try {
                    ReflectionContextState.setCreatingNullObjects(contextMap, true);
                    ReflectionContextState.setDenyMethodExecution(contextMap, true);
                    ReflectionContextState.setReportingConversionErrors(contextMap, true);
                    ValueStack stack = ac.getValueStack();
                    invocation.getInvocationContext().put(ActionConstants.METHOD_PARAM, methodParam(invocation, action, parameters, stack));
                } finally {
                    ReflectionContextState.setCreatingNullObjects(contextMap, false);
                    ReflectionContextState.setDenyMethodExecution(contextMap, false);
                    ReflectionContextState.setReportingConversionErrors(contextMap, false);
                }
            }
        }
        return invocation.invoke();
    }

    public TypeConverter getTypeConverterFromContext() {
        return (TypeConverter) ClassUtil.getValue(this.ognlUtil, "defaultConverter");
    }

    /**
     * 基本数据类型，及其封装类型。Date，File，String 返回true
     *
     * @param parameterType 类型
     * @return boolean
     */
    public boolean isPrimitive(Class<?> parameterType) {
        return ClassUtil.isPrimitiveOrWrapperOrStringOrDate(parameterType) || File.class.isAssignableFrom(parameterType);
    }

    public boolean isBean(Class<?> parameterType) {
        return !isPrimitive(parameterType) && !ClassUtil.isArray(parameterType) && !ClassUtil.isList(parameterType);
    }

    public Object convertValue(String propertyName, Object value, Class<?> toType) {
        return getTypeConverterFromContext().convertValue(Ognl.createDefaultContext(new HashMap<String, Object>()), new Object(), null, propertyName, value, toType);
    }

    /**
     * 用于缓存动态formBean
     */
    private transient final ConcurrentMap<Method, Class<?>> formBeanClassCache = new ConcurrentHashMap<Method, Class<?>>();

    @SuppressWarnings("unchecked")
    public Object methodParam(ActionInvocation invocation, Object action, final Map<String, Object> parameters, ValueStack stack) throws NoSuchMethodException {
        MethodParam methodParam = new MethodParam();
        Method method = getActionMethod(action.getClass(), invocation.getProxy().getMethod());// 调用的action方法
        String[] paramNames = AsmUtil.getMethodParamNames(method);// 方法参数名称
        Class<?>[] parameterTypes = method.getParameterTypes();// 方法参数类型
        if (paramNames.length == 0) {
            return methodParam;
        }
        // 单独处理 List<PropertyFilter>
        int index = ClassUtil.getParamIndex(method, PropertyFilter.class);
        Map<String, Object> params = new HashMap<String, Object>();
        if (index > -1) {
            // 获取参数位置
            String paramName = paramNames[index];
            List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
            for (Map.Entry<String, Object> entry : propertyFilterParameters(paramName, parameters).entrySet()) {
                if (MatchType.is(entry.getKey())) {
                    String name = entry.getKey();
                    if (name.startsWith("NULL_") || name.startsWith("NOTNULL_") || name.startsWith("EMPTY_") || name.startsWith("NOTEMPTY_")) {
                        filters.add(new PropertyFilter(name));
                    } else if (name.startsWith("IN") || name.startsWith("NOTIN")) {
                        Set<String> values = new HashSet<String>();
                        for (Object o : (Object[]) entry.getValue()) {
                            if (StringUtil.isNotBlank(o.toString())) {
                                values.add(o.toString());
                            }
                        }
                        if (!values.isEmpty()) {
                            filters.add(new PropertyFilter(name, values.toArray(new String[values.size()])));
                        }
                    } else {
                        String value = Array.getLength(entry.getValue()) == 0 ? null : StringUtil.nullValue(Array.get(entry.getValue(), 0));
                        if (StringUtil.isNotBlank(value)) {
                            filters.add(new PropertyFilter(name, value));
                        }
                    }
                }
            }
            params.put(paramName, filters);
            if (logger.isDebugEnabled()) {
                logger.debug("setting param [ " + paramName + " ]" + filters);
            }
        }
        // 只有一个参数，且同时是一个Bean对象时
        if (paramNames.length == 1 && isBean(parameterTypes[0])) {
            String paramName = paramNames[0];
            if (parameters.containsKey(paramName) && parameters.size() == 1) {
                Class formBeanClass = dynamicFormBean(action.getClass(), method, paramNames, parameterTypes, params);
                Object object = ClassUtil.newInstance(formBeanClass);
                setParameters(object, stack, newParameters(paramName, parameters));
                params.put(paramNames[0], ClassUtil.getValue(object, paramName));
            } else {
                Object object = newInstance(parameterTypes[0], paramName, parameters);
                setParameters(object, stack, newParameters(paramName, parameters));
                params.put(paramNames[0], object);
            }
        } else {// 自动生成FormBean
            Class formBeanClass = dynamicFormBean(action.getClass(), method, paramNames, parameterTypes, params);
            Object object = ClassUtil.newInstance(formBeanClass);
            setParameters(object, stack, parameters);
            params.putAll(ObjectUtil.toMap(object));
        }
        // 初始化Pager对象
        index = ObjectUtil.indexOf(parameterTypes, Pager.class);
        if (index > -1 && paramNames.length != 1 && ObjectUtil.isNull(params.get(paramNames[index]))) {
                params.put(paramNames[index], new Pager());
        }
        // 重新排序保存到 MethodParam 对象
        for (String paramName : paramNames) {
            methodParam.add(paramName, params.get(paramName));
        }
        return methodParam;
    }

    public Class dynamicFormBean(Class actionClass, Method actionMethod, String[] paramNames, Class[] parameterTypes, Map<String, Object> params) {
        if (!formBeanClassCache.containsKey(actionMethod)) {
            // 计算FormBean的名称
            String classname = RegexpUtil.replace(actionClass.getName(), "[a-zA-Z0-9_]+$", "") + "form." + actionClass.getSimpleName() + StringUtil.upperCaseFirst(actionMethod.getName()) + "Form";
            List<Property> properties = new ArrayList<Property>();
            for (int i = 0; i < paramNames.length; i++) {
                String paramName = paramNames[i];
                Class<?> parameterType = parameterTypes[i];
                if (params.containsKey(paramName)){
                    continue;
                }
                if (List.class.isAssignableFrom(parameterType)) {
                    List<Class> generic = ClassUtil.getMethodGenericParameterTypes(actionMethod, i);
                    properties.add(new Property(paramName, parameterType, generic.toArray(new Class[generic.size()])));
                } else {
                    properties.add(new Property(paramName, parameterType));
                }
            }
            formBeanClassCache.put(actionMethod, AsmUtil.makeClass(classname, properties.toArray(new Property[properties.size()])));
        }
        return formBeanClassCache.get(actionMethod);
    }

    /**
     * 从 parameters 中提取 List<PropertyFilter> 对应的参数
     *
     * @param paramName  参数名称
     * @param parameters 提交的参数
     * @return string
     */
    private static Map<String, Object> propertyFilterParameters(String paramName, Map<String, Object> parameters) {
        Map<String, Object> newParameters = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (RegexpUtil.find(entry.getKey(), "^" + paramName + "\\.")) {
                newParameters.put(RegexpUtil.replace(entry.getKey(), "^" + paramName + "\\.", ""), entry.getValue());
            }
        }
        if (newParameters.isEmpty()) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                if (MatchType.is(entry.getKey())) {
                    newParameters.put(entry.getKey(), entry.getValue());
                }
            }
            for (String parameterName : newParameters.keySet()) {
                parameters.remove(parameterName);
            }
        } else {
            for (String parameterName : newParameters.keySet()) {
                parameters.remove(paramName + "." + parameterName);
            }
        }
        return newParameters;
    }

    private static Map<String, Object> newParameters(String paramName, Map<String, Object> parameters) {
        Map<String, Object> newParameters = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (RegexpUtil.find(entry.getKey(), "^" + paramName + "\\.")) {
                newParameters.put(RegexpUtil.replace(entry.getKey(), "^" + paramName + "\\.", ""), entry.getValue());
            }
        }
        for (String parameterName : newParameters.keySet()) {
            parameters.remove(paramName + "." + parameterName);
        }
        return newParameters.isEmpty() ? parameters : newParameters;
    }

    public static class MethodParam {
        private Map<String, Object> params = new LinkedHashMap<String, Object>();

        public Map<String, Object> getParams() {
            return params;
        }

        public Object[] getArgs() {
            return params.values().toArray(new Object[params.size()]);
        }

        public void add(String key, Object value) {
            params.put(key, value);
        }
    }

    protected Method getActionMethod(Class<?> actionClass, String methodName) throws NoSuchMethodException {
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
                method = actionClass.getMethod(altMethodName, new Class[0]);
            } catch (NoSuchMethodException e1) {
                // throw the original one
                throw e;
            }
        }
        return method;
    }

    protected Map<String, Object> retrieveParameters(ActionContext ac) {
        return ac.getParameters();
    }

    protected Object newInstance(Class<?> parameterType, String paramName, Map<String, Object> parameters) {
        if (DynaBean.class.isAssignableFrom(parameterType)) {
            DynaBean bean = (DynaBean) ClassUtil.newInstance(parameterType);
            Object parameterObject = parameters.get(paramName + ".version.number") != null ? parameters.get(paramName + ".version.number") : parameters.get("version.number");
            String versionNumber = parameterObject == null ? null : (ClassUtil.isArray(parameterObject) ? Array.get(parameterObject, 0) : parameterObject).toString();
            if (StringUtil.isNotBlank(versionNumber)) {
                return VersionUtil.makeDynaBean((Class<DynaBean>)parameterType, versionNumber);
            } else {
                return bean;
            }
        }
        return ClassUtil.newInstance(parameterType);
    }

    /**
     * TODO 以后扩展时使用
     *
     * @param parameterTypes 参数类型
     * @param paramNames     参数名称
     * @param parameters     请求参数
     * @return Class[]
     */
    @Deprecated
    protected Class[] getParameterTypes(Class[] parameterTypes, String[] paramNames, Map<String, Object> parameters) {
        for (int i = 0; i < parameterTypes.length; i++) {
            Class parameterType = parameterTypes[i];
            if (DynaBean.class.isAssignableFrom(parameterType)) {
                Object parameterObject = parameters.get(paramNames[i] + ".version.number") != null ? parameters.get(paramNames[i] + ".version.number") : parameters.get("version.number");
                String versionNumber = parameterObject == null ? null : (ClassUtil.isArray(parameterObject) ? Array.get(parameterObject, 0) : parameterObject).toString();
                if (StringUtil.isNotBlank(versionNumber)) {
                    parameterTypes[0] = VersionUtil.makeClass(parameterType, versionNumber);
                }
            }
        }
        return parameterTypes;
    }

    protected void setParameters(Object formbean, ValueStack stack, final Map<String, Object> parameters) {
        stack.push(formbean);// 将formbean添加到stack

        Map<String, Object> params;
        Map<String, Object> acceptableParameters;
        if (ordered) {
            params = new TreeMap<String, Object>(getOrderedComparator());
            acceptableParameters = new TreeMap<String, Object>(getOrderedComparator());
            params.putAll(parameters);
        } else {
            params = new TreeMap<String, Object>(parameters);
            acceptableParameters = new TreeMap<String, Object>();
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String name = entry.getKey();
            boolean acceptableName = acceptableName(name);
            if (acceptableName) {
                acceptableParameters.put(name, entry.getValue());
            }
        }
        ValueStack newStack = valueStackFactory.createValueStack(stack);
        boolean clearableStack = newStack instanceof ClearableValueStack;
        if (clearableStack) {
            ((ClearableValueStack) newStack).clearContextValues();
            Map<String, Object> context = newStack.getContext();
            ReflectionContextState.setCreatingNullObjects(context, true);
            ReflectionContextState.setDenyMethodExecution(context, true);
            ReflectionContextState.setReportingConversionErrors(context, true);
            context.put(ActionContext.LOCALE, stack.getContext().get(ActionContext.LOCALE));
        }
        boolean memberAccessStack = newStack instanceof MemberAccessValueStack;
        if (memberAccessStack) {
            MemberAccessValueStack accessValueStack = (MemberAccessValueStack) newStack;
            accessValueStack.setAcceptProperties(acceptParams);
            accessValueStack.setExcludeProperties(excludeParams);
        }
        OgnlValueStack ognlValueStack = (OgnlValueStack) newStack;
        for (Map.Entry<String, Object> entry : acceptableParameters.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            if (RegexpUtil.isMatch(name, "^\\d+$")) {
                continue;
            }
            try {
                //如果 bean 为 动态 bean 同时动态数据版本不为 null 时,设置动态属性
                if (formbean instanceof DynaBean && ((DynaBean) formbean).getVersion() != null) {
                    Attribute attribute = ObjectUtil.find(((DynaBean) formbean).getVersion().getAttributes(), "code", name);
                    if (attribute != null) {
                        VersionUtil.getOgnlUtil(attribute.getAttributeType()).setValue(name, formbean, value);
                    } else {
                        ognlUtil.setValue(name, ognlValueStack.getContext(), formbean, value);
                    }
                } else {
                    ognlUtil.setValue(name, ognlValueStack.getContext(), formbean, value);
                }
            } catch (OgnlException e) {
                log.error(e.getMessage());
                if (name.contains(".")) {
                    com.fantasy.framework.util.ognl.OgnlUtil.getInstance().setValue(name, formbean, value);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    protected Comparator<String> getOrderedComparator() {
        return rbCollator;
    }

    protected String getParameterLogMap(Map<String, Object> parameters) {
        if (parameters == null) {
            return "NONE";
        }
        StringBuilder logEntry = new StringBuilder();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            logEntry.append(String.valueOf(entry.getKey()));
            logEntry.append(" => ");
            if (entry.getValue() instanceof Object[]) {
                Object[] valueArray = (Object[]) entry.getValue();
                logEntry.append("[ ");
                if (valueArray.length > 0) {
                    for (int indexA = 0; indexA < (valueArray.length - 1); indexA++) {
                        Object valueAtIndex = valueArray[indexA];
                        logEntry.append(String.valueOf(valueAtIndex));
                        logEntry.append(", ");
                    }
                    logEntry.append(String.valueOf(valueArray[valueArray.length - 1]));
                }
                logEntry.append(" ] ");
            } else {
                logEntry.append(String.valueOf(entry.getValue()));
            }
        }
        return logEntry.toString();
    }

    protected boolean acceptableName(String name) {
        return isWithinLengthLimit(name) && isAccepted(name) && !isExcluded(name);
    }

    protected boolean isWithinLengthLimit(String name) {
        return name.length() <= paramNameMaxLength;
    }

    protected boolean isAccepted(String paramName) {
        if (!this.acceptParams.isEmpty()) {
            for (Pattern pattern : acceptParams) {
                Matcher matcher = pattern.matcher(paramName);
                if (matcher.matches()) {
                    return true;
                }
            }
            return false;
        } else {
            return acceptedPattern.matcher(paramName).matches();
        }
    }

    protected boolean isExcluded(String paramName) {
        if (!this.excludeParams.isEmpty()) {
            for (Pattern pattern : excludeParams) {
                Matcher matcher = pattern.matcher(paramName);
                if (matcher.matches()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    protected Set<Pattern> getExcludeParamsSet() {
        return excludeParams;
    }

    public void setExcludeParams(String commaDelim) {
        Collection<String> excludePatterns = ArrayUtils.asCollection(commaDelim);
        if (excludePatterns != null) {
            excludeParams = new HashSet<Pattern>();
            for (String pattern : excludePatterns) {
                excludeParams.add(Pattern.compile(pattern));
            }
        }
    }

}
