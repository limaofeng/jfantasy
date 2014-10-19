package com.fantasy.framework.tags.ui;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import com.fantasy.framework.tags.AbstractUITag;
import com.fantasy.framework.tags.model.Authority;
import com.fantasy.framework.tags.model.UIBean;
import com.fantasy.framework.util.Stack;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.security.SpringSecurityUtils;

@SuppressWarnings("unchecked")
public class AuthorityTag extends AbstractUITag<Authority> {

    private static final long serialVersionUID = -8382021749254771481L;

    @Override
    public int doStartTag() throws JspException {
	String requestURI = RegexpUtil.replaceFirst(getUIBean().getUrl(), "^" + getRequest().getContextPath(), "");
	requestURI = requestURI.indexOf('?') > 0 ? requestURI.substring(0, requestURI.indexOf('?')) : requestURI;
	if (SpringSecurityUtils.hasAnyAuthority("URL_" + requestURI)) {
	    return Tag.EVAL_BODY_INCLUDE;
	} else {
	    return Tag.SKIP_BODY;
	}
    }

    // Tag.EVAL_BODY_INCLUDE 包含主体内容
    // Tag.SKIP_BODY 不包含主体内容
    // Tag.EVAL_PAGE 包含后面的页面内容
    // Tag.SKIP_PAGE 不包含主体的内容

    @Override
    public int doEndTag() throws JspException {
	((Stack<UIBean>) this.pageContext.getAttribute(TAG_STACK)).pop();
	return EVAL_PAGE;
    }

    public String getUrl() {
	return getUIBean().getUrl();
    }

    public void setUrl(String url) {
	this.getUIBean().setUrl(url);
    }
}