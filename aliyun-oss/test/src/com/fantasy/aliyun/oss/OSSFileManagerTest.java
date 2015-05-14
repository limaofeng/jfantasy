package com.fantasy.aliyun.oss;

import com.fantasy.file.FileItem;
import com.fantasy.file.FileItemFilter;
import com.fantasy.file.FileItemSelector;
import com.fantasy.file.FileManager;
import com.fantasy.file.bean.enums.FileManagerType;
import com.fantasy.file.service.FileManagerFactory;
import com.fantasy.file.service.FileManagerService;
import com.fantasy.framework.util.common.StreamUtil;
import com.fantasy.framework.util.common.file.FileUtil;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class OSSFileManagerTest {

    private final static Log LOG = LogFactory.getLog(OSSFileManagerTest.class);
    @Autowired
    private FileManagerFactory fileManagerFactory;
    @Autowired
    private FileManagerService fileManagerService;

    private FileManager fileManager;

    public static void main(String[] args) {


    }

    @Before
    public void setUp() throws Exception {
        if (fileManagerFactory.getFileManager("oss") == null) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("accessKeyId", "GjYnEEMsLVTomMzF");
            params.put("accessKeySecret", "rYSFhN67iXR0vl0pUSatSQjEqR2e2F");
            params.put("endpoint", "http://oss-cn-hangzhou.aliyuncs.com");
            params.put("bucketName", "static-jfantasy-org");
            this.fileManagerService.save(FileManagerType.oss, "test-oss", "阿里云开发存储", "bucketName=static-jfantasy-org", params);
        }
        this.fileManager = fileManagerFactory.getFileManager("test-oss");
        this.fileManager.writeFile("/test/logo.gif", new File(OSSFileManagerTest.class.getResource("logo.gif").getPath()));
    }

    @After
    public void tearDown() throws Exception {
        this.fileManager.removeFile("/test/");
    }

    @Test
    public void testWriteFile() throws Exception {
        this.fileManager.writeFile("/test/logo_1.gif", new File(OSSFileManagerTest.class.getResource("logo.gif").getPath()));
        FileItem fileItem1 = this.fileManager.getFileItem("/test/logo_1.gif");
        Assert.assertNotNull(fileItem1);

        this.fileManager.writeFile("/test/logo_2.gif", OSSFileManagerTest.class.getResourceAsStream("logo.gif"));
        FileItem fileItem2 = this.fileManager.getFileItem("/test/logo_2.gif");
        Assert.assertNotNull(fileItem2);

    }

    @Test
    public void testReadFile() throws Exception {
        FileItem fileItem = this.fileManager.getFileItem("/test/logo.gif");

        InputStream stream = this.fileManager.readFile("/test/logo.gif");
        LOG.debug(stream.available());
        StreamUtil.closeQuietly(stream);

        File file = FileUtil.tmp();
        this.fileManager.readFile("/test/logo.gif", file.getPath());
        Assert.assertEquals(file.length(), fileItem.getSize());

        file = FileUtil.tmp();
        this.fileManager.readFile("/test/logo.gif", new FileOutputStream(file));
        Assert.assertEquals(file.length(), fileItem.getSize());

    }

    @Test
    public void testRemoveFile() throws Exception {
        Assert.assertNotNull(this.fileManager.getFileItem("/test/logo.gif"));
        this.fileManager.removeFile("/test/logo.gif");
        Assert.assertNull(this.fileManager.getFileItem("/test/logo.gif"));
        this.fileManager.removeFile("/test/");
    }

    @Test
    public void testListFiles() throws Exception {
        List<FileItem> fileItems = fileManager.listFiles();
        LOG.debug(" fileItems size = " + fileItems.size());

        fileItems = fileManager.listFiles("/test/");

        Assert.assertEquals(fileItems.size(), 1);

        fileItems = fileManager.listFiles(new FileItemFilter() {
            @Override
            public boolean accept(FileItem item) {
                return item.isDirectory();
            }
        });

        Assert.assertEquals(fileItems.size(),2);

        fileItems = fileManager.listFiles(new FileItemSelector() {

            @Override
            public boolean includeFile(FileItem fileItem) {
                return fileItem.getAbsolutePath().startsWith("/test/");
            }

            @Override
            public boolean traverseDescendents(FileItem fileItem) {
                return fileItem.getAbsolutePath().equals("/test/");
            }
        });

        Assert.assertEquals(fileItems.size(),2);
    }

    @Test
    public void testGetFileItem() throws Exception {
        FileItem fileItem = fileManager.getFileItem("/aaa/");

        LOG.debug(fileItem.getAbsolutePath());
        Assert.assertEquals(true, fileItem.isDirectory());
        LOG.debug(fileItem.getContentType());

        FileItem parent = fileItem.getParentFileItem();

        LOG.debug(parent.listFileItems());

        LOG.debug(parent);
    }

}