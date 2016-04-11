package org.jfantasy.wx.framework.core;


import org.jfantasy.filestore.FileItem;
import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.message.WeiXinMessage;
import org.jfantasy.wx.framework.message.content.*;
import org.jfantasy.wx.framework.message.content.Article;
import org.jfantasy.wx.framework.message.content.Menu;
import org.jfantasy.wx.framework.message.content.Music;
import org.jfantasy.wx.framework.message.content.News;
import org.jfantasy.wx.framework.message.content.Voice;
import org.jfantasy.wx.framework.message.user.Group;
import org.jfantasy.wx.framework.message.user.OpenIdList;
import org.jfantasy.wx.framework.message.user.User;
import org.jfantasy.wx.framework.oauth2.Scope;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface WeiXinService {

    String getJsapiTicket() throws WeiXinException;

    String getJsapiTicket(boolean forceRefresh) throws WeiXinException;

    Jsapi.Signature createJsapiSignature(String url) throws WeiXinException;

    WeiXinMessage parseInMessage(HttpServletRequest request) throws WeiXinException;

    String parseInMessage(String encryptType, WeiXinMessage message) throws WeiXinException;

    void sendImageMessage(Image content, String... toUsers) throws WeiXinException;

    void sendImageMessage(Image content, long toGroup) throws WeiXinException;

    String mediaUpload(Media.Type mediaType, FileItem fileItem) throws WeiXinException;

    void sendVoiceMessage(Voice content, String... toUsers) throws WeiXinException;

    void sendVoiceMessage(Voice content, long toGroup) throws WeiXinException;

    void sendVideoMessage(Video content, String... toUsers) throws WeiXinException;

    void sendVideoMessage(Video content, long toGroup) throws WeiXinException;

    void sendMusicMessage(Music content, String toUser) throws WeiXinException;

    void sendNewsMessage(List<News> content, String toUser) throws WeiXinException;

    void sendNewsMessage(List<Article> articles, String... toUsers) throws WeiXinException;

    void sendNewsMessage(List<Article> articles, long toGroup) throws WeiXinException;

    void sendTemplateMessage(Template content, String toUser) throws WeiXinException;

    void sendTextMessage(String content, String... toUsers) throws WeiXinException;

    void sendTextMessage(String content, long toGroup) throws WeiXinException;

    String oauth2buildAuthorizationUrl(String redirectUri, Scope scope, String state) throws WeiXinException;

    User getOauth2User(String code) throws WeiXinException;

    List<Group> getGroups() throws WeiXinException;

    Group groupCreate(String groupName) throws WeiXinException;

    void groupUpdate(long groupId, String groupName) throws WeiXinException;

    void userUpdateGroup(String userId, long groupId) throws WeiXinException;

    List<User> getUsers() throws WeiXinException;

    OpenIdList getOpenIds() throws WeiXinException;

    OpenIdList getOpenIds(String nextOpenId) throws WeiXinException;

    Long getGroupIdByUserId(String openId) throws WeiXinException;

    User getUser(String userId) throws WeiXinException;

    FileItem mediaDownload(String mediaId) throws WeiXinException;

    void refreshMenu(Menu... menus) throws WeiXinException;

    Jsapi getJsapi() throws WeiXinException;

    List<Menu> getMenus() throws WeiXinException;

    void clearMenu() throws WeiXinException;

}
