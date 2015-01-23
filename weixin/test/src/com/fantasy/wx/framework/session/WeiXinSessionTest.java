package com.fantasy.wx.framework.session;

import com.fantasy.file.FileManager;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.PathUtil;
import com.fantasy.wx.framework.factory.WeiXinSessionFactory;
import com.fantasy.wx.framework.message.content.*;
import com.fantasy.wx.framework.message.user.Group;
import com.fantasy.wx.framework.message.user.User;
import com.fantasy.wx.framework.oauth2.Scope;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class WeiXinSessionTest {

    @Autowired
    private WeiXinSessionFactory factory;

    private WeiXinSession session;

    private final static Log LOG = LogFactory.getLog(WeiXinSessionTest.class);

    private final static String testOpenId = "oJ27YtwbWvKhQ8g3QSzj_Tgmg4uw";

    private final static String[] testOpenIds = {"oJ27Yt1DDz9OlDXL9uwGe9AfG8sI", "oJ27YtwbWvKhQ8g3QSzj_Tgmg4uw"};

    private FileManager fileManager = new LocalFileManager(PathUtil.classes());

    private Group group;

    @Before
    public void setUp() throws Exception {
        session = factory.openSession("wxcbc2c9fb9d585cd3");
        group = ObjectUtil.find(session.getGroups(), "getName()", "员工");
    }

    @Test
    public void testSendImageMessage() throws Exception {
        session.sendImageMessage(new Image(fileManager.getFileItem("/files/gravatar.jpg")), testOpenId);

        session.sendImageMessage(new Image(fileManager.getFileItem("/files/gravatar.jpg")), testOpenIds);

        session.sendImageMessage(new Image(fileManager.getFileItem("/files/gravatar.jpg")), group.getId());
        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
    }

    @Test
    public void testSendVoiceMessage() throws Exception {
        session.sendVoiceMessage(new Voice(fileManager.getFileItem("/files/test.amr")), testOpenId);

        session.sendVoiceMessage(new Voice(fileManager.getFileItem("/files/test.amr")), testOpenIds);

        session.sendVoiceMessage(new Voice(fileManager.getFileItem("/files/test.amr")), group.getId());
        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
    }

    @Test
    public void testSendVideoMessage() throws Exception {
        session.sendVideoMessage(new Video("昊略测试标题", "昊略测试视频摘要", fileManager.getFileItem("/files/video.mp4"), fileManager.getFileItem("/files/gravatar.jpg")), testOpenId);

        session.sendVideoMessage(new Video("昊略测试标题", "昊略测试视频摘要", fileManager.getFileItem("/files/video.mp4")), testOpenIds);

        session.sendVideoMessage(new Video("昊略测试标题", "昊略测试视频摘要", fileManager.getFileItem("/files/video.mp4")), group.getId());

        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
    }

    @Test
    public void testSendMusicMessage() throws Exception {
        session.sendMusicMessage(new Music("战地3主题器", "战地游戏3主题曲。。。。。", "http://test.jfantasy.org/static/mp3/test.mp3", "http://test.jfantasy.org/static/mp3/test.mp3", fileManager.getFileItem("/files/gravatar.jpg")), testOpenId);
        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
    }

    @Test
    public void testSendNewsMessage() throws Exception {
        List<News> newses = new ArrayList<News>();
        newses.add(new News("http://test.jfantasy.org/assets/images/gravatar.jpg", new Link("测试标题", "测试描述", "http://test.jfantasy.org/index.do")));
        session.sendNewsMessage(newses, testOpenId);

        //测试群发
        List<Article> articles = new ArrayList<Article>();
        articles.add(new Article("测试标题群发", "测试内容群发", fileManager.getFileItem("/files/gravatar.jpg")));
        session.sendNewsMessage(articles, testOpenIds);

        session.sendNewsMessage(articles, group.getId());
        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
    }

    @Test
    public void testSendTextMessage() throws Exception {
        session.sendTextMessage("测试消息", testOpenId);

        session.sendTextMessage("测试消息群发消息", testOpenIds);

        session.sendTextMessage("测试消息群发消息", group.getId());
        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
    }

    @Test
    public void testGetAuthorizationUrl() throws Exception {
        LOG.debug(session.getAuthorizationUrl("http://test.jfantasy.org", Scope.base));

        LOG.debug(session.getAuthorizationUrl("http://test.jfantasy.org", Scope.userinfo));

        LOG.debug(session.getAuthorizationUrl("http://test.jfantasy.org", Scope.base, "1"));

        LOG.debug(session.getAuthorizationUrl("http://test.jfantasy.org", Scope.userinfo, "1"));
    }

    @Test
    public void testGetUsers() throws Exception {

        List<User> users = session.getUsers();
        LOG.debug("users size : " + users.size());
        for (User user : users) {
            LOG.debug(user);
        }
    }

    @Test
    public void testRefreshMenu() throws Exception {
        List<Menu> menus = session.getMenus();
        session.refreshMenu(menus.toArray(new Menu[menus.size()]));
    }

    @Test
    public void testGetMenus() throws Exception {
        List<Menu> menus = session.getMenus();
        for(Menu menu : menus){
            LOG.debug(menu);
        }
    }

    @Test
    public void testClearMenu() throws Exception {
        List<Menu> menus = session.getMenus();
        for(Menu menu : menus){
            LOG.debug(menu);
        }
        session.clearMenu();
        this.session.refreshMenu(menus.toArray(new Menu[menus.size()]));
    }
}