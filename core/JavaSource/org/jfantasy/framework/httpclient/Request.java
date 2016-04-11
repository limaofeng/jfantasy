package org.jfantasy.framework.httpclient;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.jfantasy.framework.util.common.StringUtil;

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

    private CookieStore cookieStore = new BasicCookieStore();;
    private List<Header> requestHeaders = new ArrayList<Header>();
    private Map<String, String> params = new HashMap<String, String>();
    private Part[] upLoadFiles = new Part[0];
    private Map<String, String> requestBody = new HashMap<String, String>();
    private HttpEntity requestEntity;

    public Request() {
    }

    public Request(String queryString) {
        String[] pairs = queryString.split("[;&]");
        for (String pair : pairs) {
            String[] param = pair.split("=");
            this.params.put(param[0], param.length == 1 ? "" : param[1]);
        }
    }

    public Request(HttpEntity requestEntity) {
        this.requestEntity = requestEntity;
    }

    public Request(CookieStore store, Map<String, Object> params) throws FileNotFoundException {
        this(params);
        this.cookieStore = store;
    }

    public Request(CookieStore store) {
        this.cookieStore = store;
    }

    public Request(CookieStore store, Header[] requestHeaders) {
        this(store);
        this.requestHeaders = new ArrayList<Header>(Arrays.asList(requestHeaders));
    }

    public NameValuePair[] getRequestBody() {
        NameValuePair[] data = new NameValuePair[this.requestBody.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : this.requestBody.entrySet()) {
            data[i++] = new BasicNameValuePair(entry.getKey(), entry.getValue());

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
                    parts.add(new Part(entry.getKey(), new FileBody((File) entry.getValue())));
                }
            }
            if (!parts.isEmpty()) {
                for (Map.Entry<String, String> entry : this.getParams().entrySet()) {
                    parts.add(new Part(entry.getKey(), new StringBody(entry.getValue(), ContentType.TEXT_PLAIN)));
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
        this.addCookie(null,name,value);
    }

    public void addCookie(String domain, String name, String value) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        if(StringUtil.isNotBlank(domain)){
            cookie.setDomain(domain);
        }
        this.cookieStore.addCookie(cookie);
    }

    public void addRequestHeader(String name, String value) {
        this.requestHeaders.add(new BasicHeader(name, value));
    }

    public CookieStore getCookies() {
        return this.cookieStore;
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

    public HttpEntity getRequestEntity() {
        return requestEntity;
    }

    public void setRequestEntity(HttpEntity requestEntity) {
        this.requestEntity = requestEntity;
    }

    class Part{
        String name;
        ContentBody contentBody;

        public Part(String name, ContentBody contentBody) {
            this.name = name;
            this.contentBody = contentBody;
        }

        public String getName() {
            return name;
        }

        public ContentBody getContentBody() {
            return contentBody;
        }

    }

}