package com.fantasy.file.service;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.bean.FileDetailKey;
import com.fantasy.file.bean.Folder;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.file.FileUtil;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class FileServiceTest {

    private final static Log logger = LogFactory.getLog(FileServiceTest.class);

    @Resource
    private FileService fileService;
    @Resource
    private FileUploadService fileUploadService;

    //测试数据
    private FileDetailKey fileDetailKey;

    @Before
    public void setUp() throws Exception {
        try {
            File file = new File(FileServiceTest.class.getResource("test.jpg").getFile());
            String mimeType = FileUtil.getMimeType(file);
            FileDetail fileDetail = fileUploadService.upload(file, mimeType, file.getName(), "test");
            this.fileDetailKey = FileDetailKey.newInstance(fileDetail.getAbsolutePath(), fileDetail.getFileManagerId());
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @After
    public void tearDown() throws Exception {
        this.fileService.delete(fileDetailKey);
    }

    @Test
    public void testUpdate() throws Exception {
        FileDetail fileDetail = this.fileService.get(fileDetailKey);
        fileDetail.setFileName("替换原生的文件名");
        this.fileService.update(fileDetail);
        Assert.assertEquals("替换原生的文件名",this.fileService.get(fileDetailKey).getFileName());
    }

    @Test
    public void testGetFolder() throws Exception {
        Folder folder = this.fileService.getFolder("/","haolue-upload");
        Assert.assertNotNull(folder);
    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testCreateFolder() throws Exception {

    }

    @Test
    public void testFindUniqueByMd5() throws Exception {

    }

    @Test
    public void testFindUnique() throws Exception {

    }

    @Test
    public void testGetFileDetail() throws Exception {

    }

    @Test
    public void testFindFileDetailPager() throws Exception {
        this.fileService.findFileDetailPager(new Pager<FileDetail>(15), new ArrayList<PropertyFilter>());
    }

    @Test
    public void testListFolder() throws Exception {
        this.fileService.listFolder("/", fileDetailKey.getFileManagerId(), "absolutePath");
    }

    @Test
    public void testListFileDetail() throws Exception {

    }

    @Test
    public void testGetFileDetailByMd5() throws Exception {

    }

    @Test
    public void testLocalization() throws Exception {

    }

    @Test
    public void testGetDirectory() throws Exception {

    }

    @Test
    public void testGetAbsolutePath() throws Exception {

    }
}