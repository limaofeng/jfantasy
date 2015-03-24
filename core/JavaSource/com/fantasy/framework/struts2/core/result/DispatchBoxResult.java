package com.fantasy.framework.struts2.core.result;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.TextParseUtil;

public class DispatchBoxResult extends ServletDispatcherResult {
	private static final long serialVersionUID = -7770912385511162450L;

	public void setReloadLocation(String reloadLocation) {
		ServletActionContext.getRequest().setAttribute("reloadLocation", TextParseUtil.translateVariables(reloadLocation, ActionContext.getContext().getActionInvocation().getStack()));
	}
}