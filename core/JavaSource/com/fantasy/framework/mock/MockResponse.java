package com.fantasy.framework.mock;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class MockResponse implements HttpServletResponse {
    protected Hashtable<String, String> responseHeaders = new Hashtable<String, String>();
    protected int status = 200;
    protected String redirectedUrl;
    protected List<Cookie> cookies = new ArrayList<Cookie>();
    protected Locale locale;

    public void addCookie(Cookie cookie) {
        this.cookies.add(cookie);
    }

    public boolean containsHeader(String s) {
        return false;
    }

    public String encodeURL(String s) {
        if (s == null)
            return null;
        if (s.indexOf("http:") == 0)
            return s;
        if (s.contains("?")) {
            return s.substring(0, s.indexOf("?")) + ";mockencoded=test" + s.substring(s.indexOf("?"), s.length());
        }
        return s.concat(";mockencoded=test");
    }

    public String encodeRedirectURL(String s) {
        return encodeURL(s);
    }

    /**
     * @deprecated
     */
    public String encodeUrl(String s) {
        return encodeURL(s);
    }

    /**
     * @deprecated
     */
    public String encodeRedirectUrl(String s) {
        return encodeURL(s);
    }

    public void sendError(int i, String s) throws IOException {
    }

    public void sendError(int i) throws IOException {
    }

    public void sendRedirect(String s) throws IOException {
        this.redirectedUrl = s;
    }

    public void setDateHeader(String s, long l) {
        this.responseHeaders.put(s, l + "");
    }

    public void addDateHeader(String s, long l) {
        this.responseHeaders.put(s, l + "");
    }

    public void setHeader(String s, String s1) {
        this.responseHeaders.put(s, s1);
    }

    public void addHeader(String s, String s1) {
        this.responseHeaders.put(s, s1);
    }

    public void setIntHeader(String s, int i) {
        this.responseHeaders.put(s, i + "");
    }

    public void addIntHeader(String s, int i) {
        this.responseHeaders.put(s, i + "");
    }

    public void setStatus(int i) {
        this.status = i;
    }

    /**
     * @deprecated
     */
    public void setStatus(int i, String s) {
    }

    public String getCharacterEncoding() {
        return null;
    }

    public String getContentType() {
        return null;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    public PrintWriter getWriter() throws IOException {
        return null;
    }

    public void setCharacterEncoding(String s) {
    }

    public void setContentLength(int i) {
    }

    public void setContentType(String s) {
    }

    public void setBufferSize(int i) {
    }

    public int getBufferSize() {
        return 0;
    }

    public void flushBuffer() throws IOException {
    }

    public void resetBuffer() {
    }

    public boolean isCommitted() {
        return false;
    }

    public void reset() {
    }

    public void setLocale(Locale l) {
        this.locale = l;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public String getHeader(String s) {
        return this.responseHeaders.get(s);
    }

    public int getStatus() {
        return this.status;
    }

}