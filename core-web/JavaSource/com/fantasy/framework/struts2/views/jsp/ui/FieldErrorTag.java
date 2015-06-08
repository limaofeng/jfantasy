package com.fantasy.framework.struts2.views.jsp.ui;

import com.fantasy.framework.struts2.views.components.FieldError;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FieldErrorTag extends AbstractUITag {
	private static final long serialVersionUID = -182532967507726323L;
	protected String fieldName;
	protected String tagName;
	protected boolean escape = true;

	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new FieldError(stack, req, res);
	}

	protected void populateParams() {
		super.populateParams();

		FieldError fieldError = (FieldError) this.component;
		fieldError.setFieldName(this.fieldName);
		fieldError.setEscape(this.escape);
		if (StringUtil.isNotBlank(this.tagName)){
            fieldError.setTagName(this.tagName);
        }
		if ((StringUtil.isBlank(fieldError.getId())) && (StringUtil.isNotBlank(this.fieldName))){
            fieldError.setId(RegexpUtil.replace(RegexpUtil.replace(this.fieldName, "\\.", "_"), "\\[|\\]", "").concat("Tip"));
        }
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setEscape(boolean escape) {
		this.escape = escape;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
}