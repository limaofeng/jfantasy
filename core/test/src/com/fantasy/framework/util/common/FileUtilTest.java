package com.fantasy.framework.util.common;

import com.fantasy.file.manager.FTPFileManager;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.framework.service.FTPService;
import com.fantasy.framework.util.common.file.FileUtil;
import eu.medsea.mimeutil.MimeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public static void main(String[] args) {

        // getMagicMatch accepts Files or byte[],
        // which is nice if you want to test streams

        File file = new File("C:/home/website/files/2014-03-07/076e3caed758a1c18c91a0e9cae3368f.pdf");

        System.out.println("==============");
        long start = System.currentTimeMillis();
        Collection<?> mimeTypes = MimeUtil.getMimeTypes(file);
        for (Object o : mimeTypes) {
            System.out.println(o.getClass());
        }
        System.out.println(System.currentTimeMillis() - start);

        final LocalFileManager fileManager = new LocalFileManager();
        fileManager.setDefaultDir("C:/files");

        final List<String> errorFiles = new ArrayList<String>();
        String path = "C:/Users/Administrator/Desktop/发布文件/upload.zip";
        FileUtil.decompress(new File(path), new FileUtil.UnZipCallBack() {

            public void execute(String fileName, InputStream in) {
                try {
                    fileManager.writeFile("/" + fileName, in);
                } catch (IOException e) {
                    errorFiles.add(fileName);
                }
            }
        });
        System.out.println("上传失败的文件：" + errorFiles.size());
        for (String errorFile : errorFiles) {
            System.out.println(errorFile);
        }
    }

    @Test
    public void testGetMimeType() throws Exception {
        LOG.debug(FileUtil.getMimeType(new FileInputStream(new File("/Users/lmf/Pictures/66ea17c8tb596a917fcd9&690.jpeg"))));
    }

}
