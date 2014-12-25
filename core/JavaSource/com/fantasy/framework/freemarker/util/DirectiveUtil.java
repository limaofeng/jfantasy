package com.fantasy.framework.freemarker.util;

import freemarker.template.*;
import freemarker.template.utility.DeepUnwrap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class DirectiveUtil {

	private static final Logger LOG = Logger.getLogger(DirectiveUtil.class);

	/**
	 * 获取String类型的参数值
	 * 
	 * @return 参数值
	 */
	public static String getStringParameter(String name, Map<String, TemplateModel> params) throws TemplateModelException {
		TemplateModel templateModel = params.get(name);
		if (templateModel == null) {
			return null;
		}
		if (templateModel instanceof TemplateScalarModel) {
			return ((TemplateScalarModel) templateModel).getAsString();
		} else if ((templateModel instanceof TemplateNumberModel)) {
			return ((TemplateNumberModel) templateModel).getAsNumber().toString();
		} else {
			throw new TemplateModelException("The \"" + name + "\" parameter " + "must be a string.");
		}
	}
	
	/**
	 * 获取Integer类型的参数值
	 * 
	 * @return 参数值
	 */
	public static Integer getIntegerParameter(String name, Map<String, TemplateModel> params) throws TemplateModelException {
		TemplateModel templateModel = params.get(name);
		if (templateModel == null) {
			return null;
		}
		if (templateModel instanceof TemplateScalarModel) {
			String value = ((TemplateScalarModel) templateModel).getAsString();
			if (StringUtils.isEmpty(value)) {
				return null;
			} else {
				return Integer.parseInt(value);
			}
		} else if ((templateModel instanceof TemplateNumberModel)) {
			return ((TemplateNumberModel) templateModel).getAsNumber().intValue();
		} else {
			throw new TemplateModelException("The \"" + name + "\" parameter " + "must be a integer.");
		}
	}
	
	/**
	 * 获取Boolean类型的参数值
	 * 
	 * @return 参数值
	 */
	public static boolean getBooleanParameter(String name, Map<String, TemplateModel> params) throws TemplateModelException {
		TemplateModel templateModel = params.get(name);
		if (templateModel == null) {
			return false;
		}
		if (templateModel instanceof TemplateScalarModel) {
			String value = ((TemplateScalarModel) templateModel).getAsString();
			if (StringUtils.isEmpty(value)) {
				return false;
			} else {
				return Boolean.valueOf(value);
			}
		} else if ((templateModel instanceof TemplateBooleanModel)) {
			return ((TemplateBooleanModel) templateModel).getAsBoolean();
		} else {
			throw new TemplateModelException("The \"" + name + "\" parameter " + "must be a boolean.");
		}
	}
	
	/**
	 * 获取Date类型的参数值
	 * 
	 * @return 参数值
	 */
	public static Date getDateParameter(String name, Map<String, TemplateModel> params) throws TemplateModelException {
		TemplateModel templateModel = params.get(name);
		if (templateModel == null) {
			return null;
		}
		if (templateModel instanceof TemplateScalarModel) {
			String value = ((TemplateScalarModel) templateModel).getAsString();
			if (StringUtils.isEmpty(value)) {
				return null;
			}
			try {
				String[] pattern = new String[]{"yyyy-MM","yyyyMM","yyyy/MM", "yyyyMMdd","yyyy-MM-dd","yyyy/MM/dd", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss"};
				return DateUtils.parseDate(value, pattern);
			} catch (ParseException e) {
				LOG.error(e.getMessage(),e);
				return null;
			}
		} else if ((templateModel instanceof TemplateDateModel)) {
			return ((TemplateDateModel) templateModel).getAsDate();
		} else {
			throw new TemplateModelException("The \"" + name + "\" parameter " + "must be a date.");
		}
	}
	
	/**
	 * 获取Object类型的参数值
	 * 
	 * @return 参数值
	 */
	public static Object getObjectParameter(String name, Map<String, TemplateModel> params) throws TemplateModelException {
		TemplateModel templateModel = params.get(name);
		if (templateModel == null) {
			return null;
		}
		try {
			 return DeepUnwrap.unwrap(templateModel);
		} catch (TemplateModelException e) {
			throw new TemplateModelException("The \"" + name + "\" parameter " + "must be a object.");
		}
	}

}