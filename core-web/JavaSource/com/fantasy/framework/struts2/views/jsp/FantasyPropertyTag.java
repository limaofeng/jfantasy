package com.fantasy.framework.struts2.views.jsp;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.apache.struts2.components.Property;
import org.apache.struts2.views.jsp.PropertyTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FantasyPropertyTag extends PropertyTag {

    private static final long serialVersionUID = 8086738310339165639L;

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Property(stack);
    }

    public void setEscapeHtml(String escapeHtml) {
        super.setEscape(Boolean.valueOf(escapeHtml));
    }

}
