package org.jfantasy.wx.framework.core;

import org.jfantasy.file.FileItem;
import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.message.WeiXinMessage;
import org.jfantasy.wx.framework.message.content.*;
import org.jfantasy.wx.framework.message.user.Group;
import org.jfantasy.wx.framework.message.user.OpenIdList;
import org.jfantasy.wx.framework.message.user.User;
import org.jfantasy.wx.framework.oauth2.Scope;
import org.jfantasy.wx.framework.session.AccountDetails;
import org.jfantasy.wx.framework.session.WeiXinSession;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信服务号与订阅号
 */
@Component
public class MpCoreHelper implements WeiXinCoreHelper {

    private Map<String, WeiXinDetails> weiXinDetailsMap = new HashMap<String, WeiXinDetails>();

    @Override
    public void register(AccountDetails accountDetails) {
        if (weiXinDetailsMap.containsKey(accountDetails.getAppId())) {
            weiXinDetailsMap.remove(accountDetails.getAppId());
        }
        weiXinDetailsMap.put(accountDetails.getAppId(), new WeiXinDetails(accountDetails));
    }

    @Override
    public WeiXinMessage parseInMessage(WeiXinSession session, HttpServletRequest request) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().parseInMessage(request);
    }

    @Override
    public String buildOutMessage(WeiXinSession session, String encryptType, WeiXinMessage message) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().parseInMessage(encryptType, message);
    }

    @Override
    public void sendImageMessage(WeiXinSession session, Image content, String... toUsers) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().sendImageMessage(content, toUsers);
    }

    @Override
    public void sendImageMessage(WeiXinSession session, Image content, long toGroup) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().sendImageMessage(content, toGroup);
    }

    @Override
    public void sendVoiceMessage(WeiXinSession session, Voice content, String... toUsers) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().sendVoiceMessage(content, toUsers);
    }

    @Override
    public void sendVoiceMessage(WeiXinSession session, Voice content, long toGroup) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().sendVoiceMessage(content, toGroup);
    }

    @Override
    public void sendVideoMessage(WeiXinSession session, Video content, String... toUsers) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().sendVideoMessage(content, toUsers);
    }

    @Override
    public void sendVideoMessage(WeiXinSession session, Video content, long toGroup) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().sendVideoMessage(content, toGroup);
    }

    @Override
    public void sendMusicMessage(WeiXinSession session, Music content, String toUser) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().sendMusicMessage(content, toUser);
    }

    @Override
    public void sendNewsMessage(WeiXinSession session, List<News> content, String toUser) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().sendNewsMessage(content, toUser);
    }

    public void sendNewsMessage(WeiXinSession session, List<Article> articles, String... toUsers) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().sendNewsMessage(articles, toUsers);
    }

    @Override
    public void sendNewsMessage(WeiXinSession session, List<Article> articles, long toGroup) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().sendNewsMessage(articles, toGroup);
    }

    @Override
    public String oauth2buildAuthorizationUrl(WeiXinSession session, String redirectUri, Scope scope, String state) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().oauth2buildAuthorizationUrl(redirectUri, scope, state);
    }

    public User getOauth2User(WeiXinSession session, String code) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().getOauth2User(code);
    }

    @Override
    public void sendTextMessage(WeiXinSession session, String content, String... toUsers) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().sendTextMessage(content, toUsers);
    }

    @Override
    public void sendTextMessage(WeiXinSession session, String content, long toGroup) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().sendTextMessage(content, toGroup);
    }

    @Override
    public void sendTemplateMessage(WeiXinSession session, Template content, String toUser) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().sendTemplateMessage(content, toUser);
    }

    @Override
    public List<Group> getGroups(WeiXinSession session) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().getGroups();
    }

    @Override
    public Group groupCreate(WeiXinSession session, String groupName) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().groupCreate(groupName);
    }

    @Override
    public void groupUpdate(WeiXinSession session, long groupId, String groupName) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().groupUpdate(groupId, groupName);
    }

    @Override
    public void userUpdateGroup(WeiXinSession session, String userId, long groupId) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().userUpdateGroup(userId, groupId);
    }

    @Override
    public List<User> getUsers(WeiXinSession session) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().getUsers();
    }

    @Override
    public OpenIdList getOpenIds(WeiXinSession session) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().getOpenIds();
    }

    @Override
    public OpenIdList getOpenIds(WeiXinSession session, String nextOpenId) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().getOpenIds(nextOpenId);
    }

    @Override
    public Long getGroupIdByUserId(WeiXinSession session, String openId) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().getGroupIdByUserId(openId);
    }

    @Override
    public User getUser(WeiXinSession session, String userId) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().getUser(userId);
    }

    @Override
    public String mediaUpload(WeiXinSession session, Media.Type mediaType, FileItem fileItem) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().mediaUpload(mediaType, fileItem);
    }

    public FileItem mediaDownload(WeiXinSession session, String mediaId) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().mediaDownload(mediaId);
    }

    @Override
    public void refreshMenu(WeiXinSession session, Menu... menus) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().refreshMenu(menus);
    }

    public Jsapi getJsapi(WeiXinSession session) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().getJsapi();
    }

    @Override
    public List<Menu> getMenus(WeiXinSession session) throws WeiXinException {
        return getWeiXinDetails(session.getId()).getWeiXinService().getMenus();
    }

    @Override
    public void clearMenu(WeiXinSession session) throws WeiXinException {
        getWeiXinDetails(session.getId()).getWeiXinService().clearMenu();
    }

    private WeiXinDetails getWeiXinDetails(String appid) throws WeiXinException {
        if (!weiXinDetailsMap.containsKey(appid)) {
            throw new WeiXinException("[appid=" + appid + "]未注册！");
        }
        return weiXinDetailsMap.get(appid);
    }

}
