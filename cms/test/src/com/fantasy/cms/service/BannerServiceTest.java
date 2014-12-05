package com.fantasy.cms.service;

import com.fantasy.cms.bean.Banner;
import com.fantasy.cms.bean.BannerItem;
import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FileUploadService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.framework.util.jackson.JSON;
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
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class BannerServiceTest {

    private static Log logger = LogFactory.getLog(BannerServiceTest.class);

    @Resource
    private BannerService bannerService;
    @Resource
    private FileUploadService fileUploadService;

    @Before
    public void setUp() throws Exception {
        this.testSave();
    }

    @After
    public void tearDown() throws Exception {
        this.testDelete();
    }

    @Test
    public void testFindPager() throws Exception {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_key","JUNIT_TEST"));
        Pager<Banner> pager = this.bannerService.findPager(new Pager<Banner>(),filters);

        logger.debug(pager);

        Assert.assertEquals(1,pager.getTotalCount());

    }

    public void testSave() throws Exception {
        Banner banner = this.bannerService.findUniqueByKey("JUNIT_TEST");
        if (banner != null) {
            this.testDelete();
        }
        banner = new Banner();
        banner.setKey("JUNIT_TEST");
        banner.setName("测试 banner");
        banner.setDescription("");

        List<BannerItem> bannerItems = new ArrayList<BannerItem>();

        BannerItem item1 = new BannerItem();
        item1.setTitle("测试标题-1");
        item1.setSummary("测试摘要-1");
        item1.setUrl("http://test.jfantasy.org/static/banner/1.html");
        try {
            File file = new File(BannerServiceTest.class.getResource("banner_1.jpg").getFile());
            String mimeType = FileUtil.getMimeType(file);
            FileDetail fileDetail = fileUploadService.upload(file, mimeType, file.getName(), "test");
            item1.setBannerImageStore(JSON.serialize(new FileDetail[]{fileDetail}));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        bannerItems.add(item1);

        BannerItem item2 = new BannerItem();
        item2.setTitle("测试标题-2");
        item2.setSummary("测试摘要-2");
        item2.setUrl("http://test.jfantasy.org/static/banner/2.html");
        try {
            File file = new File(BannerServiceTest.class.getResource("banner_2.png").getFile());
            String mimeType = FileUtil.getMimeType(file);
            FileDetail fileDetail = fileUploadService.upload(file, mimeType, file.getName(), "test");
            item2.setBannerImageStore(JSON.serialize(new FileDetail[]{fileDetail}));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        bannerItems.add(item2);

        banner.setBannerItems(bannerItems);

        bannerService.save(banner);

        logger.debug(banner);

        Assert.assertNotNull(banner.getId());

        Assert.assertEquals(2, banner.getBannerItems().size());

    }

    @Test
    public void testGet() throws Exception {
        Banner banner = this.bannerService.findUniqueByKey("JUNIT_TEST");

        Assert.assertNotNull(banner);

        banner = this.bannerService.get(banner.getId());

        Assert.assertNotNull(banner);

        logger.debug(banner.getBannerItems());

        Assert.assertNotNull(banner.getBannerItems().get(0).getBannerImage().getAbsolutePath());

        Assert.assertNotNull(banner.getBannerItems().get(1).getBannerImage().getAbsolutePath());
    }

    public void testDelete() throws Exception {
        Banner banner = this.bannerService.findUniqueByKey("JUNIT_TEST");

        Assert.assertNotNull(banner);

        this.bannerService.delete(banner.getId());
    }

    @Test
    public void testBannerKeyUnique() throws Exception {
        Banner banner = this.bannerService.findUniqueByKey("JUNIT_TEST");

        Assert.assertTrue(this.bannerService.bannerKeyUnique(banner.getKey(),banner.getId()));

        Assert.assertTrue(this.bannerService.bannerKeyUnique("TEST-XXXX",banner.getId()));

        Assert.assertFalse(this.bannerService.bannerKeyUnique(banner.getKey(), 0l));
    }

}