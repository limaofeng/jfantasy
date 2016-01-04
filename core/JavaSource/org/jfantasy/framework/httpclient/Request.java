package org.jfantasy.framework.httpclient;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * HttpClient 请求对象
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-30 下午04:45:33
 */
public class Request {

    private List<Cookie> cookies = new ArrayList<Cookie>();
    private List<Header> requestHeaders = new ArrayList<Header>();
    private Map<String, String> params = new HashMap<String, String>();
    private Part[] upLoadFiles = new Part[0];
    private Map<String, String> requestBody = new HashMap<String, String>();
    private RequestEntity requestEntity;

    public Request() {
    }

    public Request(String queryString) {
        String[] pairs = queryString.split("[;&]");
        for (String pair : pairs) {
            String[] param = pair.split("=");
            this.params.put(param[0], param.length == 1 ? "" : param[1]);
        }
    }

    public Request(RequestEntity requestEntity) {
        this.requestEntity = requestEntity;
    }

    public Request(Cookie[] cookies, Map<String, Object> params) throws FileNotFoundException {
        this(params);
        this.cookies = new ArrayList<Cookie>(Arrays.asList(cookies));
    }

    public Request(Cookie[] cookies) {
        this.cookies = new ArrayList<Cookie>(Arrays.asList(cookies));
    }

    public Request(Cookie[] cookies, Header[] requestHeaders) {
        this(cookies);
        this.requestHeaders = new ArrayList<Header>(Arrays.asList(requestHeaders));
    }

    public NameValuePair[] getRequestBody() {
        NameValuePair[] data = new NameValuePair[this.requestBody.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : this.requestBody.entrySet()) {
            data[i++] = new NameValuePair(entry.getKey(), entry.getValue());

        }
        return data;
    }

    public void setRequestBody(Map<String, String> requestBody) {
        this.requestBody = requestBody;
    }

    public Request(Map<String, ?> params) throws FileNotFoundException {
        if (params != null) {
            List<Part> parts = new ArrayList<Part>();
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                if (entry.getValue() instanceof String){
                    addParam(entry.getKey(), entry.getValue().toString());
                } else if (entry.getValue() instanceof File) {
                    parts.add(new FilePart(entry.getKey(), File.class.cast(entry.getValue())));
                }
            }
            if (!parts.isEmpty()) {
                for (Map.Entry<String, String> entry : this.getParams().entrySet()) {
                    parts.add(new StringPart(entry.getKey(), entry.getValue()));
                }
                this.getParams().clear();
                setUpLoadFiles(parts.toArray(new Part[parts.size()]));
            }
        }
    }

    public void addParam(String name, String value) {
        this.params.put(name, value);
    }

    public String queryString() {
        StringBuilder queryString = new StringBuilder();
        Iterator<String> iterator = this.params.keySet().iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            queryString.append(name).append("=").append(this.params.get(name)).append(iterator.hasNext() ? "&" : "");
        }
        return queryString.toString();
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public void addCookie(String name, String value) {
        this.cookies.add(new Cookie(null, name, value));
    }

    public void addCookie(String domain, String name, String value) {
        this.cookies.add(new Cookie(domain, name, value));
    }

    public void addRequestHeader(String name, String value) {
        this.requestHeaders.add(new Header(name, value));
    }

    public Cookie[] getCookies() {
        return this.cookies.toArray(new Cookie[this.cookies.size()]);
    }

    public Header[] getRequestHeaders() {
        return this.requestHeaders.toArray(new Header[this.requestHeaders.size()]);
    }

    public Part[] getUpLoadFiles() {
        return this.upLoadFiles;
    }

    public void setUpLoadFiles(Part[] parts) {
        this.upLoadFiles = parts;
    }

    public RequestEntity getRequestEntity() {
        return requestEntity;
    }

    public void setRequestEntity(RequestEntity requestEntity) {
        this.requestEntity = requestEntity;
    }
}