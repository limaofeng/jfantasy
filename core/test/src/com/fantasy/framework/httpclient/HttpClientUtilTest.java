package com.fantasy.framework.httpclient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HttpClientUtilTest {

    private static final Log logger = LogFactory.getLog(HttpClientUtilTest.class);

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testDoGet() throws Exception {
//        Response response = HttpClientUtil.doGet("http://www.baidu.com");
//        logger.debug(response);
    }

    @Test
    public void testDoPost() throws Exception {

//        Map<String, Object> data = new HashMap<String, Object>();
//        data.put("dir", "goodsImages");
//        data.put("attach", new File("C:/log.png"));
//        Response response = HttpClientUtil.doPost("http://180.153.176.78:8082/bth/file/upload.do", data);
//        System.out.println(response.getText());
/*
        // 指定URL
        String targetURL = "http://180.153.176.78:8082/bth/file/upload.do";
        //指定上传文件
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
      */
    }

}