package com.fantasy.framework.service;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class FTPServiceTest {

    private FTPService ftpService;

    @Before
    public void setUp() throws Exception {
        ftpService = new FTPService();
        ftpService.setSystemKey(FTPClientConfig.SYST_MACOS_PETER);
        ftpService.setHostname("115.29.185.235");
        ftpService.setUsername("root");
        ftpService.setPassword("Li19881002");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testListFiles() throws Exception {
        FTPClient ftpClient = ftpService.login();
        ftpService.closeConnection(ftpClient);
    }

    @Test
    public void testExist() throws Exception {

    }

    @Test
    public void testIsDir() throws Exception {

    }

    @Test
    public void testUploadFile() throws Exception {

    }

    @Test
    public void testUploadFolder() throws Exception {

    }

    @Test
    public void testDeleteRemoteFile() throws Exception {

    }

    @Test
    public void testDeleteRemoteFolder() throws Exception {

    }

    @Test
    public void testGetInputStream() throws Exception {

    }

    @Test
    public void testGetLastModified() throws Exception {

    }

    @Test
    public void testGetOutputStream() throws Exception {

    }
}