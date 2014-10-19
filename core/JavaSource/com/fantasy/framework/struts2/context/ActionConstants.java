package com.fantasy.framework.struts2.context;

public abstract interface ActionConstants {
	public static final String HTTP_REQUEST = "com.fantasy.framework.context.ActionContext.HttpRequest";
	public static final String HTTP_RESPONSE = "com.fantasy.framework.context.ActionContext.HttpResponse";
	public static final String SERVLET_CONTEXT = "com.fantasy.framework.context.ActionContext.ServletContext";
	public static final String SESSION = "session";
	public static final String REQUEST = "request";
	public static final String RESPONSE = "response";
	public static final String PARAMETERS = "parameters";
	public static final String SERVLETACTIONCONTEXT = "com.fantasy.framework.context.ActionContext.ServletActionContext";
	public static final String PAGECONTEXT = "com.fantasy.framework.context.ActionContext.PageContext";
	public static final String ACTIONMAPPING = "com.fantasy.framework.context.ActionContext.ActionMapping";
	public static final String CONVERSION_ERRORS = "com.fantasy.framework.context.ActionContext.ConversionErrors";
	public static final Object RESULT = "com.fantasy.framework.context.ActionContext.Result";
	public static final String ACTIONRESULTS = "com.fantasy.framework.context.ActionContext.Results";
	public static final String TEMPLATE = "com.fantasy.framework.context.ActionContext.Template";
	public static final String IMAGE = "com.fantasy.framework.context.ActionContext.Image";
	public static final String APPLICATION = "application";
	public static final String XBOX = "xbox";
	public static final String JSONDATA = "jsondata";
	public static final String TEXT = "text";
	public static final String INDEX = "index";
	public final static String METHOD_PARAM = "com.fantasy.action.method.param";
}