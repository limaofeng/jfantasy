package com.fantasy.wx.service;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FileUploadService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.wx.bean.GroupMessage;
import com.fantasy.wx.bean.GroupNews;
import com.fantasy.wx.bean.GroupNewsArticle;
import com.fantasy.wx.media.service.impl.MediaWeiXinServiceTest;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class GroupMessageWeiXinServiceTest {
    @Autowired
    private GroupMessageWeiXinService groupMessageWeiXinService;
    @Autowired
    private FileUploadService fileUploadService;

    private static final Log logger = LogFactory.getLog(GroupMessage.class);

    @Before
    public void setUp() throws Exception {
        testSave();
    }

    @After
    public void tearDown() throws Exception {
        testDelete();
    }

    @Test
    public void testFindPager() throws Exception {
        Pager<GroupMessage> p = new Pager<GroupMessage>();
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("EQS_content", "test@"));
        Pager<GroupMessage> pager = groupMessageWeiXinService.findPager(p, list);
        Assert.assertNotNull(pager.getPageItems());
        logger.debug(JSON.serialize(pager));
    }

    public void testDelete() throws Exception {
        Pager<GroupMessage> pager = groupMessageWeiXinService.findPager(new Pager<GroupMessage>(), new ArrayList<PropertyFilter>());
        groupMessageWeiXinService.delete(pager.getPageItems().get(0).getId());
    }

    @Test
    public void testGetOutMessage() throws Exception {
        Pager<GroupMessage> pager = groupMessageWeiXinService.findPager(new Pager<GroupMessage>(), new ArrayList<PropertyFilter>());
        GroupMessage o = groupMessageWeiXinService.getGroupMessage(pager.getPageItems().get(0).getId());
        Assert.assertNotNull(o);
        logger.debug(JSON.serialize(o));
    }

    public void testSave() throws Exception {
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setContent("test@");
        groupMessage.setMsgType("text");

        groupMessage.setToUsers(createOpenId());
        groupMessageWeiXinService.save(groupMessage);
    }

    public List<String> createOpenId() {
        List<String> list = new ArrayList<String>();
//        WeixinConfigInit.WxXmlMpInMemoryConfigStorage configProvider = (WeixinConfigInit.WxXmlMpInMemoryConfigStorage) weixinConfigInit.getWxMpConfigStorage();
//        list.add(configProvider.getOpenId());
        return list;
    }

    @Test
    public void testSendGroupMessage() throws Exception {
        groupMessageWeiXinService.sendTextGroupMessage(1L, "福建省快乐group");
    }

    @Test
    public void testSendOpenIdMessage() throws Exception {
        groupMessageWeiXinService.sendTextOpenIdMessage(createOpenId(), "福建省快乐openId");
    }
    @Test
    public void testSendNewsOpenIdMessage() throws Exception {
        //生成缩略图FileDetail对象
        File file=new File(MediaWeiXinServiceTest.class.getResource("mm.jpeg").getPath());
        String rename=Long.toString(new Date().getTime())+Integer.toString(new Random().nextInt(900000)+100000)+"."+ WebUtil.getExtension(file.getName());
        FileDetail fileDetail=fileUploadService.upload(file, FileUtil.getMimeType(file), rename, "test");
        Assert.assertNotNull(fileDetail);

        //第二个article
        GroupNews news=new GroupNews();
        GroupNewsArticle article=new GroupNewsArticle();
        article.setThumbFile(fileDetail);
        article.setContent("<img src=\"http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png\" width=\"270\" height=\"129\">我是内容<h1>我比别人大哈哈哈哈</h1>");
        article.setAuthor("作者是我");
        article.setContentSourceUrl("http://www.baidu.com/?id=123");
        article.setDigest("描个述");
        article.setTitle("我是个标题");
        news.addArticle(article);
        //第二个article
        article=new GroupNewsArticle();
        article.setThumbFile(fileDetail);
        article.setContent("<img src=\"http://img3.douban.com/f/fm/6b922536970a3676b4c0b1d464bac01d5573f22f/pics/fm/home/fm_logoV1.png\" >我是内容1<h1>我比别人大哈哈哈哈1</h1>");
        article.setAuthor("作者是我1");
        article.setContentSourceUrl("http://www.baidu.com/?id=456");
        article.setDigest("描个述1");
        article.setTitle("我是个标题1");
        article.setShowCoverPic(false);
        news.addArticle(article);

        //第三个article
        article=new GroupNewsArticle();
        article.setContent("<img src=\"http://img3.douban.com/f/fm/6b922536970a3676b4c0b1d464bac01d5573f22f/pics/fm/home/fm_logoV1.png\" >我是内容1<h1>我比别人大哈哈哈哈1</h1>");
        article.setAuthor("作者是我1");
        article.setContentSourceUrl("http://www.baidu.com/?id=456");
        article.setDigest("描个述1");
        article.setTitle("我是个标题1");
        news.addArticle(article);
        //第四个article
        article=new GroupNewsArticle();
        article.setThumbFile(fileDetail);
        article.setContent("<img src=\"http://img3.douban.com/f/fm/6b922536970a3676b4c0b1d464bac01d5573f22f/pics/fm/home/fm_logoV1.png\" >我是内容1<h1>我比别人大哈哈哈哈1</h1>");
        article.setAuthor("作者是我1");
        article.setContentSourceUrl("http://www.baidu.com/?id=456");
        article.setDigest("描个述1");
        article.setTitle("我是个标题1");
        news.addArticle(article);

        groupMessageWeiXinService.sendNewsOpenIdMessage(new ArrayList<String>(){
            {
                add("");
            }

        },news);
        //groupMessageService.sendNewsGroupMessage()
    }
}