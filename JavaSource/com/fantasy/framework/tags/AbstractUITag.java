package com.fantasy.framework.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fantasy.framework.freemarker.FreeMarkerConfigurationFactoryBean;
import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.tags.model.UIBean;
import com.fantasy.framework.tags.ui.AnchorTag;
import com.fantasy.framework.tags.ui.ButtonTag;
import com.fantasy.framework.tags.ui.FormTag;
import com.fantasy.framework.util.Stack;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.security.SpringSecurityUtils;

import freemarker.template.Template;

@SuppressWarnings("unchecked")
public abstract class AbstractUITag<T extends UIBean> extends TagSupport {

	public static final String TAG_STACK = "tag.stack";

	private static final long serialVersionUID = -5265883826096993641L;

	private final Log logger = LogFactory.getLog(getClass());

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);
		if (ObjectUtil.isNull(this.pageContext.getAttribute(TAG_STACK))) {
			this.pageContext.setAttribute(TAG_STACK, new Stack<T>());
		}
		Stack<T> stack = (Stack<T>) this.pageContext.getAttribute(TAG_STACK);
		try {
			stack.push((T) ClassUtil.getSuperClassGenricType(getClass()).newInstance());
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public HttpServletResponse getResponse() {
		return (HttpServletResponse) this.pageContext.getResponse();
	}

	public HttpServletRequest getRequest() {
		return (HttpServletRequest) this.pageContext.getRequest();
	}

	public int doStartTag() throws JspException {
		Stack<T> stack = (Stack<T>) this.pageContext.getAttribute(TAG_STACK);
		T model = stack.peek();
		String requestURI = null;
		if (getClass().isAssignableFrom(ButtonTag.class)) {
			requestURI = StringUtil.nullValue(ClassUtil.getValue(model, "url"));
		} else if (getClass().isAssignableFrom(AnchorTag.class)) {
			requestURI = StringUtil.nullValue(ClassUtil.getValue(model, "href"));
		} else if (getClass().isAssignableFrom(FormTag.class)) {
			requestURI = StringUtil.nullValue(ClassUtil.getValue(model, "action"));
		} else {
			writer(model.getDefaultOpenTemplate(), model);
			return Tag.EVAL_BODY_INCLUDE;
		}
		requestURI = RegexpUtil.replaceFirst(requestURI, "^" + getRequest().getContextPath(), "");
		requestURI = requestURI.indexOf('?') > 0 ? requestURI.substring(0, requestURI.indexOf('?')) : requestURI;
		if (SpringSecurityUtils.hasAnyAuthority("URL_" + requestURI)) {// TODO
			writer(model.getDefaultOpenTemplate(), model);
			return Tag.EVAL_BODY_INCLUDE;
		} else {
			return Tag.SKIP_BODY;
		}
	}

	// Tag.EVAL_BODY_INCLUDE 包含主体内容
	// Tag.SKIP_BODY 不包含主体内容
	// Tag.EVAL_PAGE 包含后面的页面内容
	// Tag.SKIP_PAGE 不包含主体的内容
	public int doEndTag() throws JspException {
		T model = ((Stack<T>) this.pageContext.getAttribute(TAG_STACK)).pop();
		String requestURI = null;
		if (getClass().isAssignableFrom(ButtonTag.class)) {
			requestURI = StringUtil.nullValue(ClassUtil.getValue(model, "url"));
		} else if (getClass().isAssignableFrom(AnchorTag.class)) {
			requestURI = StringUtil.nullValue(ClassUtil.getValue(model, "href"));
		} else if (getClass().isAssignableFrom(FormTag.class)) {
			requestURI = StringUtil.nullValue(ClassUtil.getValue(model, "action"));
		} else {
			writer(model.getDefaultTemplate(), model);
			return Tag.EVAL_PAGE;
		}
		requestURI = RegexpUtil.replaceFirst(requestURI, "^" + getRequest().getContextPath(), "");
		requestURI = requestURI.indexOf('?') > 0 ? requestURI.substring(0, requestURI.indexOf('?')) : requestURI;
		if (SpringSecurityUtils.hasAnyAuthority("URL_" + requestURI)) {
			writer(model.getDefaultTemplate(), model);
			return Tag.EVAL_PAGE;
		} else {
			return Tag.EVAL_PAGE;
		}
	}

	protected T getUIBean() {
		return ((Stack<T>) this.pageContext.getAttribute(TAG_STACK)).peek();
	}

	/**
	 * 调用ftl模板写标签
	 * 
	 * @param templateName
	 * @param tagModel
	 */
	public void writer(String templateName, T tagModel) {
		if (StringUtil.isBlank(templateName))
			return;
		FreeMarkerConfigurationFactoryBean freemarkerService = (FreeMarkerConfigurationFactoryBean) SpringContextUtil.getBean("freemarkerService", FreeMarkerConfigurationFactoryBean.class);
		Template template;
		try {
			template = freemarkerService.getConfiguration().getTemplate(templateName);
			FreeMarkerTemplateUtils.writer(tagModel, template, this.pageContext.getOut());
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public void setId(String id) {
		getUIBean().setId(id);
	}

	public void setHref(String href) {
		getUIBean().setHref(href);
	}

	public void setName(String name) {
		getUIBean().setName(name);
	}

	public void setOnclick(String onclick) {
		getUIBean().setOnclick(onclick);
	}

	public void setCssClass(String cssClass) {
		getUIBean().setCssClass(cssClass);
	}

	public void setStyle(String style) {
		getUIBean().setStyle(style);
	}

	public void setTitle(String title) {
		getUIBean().setTitle(title);
	}
}