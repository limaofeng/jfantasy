package com.fantasy.cms.web;

import com.fantasy.cms.bean.Banner;
import com.fantasy.cms.service.BannerService;
import com.fantasy.cms.service.BannerServiceTest;
import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FileUploadService;
import com.fantasy.framework.struts2.context.ActionConstants;
import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.security.SpringSecurityUtils;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsSpringJUnit4TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class BannerActionTest extends StrutsSpringJUnit4TestCase {

    private final static Log LOG = LogFactory.getLog(BannerActionTest.class);

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private FileUploadService fileUploadService;
    @Resource
    private BannerService bannerService;


    //加载struts文件
    @Override
    protected String getConfigPath() {
        return "struts.xml";
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //默认登陆用户
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");
        SpringSecurityUtils.saveUserDetailsToContext(userDetails, request);
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        Banner banner = bannerService.findUniqueByKey("test");
        if(banner!=null) {
            bannerService.delete(banner.getId());
        }
    }

    @Test
    public void testIndex() throws Exception {
        //添加查询条件
        this.request.addParameter("LIKES_name", "test");

        ActionProxy proxy = super.getActionProxy("/cms/banner/index.do");
        Assert.assertNotNull(proxy);
        proxy.setExecuteResult(false);

        String result = proxy.execute();
        Assert.assertEquals(Action.SUCCESS, result);
    }

    @Test
    public void testSearch() throws Exception {
        this.testSave();
        this.request.removeAllParameters();
        //添加查询条件
        this.request.addParameter("LIKES_name", "测试轮播图");

        ActionProxy proxy = super.getActionProxy("/cms/banner/search.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();
        Assert.assertEquals(ActionConstants.JSONDATA, result);
    }

    @Test
    public void testSave() throws Exception {
        this.request.addParameter("key", "test");
        this.request.addParameter("name", "测试轮播图");
        this.request.addParameter("size", "100x100");
        this.request.addParameter("description", "test");
        this.request.addParameter("bannerItems[0].title", "测试图片");
        this.request.addParameter("bannerItems[0].summary", "测试摘要");
        this.request.addParameter("bannerItems[0].url", "http://jira.jfantasy.org");

        try {
            File file = new File(BannerServiceTest.class.getResource("Banner_2.png").getFile());
            String mimeType = FileUtil.getMimeType(file);
            FileDetail fileDetail = fileUploadService.upload(file, mimeType, file.getName(), "test");
            this.request.addParameter("bannerItems[0].bannerImageStore", fileDetail.getFileManagerId() + ":" + fileDetail.getAbsolutePath());
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage(), e);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        ActionProxy proxy = super.getActionProxy("/cms/banner/save.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();
        Assert.assertEquals(ActionConstants.JSONDATA, result);
        Assert.assertNotNull(bannerService.findUniqueByKey("test"));
    }

    @Test
    public void testEdit() throws Exception {
        this.testSave();
        this.request.removeAllParameters();

        Banner banner = bannerService.findUniqueByKey("test");
        this.request.addParameter("id", banner.getId().toString());

        ActionProxy proxy = super.getActionProxy("/cms/banner/edit.do");
        Assert.assertNotNull(proxy);
        proxy.setExecuteResult(false);

        String result = proxy.execute();
        Assert.assertEquals(Action.SUCCESS, result);
    }

    @Test
    public void testDelete() throws Exception {
        this.testSave();
        this.request.removeAllParameters();

        Banner banner = bannerService.findUniqueByKey("test");
        this.request.addParameter("ids", banner.getId().toString());

        ActionProxy proxy = super.getActionProxy("/cms/banner/delete.do");
        Assert.assertNotNull(proxy);

        String result = proxy.execute();
        Assert.assertEquals(ActionConstants.JSONDATA, result);
        Assert.assertNull(bannerService.findUniqueByKey("test"));
    }
}