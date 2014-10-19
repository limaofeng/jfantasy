package com.fantasy.framework.struts2;

import com.fantasy.framework.struts2.context.ActionConstants;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.inject.Inject;
import org.apache.struts2.interceptor.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class ActionSupport extends com.opensymphony.xwork2.ActionSupport implements ParameterAware, RequestAware, ServletRequestAware, ServletResponseAware, SessionAware, CookiesAware, Preparable, ActionConstants {
    protected static final long serialVersionUID = -2580531325752002722L;
    public static final String ROOT = "root";
    protected Map<String, Object> attrs;
    protected Map<String, Object> session;
    protected Map<String, String> cookies;
    protected transient HttpServletRequest request;
    protected transient HttpServletResponse response;
    protected transient Map<String, String[]> parameters;
    protected String extension;
    private String pageTheme;

    public void setRequest(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public void setCookiesMap(Map<String, String> cookiesMap) {
        this.cookies = cookiesMap;
    }

    public void prepare() throws Exception {
    }

    public void setParameters(Map<String, String[]> parameters) {
        this.parameters = parameters;
    }

    @Inject("struts.action.extension")
    public void setExtensions(String extensions) {
        this.extension = com.fantasy.framework.util.common.StringUtil.nullValue(extensions).split(",")[0];
    }

    @Inject("fantasy.page.theme")
    public void setPageTheme(String pageTheme) {
        this.pageTheme = pageTheme;
    }

    public String getPageTheme() {
        return this.pageTheme;
    }

    public String getExtension() {
        return this.extension;
    }

    public Map<String,Object> jsonRoot(String key, Object value) {
        Map<String,Object> root = new HashMap<String, Object>();
        root.put(key,value);
        return root;
    }
}