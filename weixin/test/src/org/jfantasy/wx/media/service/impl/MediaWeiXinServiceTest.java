package org.jfantasy.wx.media.service.impl;

import org.jfantasy.filestore.bean.FileDetail;
import org.jfantasy.filestore.service.FileUploadService;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.jfantasy.wx.bean.Media;
import org.jfantasy.wx.service.MediaWeiXinService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class MediaWeiXinServiceTest {

    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private MediaWeiXinService wxMediaWeiXinService;

    @After
    public void tearDown() throws Exception {
        Pager<Media> pager= wxMediaWeiXinService.findPager(new Pager<Media>(), new ArrayList<PropertyFilter>());
        List<Long> list=new ArrayList<Long>();
        for(Media media:pager.getPageItems()){
            list.add(media.getId());
        }
        if(list.size()>0){
            Long[] ids=list.toArray(new Long[list.size()]);
            wxMediaWeiXinService.delete(ids);
        }
    }
    @Test
    public void testFindPager() throws Exception {
        File file=new File(MediaWeiXinServiceTest.class.getResource("mm.jpeg").getPath());
        String rename=Long.toString(new Date().getTime())+Integer.toString(new Random().nextInt(900000)+100000)+"."+ WebUtil.getExtension(file.getName());
        FileDetail fileDetail=fileUploadService.upload(file, FileUtil.getMimeType(file), rename, "test");
        Assert.assertNotNull(fileDetail);

        Media media= wxMediaWeiXinService.mediaUpload(fileDetail, "image");
        Assert.assertNotNull(media);

        Pager<Media> pager= wxMediaWeiXinService.findPager(new Pager<Media>(), new ArrayList<PropertyFilter>());
        Assert.assertNotNull(pager.getPageItems());

        Media wxMedia= wxMediaWeiXinService.getMedia(media.getMediaId());
        Assert.assertNotNull(wxMedia);

        FileDetail fileDetail1= wxMediaWeiXinService.mediaDownload(wxMedia.getMediaId(), "test");
        Assert.assertNotNull(fileDetail1);
    }
    @Test
    public void testUploadMediaNews() throws Exception {

    }
}