package com.fantasy.framework.struts2.views.jsp.ui;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.framework.struts2.views.components.File;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.opensymphony.xwork2.util.ValueStack;

public class FileTag extends AbstractUITag {
	private static final long serialVersionUID = 4008321310097730458L;

	protected String files;

	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new File(stack, req, res);
	}

	protected void populateParams() {
		super.populateParams();
		FileDetail[] details = StringUtil.isBlank(files) ? new FileDetail[0] : JSON.deserialize(files, FileDetail[].class);
		((File) component).setFiles(Arrays.asList(details));
	}

	public void setFiles(String files) {
		this.files = files;
	}
}
