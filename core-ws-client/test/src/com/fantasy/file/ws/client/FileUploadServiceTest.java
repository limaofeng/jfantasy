package com.fantasy.file.ws.client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.activation.DataHandler;

public class FileUploadServiceTest {

    private FileUploadService fileUploadService;

    @Before
    public void setUp() throws Exception {
        fileUploadService = new FileUploadService();
        fileUploadService.setEndPointReference("http://localhost:8080/services");
        fileUploadService.setTargetNamespace("http://ws.file.fantasy.com");
        fileUploadService.setAxis2xml("classpath:axis2.xml");
        fileUploadService.afterPropertiesSet();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testUploadFile() throws Exception {
        DataHandler handler = new DataHandler(FileUploadServiceTest.class.getResource("/files/mm.mp4").toURI().toURL());
        fileUploadService.upload(handler,"test.mp4","test");
    }

    @Test
    public void testUpload() throws Exception {
        DataHandler handler = new DataHandler(FileUploadServiceTest.class.getResource("/files/mm.mp4").toURI().toURL());
        fileUploadService.upload(handler, "test.mp4", "test");
    }
}