package com.fantasy.framework.httpclient;

import com.fantasy.framework.util.common.StringUtil;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 用于模拟http请求
 *
 * @author 李茂峰
 * @version 1.0
 *          依赖 commons-httpclient.jar
 * @since 2012-11-30 下午04:38:14
 */
public class HttpClientUtil {

    private static final Log logger = LogFactory.getLog(HttpClientUtil.class);

    static {
        Protocol myhttps = new Protocol("https", new DefaultProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);
    }

    /**
     * 执行get请求
     *
     * @param url webUrl webUrl
     * @return {Response}
     * @throws IOException
     */
    public static Response doGet(String url) throws IOException {
        return doGet(url, new Request());
    }

    /**
     * 执行一个带参数的get请求
     *
     * @param url webUrl         webUrl
     * @param queryString 请求参数字符串
     * @return {Response}
     * @throws IOException
     */
    public static Response doGet(String url, String queryString) throws IOException {
        return doGet(url, new Request(queryString));
    }

    /**
     * 执行一个带参数的get请求
     *
     * @param url webUrl webUrl
     * @param params 请求参数 请求参数
     * @return {Response}
     * @throws IOException
     */
    public static Response doGet(String url, Map<String, Object> params) throws IOException {
        return doGet(url, new Request(params));
    }

    /**
     * 执行一个带请求信息的get请求
     *
     * @param url webUrl webUrl
     * @param request 请求对象 请求对象
     * @return {Response}
     * @throws IOException
     */
    public static Response doGet(String url, Request request) throws IOException {
        request = request == null ? new Request() : request;
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        try {
            if (StringUtil.isNotBlank(request.queryString())) {
                method.setQueryString(URIUtil.encodeQuery(request.queryString()));
            }
            for (Header header : request.getRequestHeaders()) {
                method.addRequestHeader(header);
            }
            client.getState().addCookies(request.getCookies());
            client.executeMethod(method);
            Response response = new Response(url, method.getStatusCode());
            response.setInputStream(new ResponseInputStream(method, method.getResponseBodyAsStream()));
            response.setCookies(client.getState().getCookies());
            response.setRequestHeaders(method.getRequestHeaders());
            response.setResponseHeaders(method.getResponseHeaders());
            return response;
        } catch (URIException e) {
            logger.error("执行HTTP Get请求时，编码查询字符串“" + request.queryString() + "”发生异常！", e);
            throw e;
        } catch (IOException e) {
            logger.error("执行HTTP Get请求" + url + "时，发生异常！", e);
            throw e;
        }
    }

    /**
     * 执行一个带参数的post请求
     *
     * @param url webUrl
     * @param params 请求参数
     * @return {Response}
     */
    public static Response doPost(String url, Map<String, Object> params) throws IOException {
        return doPost(url, new Request(params));
    }

    /**
     * 执行一个带请求信息的post请求
     *
     * @param url webUrl
     * @param request 请求对象
     * @return {Response}
     * @throws IOException
     */
    public static Response doPost(String url, Request request) throws IOException {
        request = request == null ? new Request() : request;
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        for (Header header : request.getRequestHeaders()) {
            method.addRequestHeader(header);
        }
        if (!request.getParams().isEmpty()) {
            for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
                method.setParameter(entry.getKey(), entry.getValue());
            }
        }
        if (request.getUpLoadFiles().length > 0) {
            method.setRequestEntity(new MultipartRequestEntity(request.getUpLoadFiles(), method.getParams()));
        }
        if (request.getRequestBody().length > 0) {
            method.setRequestBody(request.getRequestBody());
        }
        if (request.getRequestEntity() != null){
            method.setRequestEntity(request.getRequestEntity());
        }
        client.getState().addCookies(request.getCookies());
        try {
            client.executeMethod(method);
            Response response = new Response(url, method.getStatusCode());
            response.setInputStream(new ResponseInputStream(method, method.getResponseBodyAsStream()));
            response.setCookies(client.getState().getCookies());
            response.setRequestHeaders(method.getRequestHeaders());
            response.setResponseHeaders(method.getResponseHeaders());
            return response;
        } catch (IOException e) {
            logger.error("执行HTTP Post请求" + url + "时，发生异常！", e);
            throw e;
        }
    }

    /**
     * 响应流封装类
     * 请求完成并不会马上解析响应。所以需要缓存InputStream，并在成功读取后自动关闭连接
     *
     * @author 李茂峰
     * @version 1.0
     * @since 2012-11-30 下午04:42:25
     */
    private static class ResponseInputStream extends InputStream {
        private HttpMethod httpMethod;
        private InputStream inputStream;

        public ResponseInputStream(HttpMethod httpMethod, InputStream inputStream) {
            this.httpMethod = httpMethod;
            this.inputStream = inputStream;
        }

        public int read() throws IOException {
            return this.inputStream.read();
        }

        public int available() throws IOException {
            return this.inputStream.available();
        }

        public void close() throws IOException {
            try {
                this.inputStream.close();
            } finally {
                this.httpMethod.releaseConnection();
            }
        }

        public synchronized void mark(int readlimit) {
            this.inputStream.mark(readlimit);
        }

        public boolean markSupported() {
            return this.inputStream.markSupported();
        }

        public int read(byte[] b, int off, int len) throws IOException {
            return this.inputStream.read(b, off, len);
        }

        public int read(byte[] b) throws IOException {
            return this.inputStream.read(b);
        }

        public synchronized void reset() throws IOException {
            this.inputStream.reset();
        }

        public long skip(long n) throws IOException {
            return this.inputStream.skip(n);
        }

    }

}