package com.fantasy.framework.httpclient;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lmf on 14-5-30.
 */
public class TestHttpClientUtil {

    @Test
    public void testUpload() throws IOException {


        Map<String, Object> data = new HashMap<String, Object>();
        data.put("dir", "goodsImages");
        data.put("attach", new File("C:/log.png"));
        Response response = HttpClientUtil.doPost("http://180.153.176.78:8082/bth/file/upload.do", data);
//        Response response = HttpClientUtil.doPost("http://180.153.176.78:8082/bth/file/upload.do",data);
        System.out.println(response.getText());
    }

    @Test
    public void testEncode() throws IOException {
        String url = "http://localhost:63342/template/fides-demo/login-alt.html";
        Response response = HttpClientUtil.doGet(url);
        System.out.println(response.getText().equals(HttpClientUtil.doGet("http://localhost:8080/login.do").getText()));
    }


    @Test
    public void testUpload2() {
        // TODO 指定URL
        String targetURL = "http://180.153.176.78:8082/bth/file/upload.do";
        // TODO 指定上传文件
        File targetFile = new File("C:/log.png");
        PostMethod filePost = new PostMethod(targetURL);
        try {
            // 上传参数
            Part[] parts = {new FilePart("attach", targetFile), new StringPart("dir", "goodsImages")};
            filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
            HttpClient client = new HttpClient();
            client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
            int status = client.executeMethod(filePost);
            if (status == HttpStatus.SC_OK) {
                System.out.println("上传成功:" + filePost.getResponseBodyAsString());
            } else {
                System.out.println("上传失败");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            filePost.releaseConnection();
        }
    }

    @Test
    public void testHttpWebService() throws IOException {
        String body = "<?xml version='1.0' encoding='UTF-8'?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><wsa:To>http://127.0.0.1:8080/services/BrandService</wsa:To><wsa:MessageID>urn:uuid:d28f6a05-46d7-4a22-a6a7-954d9b9fbed5</wsa:MessageID><wsa:Action>urn:anonOutInOpResponse</wsa:Action></soapenv:Header><soapenv:Body><brands xmlns=\"http://ws.goods.mall.fantasy.com\" /></soapenv:Body></soapenv:Envelope>";
        Request request = new Request(new StringRequestEntity(body, "text/xml", "UTF-8"));
//        request.addRequestHeader("content-type","text/xml; charset=UTF-8");
        request.addRequestHeader("soapaction", "urn:anonOutInOpResponse");
        request.addRequestHeader("user-agent", "Axis2");
//        request.addRequestHeader("host","127.0.0.1:8080");
//        request.addRequestHeader("transfer-encoding","chunked");

        Response response = HttpClientUtil.doPost("http://127.0.0.1:8080/services/BrandService", request);


        for (Header header : response.getRequestHeaders()) {
            System.out.println(header.getName() + "=" + header.getValue());
        }

        System.out.println(response.getText());


    }

}
