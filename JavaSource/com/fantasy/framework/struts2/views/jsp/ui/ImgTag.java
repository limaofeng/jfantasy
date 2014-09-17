package com.fantasy.framework.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;

import com.fantasy.framework.struts2.views.components.Img;
import com.opensymphony.xwork2.util.ValueStack;

public class ImgTag extends AbstractUITag {
	private static final long serialVersionUID = 6229581103256737298L;

	protected String src;
	protected String alt;
	protected String ratio;
	
	public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		return new Img(stack, request, response);
	}

	protected void populateParams() {
		super.populateParams();

		Img img = (Img) component;
		img.setSrc(src);
		img.setAlt(alt);
		img.setRatio(ratio);
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

}