package com.fantasy.framework.mock;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

public class MockRequest implements HttpServletRequest {
    protected String requestURI;
    protected int serverPort = 80;
    protected String queryString;
    protected String method = "GET";
    protected Hashtable<String, String> headers = new Hashtable<String, String>();
    protected Hashtable<String, Object> attrs = new Hashtable<String, Object>();
    protected Hashtable<String, Object> parameters = new Hashtable<String, Object>();
    protected String authType;
    protected int contentLength;
    protected String contentType;
    protected String contextPath = "";
    protected Cookie[] cookies;
    protected String pathInfo;
    protected String pathTranslated;
    protected String protocol;
    protected String remoteAddr;
    protected String remoteHost;
    protected String remoteUser;
    protected String requestedSessionId;
    protected String requestUrl;
    protected String serverName;
    protected String servletPath;
    protected String scheme;
    protected int localPort = 0;
    protected ArrayList<String> roles = new ArrayList<String>();
    protected String characterEncoding;

    public MockRequest() {
    }

    public MockRequest(String requestURI) {
        this.requestURI = (this.contextPath + requestURI);
        this.servletPath = requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getAuthType() {
        return this.authType;
    }

    public Cookie[] getCookies() {
        return this.cookies;
    }

    public long getDateHeader(String s) {
        return 0L;
    }

    public String getHeader(String s) {
        if (s == null) {
            return null;
        }
        return this.headers.get(s);
    }

    public Enumeration<String> getHeaders(String s) {
        return this.headers.elements();
    }

    public Enumeration<String> getHeaderNames() {
        return this.headers.keys();
    }

    public int getIntHeader(String s) {
        return 0;
    }

    public String getMethod() {
        return this.method;
    }

    public String getPathInfo() {
        return this.pathInfo;
    }

    public String getPathTranslated() {
        return this.pathTranslated;
    }

    public String getContextPath() {
        return this.contextPath;
    }

    public String getQueryString() {
        return this.queryString;
    }

    public String getRemoteUser() {
        return this.remoteUser;
    }

    public boolean isUserInRole(String s) {
        return this.roles.contains(s);
    }

    public Principal getUserPrincipal() {
        return null;
    }

    public String getRequestedSessionId() {
        return this.requestedSessionId;
    }

    public String getRequestURI() {
        return this.requestURI;
    }

    public StringBuffer getRequestURL() {
        if (this.requestUrl == null)
            return null;
        return new StringBuffer(this.requestUrl);
    }

    public String getServletPath() {
        return this.servletPath;
    }

    public HttpSession getSession(boolean b) {
        return null;
    }

    public HttpSession getSession() {
        return null;
    }

    public boolean isRequestedSessionIdValid() {
        return false;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    public Object getAttribute(String s) {
        return this.attrs.get(s);
    }

    public Enumeration<String> getAttributeNames() {
        return null;
    }

    public String getCharacterEncoding() {
        return this.characterEncoding;
    }

    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
        this.characterEncoding = s;
    }

    public int getContentLength() {
        return this.contentLength;
    }

    public String getContentType() {
        return this.contentType;
    }

    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    public String getParameter(String s) {
        return (String) this.parameters.get(s);
    }

    public Enumeration<String> getParameterNames() {
        return null;
    }

    public String[] getParameterValues(String s) {
        return new String[0];
    }

    public Map<String, String[]> getParameterMap() {
        return null;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public String getScheme() {
        return this.scheme;
    }

    public String getServerName() {
        return this.serverName;
    }

    public int getServerPort() {
        return this.serverPort;
    }

    public BufferedReader getReader() throws IOException {
        return null;
    }

    public String getRemoteAddr() {
        return this.remoteAddr;
    }

    public String getRemoteHost() {
        return this.remoteHost;
    }

    public void setAttribute(String s, Object o) {
        this.attrs.put(s, o);
    }

    public void removeAttribute(String s) {
        this.attrs.remove(s);
    }

    public Locale getLocale() {
        return null;
    }

    public Enumeration<Locale> getLocales() {
        return null;
    }

    public boolean isSecure() {
        return false;
    }

    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    public String getRealPath(String s) {
        return null;
    }

    public int getRemotePort() {
        return 0;
    }

    public String getLocalName() {
        return null;
    }

    public String getLocalAddr() {
        return null;
    }

    public int getLocalPort() {
        return this.localPort;
    }

    public void setQueryString(String s) {
        this.queryString = s;
    }

    public void setMethod(String s) {
        this.method = s;
    }

    public void setHeader(String name, String value) {
        this.headers.put(name, value);
    }

    public void setContentType(String s) {
        this.contentType = s;
    }

    public void setContextPath(String s) {
        this.contextPath = s;
    }

    public void addParameter(String s, String s1) {
        this.parameters.put(s, s1);
    }

    public void setProtocol(String s) {
        this.protocol = s;
    }

    public void setRemoteAddr(String s) {
        this.remoteAddr = s;
    }

    public void setRemoteHost(String s) {
        this.remoteHost = s;
    }

    public void setRequestedSessionId(String s) {
        this.requestedSessionId = s;
    }

    public void setServerName(String s) {
        this.serverName = s;
    }

    public void setScheme(String s) {
        this.scheme = s;
    }

    public void addHeader(String s, String s1) {
        this.headers.put(s, s1);
    }

}