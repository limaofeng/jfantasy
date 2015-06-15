package com.fantasy.framework.util.common;

import com.fantasy.file.manager.FTPFileManager;
import com.fantasy.framework.service.FTPService;
import com.fantasy.framework.util.common.file.FileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.io.File;

public class FileUtilTest {

    private final static Log LOG = LogFactory.getLog(FileUtilTest.class);

    @Test
    public void fileSize() {
        System.out.println(FileUtil.fileSize(1024 + 1024));
    }

    public void ftp() {
        final FTPFileManager fileManager = new FTPFileManager();
        FTPService ftpService = new FTPService();
        ftpService.setHostname("192.168.199.1");
        ftpService.setUsername("lmf");
        ftpService.setPassword("123456");
        fileManager.setFtpService(ftpService);
    }

    public void systemProperty() {

        System.out.println(System.getProperty("java.io.tmpdir"));

        System.out.println(File.pathSeparator);

        System.out.println(File.separator);

        System.out.println(File.pathSeparatorChar);

        System.out.println(File.separatorChar);

    }

    @Test
    public void testGetMimeType() throws Exception {
        LOG.debug(FileUtil.getMimeType(FileUtilTest.class.getResourceAsStream("FileUtilTest.class")));
    }

}
