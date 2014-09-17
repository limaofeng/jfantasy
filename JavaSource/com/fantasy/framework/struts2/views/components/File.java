package com.fantasy.framework.struts2.views.components;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import com.fantasy.file.bean.FileDetail;
import com.opensymphony.xwork2.util.ValueStack;

@StrutsTag(name = "file", tldTagClass = "com.fantasy.framework.struts2.views.jsp.ui.FileTag"/*, allowDynamicAttributes = true*/, description = "")
public class File extends UIBean {
	final public static String TEMPLATE = "file-dowload";

	protected List<FileDetail> files;

	public File(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	protected String getDefaultTemplate() {
		return TEMPLATE;
	}

	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		addParameter("files", files);
		addParameter("contextPath",this.request.getContextPath());
	}

	@StrutsTagAttribute(description = " HTML for attribute")
	public void setFiles(List<FileDetail> fileDetails) {
		this.files = fileDetails;
	}

	public List<FileDetail> getFiles() {
		return files;
	}
	
}
