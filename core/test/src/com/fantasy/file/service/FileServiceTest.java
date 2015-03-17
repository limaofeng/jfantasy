package com.fantasy.file.service;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.bean.FileDetailKey;
import com.fantasy.file.bean.Folder;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.NumberUtil;
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

import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class FileServiceTest {

    private final static Log logger = LogFactory.getLog(FileServiceTest.class);

    @Autowired
    private FileService fileService;
    @Autowired
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
        Assert.assertEquals("替换原生的文件名", this.fileService.get(fileDetailKey).getFileName());
    }

    @Test
    public void testGetFolder() throws Exception {
        Folder folder = this.fileService.getFolder("/", "haolue-upload");
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
        logger.debug("> Dao findPager 方法缓存测试");

        long min, max, total = 0;
        long start = System.currentTimeMillis();
        logger.debug(" 开始第一次查 >> ");
        Pager<FileDetail> pager = this.fileService.findFileDetailPager(new Pager<FileDetail>(15), new ArrayList<PropertyFilter>());
        long _temp = System.currentTimeMillis() - start;
        total += _temp;
        max = min = _temp;
        logger.debug(" 第一次查询耗时：" + _temp + "ms");

        for (int i = 2; i < 250; i++) {
            start = System.currentTimeMillis();
            logger.debug(" 开始第" + NumberUtil.toChinese(i) + "次查 >> ");
            Pager<FileDetail> _pager = this.fileService.findFileDetailPager(new Pager<FileDetail>(15), new ArrayList<PropertyFilter>());

            Assert.assertEquals(pager.getTotalCount(), _pager.getTotalCount());
            Assert.assertEquals(pager.getPageItems().size(), _pager.getPageItems().size());

            _temp = System.currentTimeMillis() - start;
            total += _temp;
            if (_temp >= max) {
                max = _temp;
            }
            if (_temp <= min) {
                min = _temp;
            }
            logger.debug(" 第" + NumberUtil.toChinese(i) + "次查询耗时：" + _temp + "ms");
        }
        logger.debug("查询耗共耗时：" + total + "ms\t平均:" + total / 250 + "ms\t最大:" + max + "ms\t最小:" + min + "ms");
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