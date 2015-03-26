package com.restful;

import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.httpclient.HttpClientUtil;
import com.fantasy.framework.httpclient.Request;
import com.fantasy.framework.httpclient.Response;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hebo on 2015/3/22.
 * restful测试
 */
public class RestfulHttp {

    public static void main(String[] args) throws Exception{
       Map<String, Object> params = new HashMap<String, Object>();
        params.put("id","1");
        params.put("title","post11");
        params.put("summary","post");
        params.put("_method","put");
        String url = "http://localhost:8080/api/articles/1";
         Response response =  HttpClientUtil.doPost(url, params);
      /* Response response  = doPut(url,new Request(params));*/
        /* String y = doDelete("http://localhost:8080/api/articles/1","UTF-8",true);*/
       /* testPut();*/
    }

    public static String doDelete(String url, String charset, boolean pretty) {
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        HttpMethod method = new DeleteMethod(url);
        try {

            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty)
                        response.append(line).append(System.getProperty("line.separator"));
                    else
                        response.append(line);
                }
                reader.close();
            }
        } catch (URIException e) {
          throw new IgnoreException("执行HTTP Get请求时，编码查询字符串 发生异常！");
        } catch (IOException e) {
            throw new IgnoreException("执行HTTP Get请求时，编码查询字符串 发生异常！");
        } finally {
            method.releaseConnection();
        }
        return response.toString();
    }


    public static Response doPut(String url, Request request) throws IOException {
        request = request == null ? new Request() : request;
        HttpClient client = new HttpClient();
        PutMethod method = new PutMethod(url);
        for (Header header : request.getRequestHeaders()) {
            method.addRequestHeader(header);
        }
        if (!request.getParams().isEmpty()) {
            HttpMethodParams p = new HttpMethodParams();
            for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
                p.setParameter(entry.getKey(), entry.getValue());
            }
            method.setParams(p);
        }

        if (request.getUpLoadFiles().length > 0) {
            method.setRequestEntity(new MultipartRequestEntity(request.getUpLoadFiles(), method.getParams()));
        }
       /* if (request.getRequestBody().length > 0) {
            method.setRequestBody(request.getRequestBody());
        }*/
        if (request.getRequestEntity() != null){
            method.setRequestEntity(request.getRequestEntity());
        }
        client.getState().addCookies(request.getCookies());
        try {
            client.executeMethod(method);
            Response response = new Response(url, method.getStatusCode());
           /* response.setInputStream(new ResponseInputStream(method, method.getResponseBodyAsStream()));*/
            response.setCookies(client.getState().getCookies());
            response.setRequestHeaders(method.getRequestHeaders());
            response.setResponseHeaders(method.getResponseHeaders());
            return response;
        } catch (IOException e) {
            throw new IgnoreException("执行HTTP Post请求" + url + "时，发生异常！");
        }
    }

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

    /*public static String doPut(String url, Map<String, Object> params, String charset, boolean pretty){
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        HttpMethod method = new PutMethod(url);
        if (params != null) {
            HttpMethodParams p = new HttpMethodParams();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                p.setParameter(entry.getKey(), entry.getValue());
            }
            method.setParams(p);
        }
        try {
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty)
                        response.append(line).append(System.getProperty("line.separator"));
                    else
                        response.append(line);
                }
                reader.close();
            }
        } catch (IOException e) {
            throw new IgnoreException("执行HTTP Post请求" + url + "时，发生异常！");
        } finally {
            method.releaseConnection();
        }
        return response.toString();
    }*/

    /*public static void doPut(String urlStr,Map<String,Object> paramMap) throws Exception{
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("PUT");
        String paramStr = prepareParam(paramMap);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(paramStr.toString().getBytes("utf-8"));
        os.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line ;
        String result ="";
        while( (line =br.readLine()) != null ){
            result += "/n"+line;
        }
        System.out.println(result);
        br.close();

    }

    private static String prepareParam(Map<String,Object> paramMap){
        StringBuffer sb = new StringBuffer();
        if(paramMap.isEmpty()){
            return "" ;
        }else{
            for(String key: paramMap.keySet()){
                String value = (String)paramMap.get(key);
                if(sb.length()<1){
                    sb.append(key).append("=").append(value);
                }else{
                    sb.append("&").append(key).append("=").append(value);
                }
            }
            return sb.toString();
        }
    }*/

/*    public static void testUpdate() throws Exception {
     *//*   Map<String, Object> param = new HashMap<String, Object>();
        param.put("id","1");
        param.put("title","测试post请求");*//*
        String url = "http://localhost:8080/api/articles/1";
        org.apache.http.client.HttpClient client = HttpClientBuilder.create().build();
        HttpPut put = new HttpPut(url);
        put.setHeader("Content-type", "application/json");
        String jo ="{\"id\":\"1\",\"title\":\"put\"}";
        StringEntity params =new StringEntity(jo);
        put.setEntity(params);


        HttpResponse response = client.execute(put);
        System.out.println("Response Code:"+response.getStatusLine().getStatusCode());
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println("result:"+result);
    }*/
}
