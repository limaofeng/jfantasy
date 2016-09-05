package org.jfantasy.framework.httpclient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jfantasy.framework.util.common.ObjectUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    private HttpClientUtil() {
    }

    private static final Log LOGGER = LogFactory.getLog(HttpClientUtil.class);

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
     * @param url         webUrl
     * @param queryString 请求参数字符串
     * @return {Response}
     * @throws IOException
     */
    public static Response doGet(String url, String queryString) throws IOException {
        return doGet(url, new Request(queryString));
    }

    /**
     * 执行一个带参数的get请求
     * @param url  webUrl
     * @param params 会自动将 value 做 encode 操作
     * @return {Response}
     * @throws IOException
     */
    public static Response doGet(String url, Map<String, String> params) throws IOException {
        for(Map.Entry<String,String> entry : params.entrySet()){
            entry.setValue(URLEncoder.encode(entry.getValue(),"utf-8"));
        }
        return doGet(url, new Request(params));
    }

    /**
     * 执行一个带请求信息的get请求
     *
     * @param url     webUrl webUrl
     * @param request 请求对象 请求对象
     * @return {Response}
     * @throws IOException
     */
    public static Response doGet(String url, Request request) throws IOException {
        request = request == null ? new Request() : request;
        HttpGet http = new HttpGet(url + (url.contains("?")?"":"?") + request.queryString());
        try {
            for (Header header : request.getRequestHeaders()) {
                http.addHeader(header);
            }

            CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(request.getCookies()).build();

            CloseableHttpResponse _response = client.execute(http);
            Response response = new Response(url, _response);
            response.setInputStream(new ResponseInputStream(_response));
            response.setRequestHeaders(request.getRequestHeaders());
            return response;
        }  catch (IOException e) {
            LOGGER.error("执行HTTP Get请求" + url + "时，发生异常！", e);
            throw e;
        }
    }

    /**
     * 执行一个带参数的post请求
     *
     * @param url    webUrl
     * @param params 请求参数
     * @return {Response}
     */
    public static Response doPost(String url, Map<String, ?> params) throws IOException {
        return doPost(url, new Request(params));
    }

    /**
     * 执行一个带请求信息的pos请求
     *
     * @param url     webUrl
     * @param request 请求对象
     * @return {Response}
     * @throws IOException
     */
    public static Response doPost(String url, Request request) throws IOException {
        request = ObjectUtil.defaultValue(request,new Request());
        HttpPost http = new HttpPost(url);
        for (Header header : request.getRequestHeaders()) {
            http.addHeader(header);
        }
        if (request.getUpLoadFiles().length > 0) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            for(Request.Part part : request.getUpLoadFiles()){
                builder.addPart(part.getName(),part.getContentBody());
            }
            http.setEntity(builder.build());
        }else if (!request.getParams().isEmpty()) {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
                formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
            http.setEntity(entity);
        }else if (request.getRequestBody().length > 0) {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(Arrays.asList(request.getRequestBody()), Consts.UTF_8);
            http.setEntity(entity);
        }else if (request.getRequestEntity() != null) {
            http.setEntity(request.getRequestEntity());
        }
        try {

            HttpClientBuilder builder = HttpClients.custom().setDefaultCookieStore(request.getCookies());

            if(request.getSslSocketFactory() != null){
                builder.setSSLSocketFactory(request.getSslSocketFactory());
            }

            CloseableHttpClient client = builder.build();

            CloseableHttpResponse _response = client.execute(http);
            Response response = new Response(url, _response);
            response.setInputStream(new ResponseInputStream(_response));
            response.setRequestHeaders(request.getRequestHeaders());

            return response;
        } catch (IOException e) {
            LOGGER.error("执行HTTP Post请求" + url + "时，发生异常！", e);
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
        private CloseableHttpResponse response;
        private InputStream inputStream;

        ResponseInputStream(CloseableHttpResponse response) throws IOException {
            this.response = response;
            this.inputStream = response.getEntity() == null ? new ByteArrayInputStream(new byte[0]) : response.getEntity().getContent();
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
                this.response.close();
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