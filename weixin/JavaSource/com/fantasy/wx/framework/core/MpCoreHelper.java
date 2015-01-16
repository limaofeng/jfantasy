package com.fantasy.wx.framework.core;

import com.fantasy.file.FileItem;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.security.bean.enums.Sex;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.message.MessageFactory;
import com.fantasy.wx.framework.message.TextMessage;
import com.fantasy.wx.framework.message.WeiXinMessage;
import com.fantasy.wx.framework.message.content.*;
import com.fantasy.wx.framework.message.user.Group;
import com.fantasy.wx.framework.message.user.OpenIdList;
import com.fantasy.wx.framework.message.user.User;
import com.fantasy.wx.framework.oauth2.AccessToken;
import com.fantasy.wx.framework.oauth2.Scope;
import com.fantasy.wx.framework.session.AccountDetails;
import com.fantasy.wx.framework.session.WeiXinSession;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.http.URIUtil;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.*;
import me.chanjar.weixin.mp.bean.custombuilder.MusicBuilder;
import me.chanjar.weixin.mp.bean.custombuilder.VideoBuilder;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 微信服务号与订阅号
 */
@Component
public class MpCoreHelper implements WeiXinCoreHelper {

    private final static Log LOG = LogFactory.getLog(MpCoreHelper.class);

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
        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String encrypt_type = request.getParameter("encrypt_type");
        String msg_signature = request.getParameter("msg_signature");
        InputStream input;
        try {
            input = request.getInputStream();
        } catch (IOException e) {
            throw new WeiXinException(e.getMessage(), e);
        }

        WeiXinDetails weiXinDetails = getWeiXinDetails(session.getId());

        if (!weiXinDetails.getWxMpService().checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            throw new WeiXinException("非法请求");
        }

        String encryptType = StringUtils.isBlank(encrypt_type) ? "raw" : encrypt_type;
        WxMpXmlMessage inMessage;
        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            inMessage = WxMpXmlMessage.fromXml(input);
        } else if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            inMessage = WxMpXmlMessage.fromEncryptedXml(input, weiXinDetails.getWxMpConfigStorage(), timestamp, nonce, msg_signature);
        } else {
            throw new WeiXinException("不可识别的加密类型");
        }
        LOG.debug("inMessage=>" + JSON.serialize(inMessage));
        if ("text".equals(inMessage.getMsgType())) {
            return MessageFactory.createTextMessage(inMessage.getMsgId(), inMessage.getFromUserName(), new Date(inMessage.getCreateTime()), inMessage.getContent());
        } else if ("image".equalsIgnoreCase(inMessage.getMsgType())) {
            return MessageFactory.createImageMessage(this, inMessage.getMsgId(), inMessage.getFromUserName(), new Date(inMessage.getCreateTime()), inMessage.getMediaId(), inMessage.getUrl());
        } else if ("voice".equalsIgnoreCase(inMessage.getMsgType())) {
            return MessageFactory.createVoiceMessage(inMessage.getMsgId(), inMessage.getFromUserName(), new Date(inMessage.getCreateTime()), inMessage.getMediaId(), inMessage.getFormat(), inMessage.getRecognition());
        } else if ("video".equalsIgnoreCase(inMessage.getMsgType())) {
            return MessageFactory.createVideoMessage(inMessage.getMsgId(), inMessage.getFromUserName(), new Date(inMessage.getCreateTime()), inMessage.getMediaId(), inMessage.getThumbMediaId());
        } else if ("location".equalsIgnoreCase(inMessage.getMsgType())) {
            return MessageFactory.createLocationMessage(inMessage.getMsgId(), inMessage.getFromUserName(), new Date(inMessage.getCreateTime()), inMessage.getLocationX(), inMessage.getLocationY(), inMessage.getScale(), inMessage.getLabel());
        } else if ("link".equalsIgnoreCase(inMessage.getMsgType())) {
            return MessageFactory.createLinkMessage(inMessage.getMsgId(), inMessage.getFromUserName(), new Date(inMessage.getCreateTime()), inMessage.getTitle(), inMessage.getDescription(), inMessage.getUrl());
        } else if ("event".equalsIgnoreCase(inMessage.getMsgType())) {
            return MessageFactory.createEventMessage(inMessage.getMsgId(), inMessage.getFromUserName(), new Date(inMessage.getCreateTime()), inMessage.getEvent(), inMessage.getEventKey(), inMessage.getTicket(), inMessage.getLatitude(), inMessage.getLongitude(), inMessage.getPrecision());
        } else {
            LOG.debug(inMessage);
            throw new WeiXinException("无法处理的消息类型" + inMessage.getMsgType());
        }
    }

    @Override
    public String buildOutMessage(WeiXinSession session, String encryptType, WeiXinMessage message) throws WeiXinException {
        WeiXinDetails weiXinDetails = getWeiXinDetails(session.getId());
        WxMpXmlOutMessage outMessage;
        if (message instanceof TextMessage) {
            outMessage = WxMpXmlOutMessage.TEXT()
                    .content(((TextMessage) message).getContent())
                    .fromUser(message.getFromUserName())
                    .toUser(message.getToUserName())
                    .build();
        } else {
            throw new WeiXinException("不支持的消息类型");
        }
        if ("raw".equals(encryptType)) {
            return outMessage.toXml();
        } else if ("aes".equals(encryptType)) {
            return outMessage.toEncryptedXml(weiXinDetails.getWxMpConfigStorage());
        } else {
            throw new WeiXinException("不可识别的加密类型");
        }
    }

    @Override
    public void sendImageMessage(WeiXinSession session, Image content, String... toUsers) throws WeiXinException {
        try {
            if (toUsers.length == 0) {
                this.sendImageMessage(session, content, -1);
                return;
            }
            //上传图片文件
            Media media = content.getMedia();
            media.setId(this.mediaUpload(session, media.getType(), media.getFileItem()));
            if (toUsers.length == 1) {
                getWeiXinDetails(session.getId()).getWxMpService().customMessageSend(WxMpCustomMessage.IMAGE().toUser(toUsers[0]).mediaId(media.getId()).build());
            } else {
                WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
                openIdsMessage.setMsgType(WxConsts.MASS_MSG_IMAGE);
                for (String toUser : toUsers) {
                    openIdsMessage.addUser(toUser);
                }
                openIdsMessage.setMediaId(media.getId());
                getWeiXinDetails(session.getId()).getWxMpService().massOpenIdsMessageSend(openIdsMessage);
            }
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendImageMessage(WeiXinSession session, Image content, long toGroup) throws WeiXinException {
        try {
            //上传图片文件
            Media media = content.getMedia();
            media.setId(this.mediaUpload(session, media.getType(), media.getFileItem()));
            WxMpMassGroupMessage groupMessage = new WxMpMassGroupMessage();
            groupMessage.setMsgtype(WxConsts.MASS_MSG_IMAGE);
            if (toGroup != -1) {
                groupMessage.setGroupId(toGroup);
            }
            groupMessage.setMediaId(media.getId());
            getWeiXinDetails(session.getId()).getWxMpService().massGroupMessageSend(groupMessage);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendVoiceMessage(WeiXinSession session, Voice content, String... toUsers) throws WeiXinException {
        try {
            if (toUsers.length == 0) {
                this.sendVoiceMessage(session, content, -1);
                return;
            }
            //上传语言文件
            Media media = content.getMedia();
            media.setId(this.mediaUpload(session, media.getType(), media.getFileItem()));
            if (toUsers.length == 1) {
                getWeiXinDetails(session.getId()).getWxMpService().customMessageSend(WxMpCustomMessage.VOICE().toUser(toUsers[0]).mediaId(media.getId()).build());
            } else {
                WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
                openIdsMessage.setMsgType(WxConsts.MASS_MSG_VOICE);
                for (String toUser : toUsers) {
                    openIdsMessage.addUser(toUser);
                }
                openIdsMessage.setMediaId(media.getId());
                getWeiXinDetails(session.getId()).getWxMpService().massOpenIdsMessageSend(openIdsMessage);
            }
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendVoiceMessage(WeiXinSession session, Voice content, long toGroup) throws WeiXinException {
        try {
            //上传语言文件
            Media media = content.getMedia();
            media.setId(this.mediaUpload(session, media.getType(), media.getFileItem()));
            WxMpMassGroupMessage groupMessage = new WxMpMassGroupMessage();
            groupMessage.setMsgtype(WxConsts.MASS_MSG_VOICE);
            if (toGroup != -1) {
                groupMessage.setGroupId(toGroup);
            }
            groupMessage.setMediaId(media.getId());
            getWeiXinDetails(session.getId()).getWxMpService().massGroupMessageSend(groupMessage);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendVideoMessage(WeiXinSession session, Video content, String... toUsers) throws WeiXinException {
        try {
            if (toUsers.length == 0) {
                this.sendVideoMessage(session, content, -1);
                return;
            }
            //上传视频
            Media media = content.getMedia();
            media.setId(this.mediaUpload(session, media.getType(), media.getFileItem()));
            //发送消息
            if (toUsers.length == 1) {
                //上传缩略图
                Media thumb = content.getThumb();
                thumb.setId(this.mediaUpload(session, thumb.getType(), thumb.getFileItem()));
                VideoBuilder videoBuilder = WxMpCustomMessage.VIDEO().toUser(toUsers[0]).mediaId(media.getId()).thumbMediaId(thumb.getId());
                if (StringUtil.isNotBlank(content.getTitle())) {
                    videoBuilder.title(content.getTitle());
                }
                if (StringUtil.isNotBlank(content.getDescription())) {
                    videoBuilder.description(content.getDescription());
                }
                getWeiXinDetails(session.getId()).getWxMpService().customMessageSend(videoBuilder.build());
            } else {
                WxMpMassVideo massVideo = new WxMpMassVideo();
                massVideo.setMediaId(media.getId());
                massVideo.setTitle(content.getTitle());
                massVideo.setDescription(content.getDescription());
                WxMpMassUploadResult result = getWeiXinDetails(session.getId()).getWxMpService().massVideoUpload(massVideo);
                WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
                openIdsMessage.setMsgType(WxConsts.MASS_MSG_VIDEO);
                for (String toUser : toUsers) {
                    openIdsMessage.addUser(toUser);
                }
                openIdsMessage.setMediaId(result.getMediaId());
                getWeiXinDetails(session.getId()).getWxMpService().massOpenIdsMessageSend(openIdsMessage);
            }
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendVideoMessage(WeiXinSession session, Video content, long toGroup) throws WeiXinException {
        try {
            //上传视频
            Media media = content.getMedia();
            media.setId(this.mediaUpload(session, media.getType(), media.getFileItem()));
            //发送消息
            WxMpMassVideo massVideo = new WxMpMassVideo();
            massVideo.setMediaId(media.getId());
            massVideo.setTitle(content.getTitle());
            massVideo.setDescription(content.getDescription());
            WxMpMassUploadResult result = getWeiXinDetails(session.getId()).getWxMpService().massVideoUpload(massVideo);
            WxMpMassGroupMessage groupMessage = new WxMpMassGroupMessage();
            groupMessage.setMsgtype(WxConsts.MASS_MSG_VIDEO);
            if (toGroup != -1) {
                groupMessage.setGroupId(toGroup);
            }
            groupMessage.setMediaId(result.getMediaId());
            getWeiXinDetails(session.getId()).getWxMpService().massGroupMessageSend(groupMessage);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendMusicMessage(WeiXinSession session, Music content, String toUser) throws WeiXinException {
        try {
            //上传缩略图
            Media thumb = content.getThumb();
            thumb.setId(this.mediaUpload(session, thumb.getType(), thumb.getFileItem()));
            //发送消息
            MusicBuilder musicBuilder = WxMpCustomMessage.MUSIC().toUser(toUser).musicUrl(content.getUrl()).hqMusicUrl(content.getHqUrl()).thumbMediaId(thumb.getId());
            if (StringUtil.isNotBlank(content.getTitle())) {
                musicBuilder.title(content.getTitle());
            }
            if (StringUtil.isNotBlank(content.getDescription())) {
                musicBuilder.description(content.getDescription());
            }
            getWeiXinDetails(session.getId()).getWxMpService().customMessageSend(musicBuilder.build());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendNewsMessage(WeiXinSession session, News content, String toUser) throws WeiXinException {
        try {
            WxMpCustomMessage.WxArticle article = new WxMpCustomMessage.WxArticle();
            article.setPicUrl(content.getPicUrl());
            article.setTitle(content.getLink().getTitle());
            article.setDescription(content.getLink().getDescription());
            article.setUrl(content.getLink().getUrl());
            getWeiXinDetails(session.getId()).getWxMpService().customMessageSend(WxMpCustomMessage.NEWS().toUser(toUser).addArticle(article).build());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    public void sendNewsMessage(WeiXinSession session, List<Article> articles, String... toUsers) throws WeiXinException {
        if (toUsers.length == 0) {
            this.sendNewsMessage(session, articles, -1);
            return;
        }
        try {
            WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
            openIdsMessage.setMsgType(WxConsts.MASS_MSG_NEWS);
            for (String toUser : toUsers) {
                openIdsMessage.addUser(toUser);
            }
            WxMpMassNews massNews = new WxMpMassNews();
            for (Article article : articles) {
                WxMpMassNews.WxMpMassNewsArticle newsArticle = new WxMpMassNews.WxMpMassNewsArticle();
                article.getThumb().setId(this.mediaUpload(session, article.getThumb().getType(), article.getThumb().getFileItem()));
                newsArticle.setThumbMediaId(article.getThumb().getId());
                newsArticle.setTitle(article.getTitle());
                newsArticle.setContent(article.getContent());
                newsArticle.setShowCoverPic(article.isShowCoverPic());
                if (StringUtil.isNotBlank(article.getAuthor())) {
                    newsArticle.setAuthor(newsArticle.getAuthor());
                }
                if (StringUtil.isNotBlank(article.getContentSourceUrl())) {
                    newsArticle.setContentSourceUrl(newsArticle.getContentSourceUrl());
                }
                if (StringUtil.isNotBlank(article.getDigest())) {
                    newsArticle.setDigest(article.getDigest());
                }
                massNews.addArticle(newsArticle);
            }
            WxMpMassUploadResult result = getWeiXinDetails(session.getId()).getWxMpService().massNewsUpload(massNews);
            openIdsMessage.setMediaId(result.getMediaId());
            getWeiXinDetails(session.getId()).getWxMpService().massOpenIdsMessageSend(openIdsMessage);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendNewsMessage(WeiXinSession session, List<Article> articles, long toGroup) throws WeiXinException {
        try {
            WxMpMassGroupMessage groupMessage = new WxMpMassGroupMessage();
            groupMessage.setMsgtype(WxConsts.MASS_MSG_NEWS);
            if (toGroup != -1) {
                groupMessage.setGroupId(toGroup);
            }
            WxMpMassNews massNews = new WxMpMassNews();
            for (Article article : articles) {
                WxMpMassNews.WxMpMassNewsArticle newsArticle = new WxMpMassNews.WxMpMassNewsArticle();
                article.getThumb().setId(this.mediaUpload(session, article.getThumb().getType(), article.getThumb().getFileItem()));
                newsArticle.setThumbMediaId(article.getThumb().getId());
                newsArticle.setTitle(article.getTitle());
                newsArticle.setContent(article.getContent());
                newsArticle.setShowCoverPic(article.isShowCoverPic());
                if (StringUtil.isNotBlank(article.getAuthor())) {
                    newsArticle.setAuthor(newsArticle.getAuthor());
                }
                if (StringUtil.isNotBlank(article.getContentSourceUrl())) {
                    newsArticle.setContentSourceUrl(newsArticle.getContentSourceUrl());
                }
                if (StringUtil.isNotBlank(article.getDigest())) {
                    newsArticle.setDigest(article.getDigest());
                }
                massNews.addArticle(newsArticle);
            }
            WxMpMassUploadResult result = getWeiXinDetails(session.getId()).getWxMpService().massNewsUpload(massNews);
            groupMessage.setMediaId(result.getMediaId());
            getWeiXinDetails(session.getId()).getWxMpService().massGroupMessageSend(groupMessage);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public String oauth2buildAuthorizationUrl(WeiXinSession session, String redirectUri, Scope scope, String state) throws WeiXinException {
        WxMpConfigStorage wxMpConfigStorage = getWeiXinDetails(session.getId()).getWxMpConfigStorage();
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
        url += "appid=" + wxMpConfigStorage.getAppId();
        url += "&redirect_uri=" + URIUtil.encodeURIComponent(redirectUri);
        url += "&response_type=code";
        url += "&scope=" + scope;
        if (StringUtil.isNotBlank(state)) {
            url += "&state=" + state;
        }
        url += "#wechat_redirect";
        return url;
    }

    public AccessToken oauth2getAccessToken(WeiXinSession session, String code) throws WeiXinException {
        try {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = getWeiXinDetails(session.getId()).getWxMpService().oauth2getAccessToken(code);
            return new AccessToken(wxMpOAuth2AccessToken.getAccessToken(), wxMpOAuth2AccessToken.getExpiresIn(), wxMpOAuth2AccessToken.getRefreshToken(), wxMpOAuth2AccessToken.getOpenId(), wxMpOAuth2AccessToken.getScope());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    public User getUser(WeiXinSession session, AccessToken accessToken) throws WeiXinException {
        if (Scope.userinfo == accessToken.getScope()) {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
            wxMpOAuth2AccessToken.setAccessToken(accessToken.getToken());
            wxMpOAuth2AccessToken.setExpiresIn(accessToken.getExpiresIn());
            wxMpOAuth2AccessToken.setOpenId(accessToken.getOpenId());
            wxMpOAuth2AccessToken.setRefreshToken(accessToken.getRefreshToken());
            wxMpOAuth2AccessToken.setScope(accessToken.getScope().name());
            try {
                return toUser(getWeiXinDetails(session.getId()).getWxMpService().oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN"));
            } catch (WxErrorException e) {
                throw new WeiXinException(e.getMessage(), e);
            }
        } else {
            return getUser(session, accessToken.getOpenId());
        }
    }

    @Override
    public void sendTextMessage(WeiXinSession session, String content, String... toUsers) throws WeiXinException {
        try {
            if (toUsers.length == 0) {
                sendTextMessage(session, content, -1);
            } else if (toUsers.length == 1) {
                getWeiXinDetails(session.getId()).getWxMpService().customMessageSend(WxMpCustomMessage.TEXT().toUser(toUsers[0]).content(content).build());
            } else {
                WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
                openIdsMessage.setMsgType(WxConsts.MASS_MSG_TEXT);
                for (String toUser : toUsers) {
                    openIdsMessage.addUser(toUser);
                }
                openIdsMessage.setContent(content);
                getWeiXinDetails(session.getId()).getWxMpService().massOpenIdsMessageSend(openIdsMessage);
            }
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendTextMessage(WeiXinSession session, String content, long toGroup) throws WeiXinException {
        try {
            WxMpMassGroupMessage groupMessage = new WxMpMassGroupMessage();
            groupMessage.setMsgtype(WxConsts.MASS_MSG_TEXT);
            if (toGroup != -1) {
                groupMessage.setGroupId(toGroup);
            }
            groupMessage.setContent(content);
            getWeiXinDetails(session.getId()).getWxMpService().massGroupMessageSend(groupMessage);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public List<Group> getGroups(WeiXinSession session) throws WeiXinException {
        try {
            List<WxMpGroup> list = getWeiXinDetails(session.getId()).getWxMpService().groupGet();
            List<Group> groups = new ArrayList<Group>();
            for (WxMpGroup wxMpGroup : list) {
                groups.add(new Group(wxMpGroup.getId(), wxMpGroup.getName(), wxMpGroup.getCount()));
            }
            return groups;
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public Group groupCreate(WeiXinSession session, String groupName) throws WeiXinException {
        try {
            WxMpGroup res = getWeiXinDetails(session.getId()).getWxMpService().groupCreate(groupName);
            return new Group(res.getId(), res.getName(), res.getCount());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void groupUpdate(WeiXinSession session, long groupId, String groupName) throws WeiXinException {
        try {
            WxMpGroup wxMpGroup = new WxMpGroup();
            wxMpGroup.setId(groupId);
            wxMpGroup.setName(groupName);
            getWeiXinDetails(session.getId()).getWxMpService().groupUpdate(wxMpGroup);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void userUpdateGroup(WeiXinSession session, String userId, long groupId) throws WeiXinException {
        try {
            getWeiXinDetails(session.getId()).getWxMpService().userUpdateGroup(userId, groupId);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public List<User> getUsers(WeiXinSession session) throws WeiXinException {
        try {
            List<User> users = new ArrayList<User>();
            WxMpUserList userList = getWeiXinDetails(session.getId()).getWxMpService().userList(null);
            if (userList.getTotal() > userList.getCount()) {
                throw new WeiXinException("微信关注粉丝超出程序设计值，请联系开发人员。重新设计");
            }
            List<Group> groups = session.getGroups();
            for (String openId : userList.getOpenIds()) {
                User user = getUser(session, openId);
                if (groups.size() > 1) {
                    Group group = ObjectUtil.find(groups, "getId()", this.getGroupIdByUserId(session, openId));
                    group.addUser(user);
                } else {
                    groups.get(0).addUser(user);
                }
                users.add(user);
            }
            return users;
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public OpenIdList getOpenIds(WeiXinSession session) {
        return null;
    }

    @Override
    public OpenIdList getOpenIds(WeiXinSession session, String nextOpenId) {
        return null;
    }

//    @Override
//    public List<String> getUsers(WeiXinSession session,String nextOpenId) throws WeiXinException {
//        try {
//            List<String> openIds = new ArrayList<String>();
//            WxMpUserList userList = getWeiXinDetails(session.getId()).getWxMpService().userList(null);
//            openIds.addAll(userList.getOpenIds());
//            while (userList.getTotal() > userList.getCount()) {
//                userList = getWeiXinDetails(session.getId()).getWxMpService().userList(userList.getNextOpenId());
//                openIds.addAll(userList.getOpenIds());
//            }
//        } catch (WxErrorException e) {
//            throw new WeiXinException(e.getMessage(),e);
//        } catch (WeiXinException e) {
//            throw new WeiXinException(e.getMessage(),e);
//        }
//        return null;
//    }

    @Override
    public Long getGroupIdByUserId(WeiXinSession session, String openId) throws WeiXinException {
        try {
            return getWeiXinDetails(session.getId()).getWxMpService().userGetGroup(openId);
        } catch (WxErrorException e) {
            throw WeiXinException.wxExceptionBuilder(e);
        }
    }

    @Override
    public User getUser(WeiXinSession session, String userId) throws WeiXinException {
        try {
            return toUser(getWeiXinDetails(session.getId()).getWxMpService().userInfo(userId, null));
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    private User toUser(WxMpUser wxMpUser) {
        if (wxMpUser == null) {
            return null;
        }
        User user = new User();
        user.setOpenId(wxMpUser.getOpenId());
        user.setAvatar(wxMpUser.getHeadImgUrl());
        user.setCity(wxMpUser.getCity());
        user.setCountry(wxMpUser.getCountry());
        user.setProvince(wxMpUser.getProvince());
        user.setLanguage(wxMpUser.getLanguage());
        user.setNickname(wxMpUser.getNickname());
        user.setSex(toSex(wxMpUser.getSex()));
        user.setSubscribe(wxMpUser.isSubscribe());
        if (wxMpUser.getSubscribeTime() != null) {
            user.setSubscribeTime(new Date(wxMpUser.getSubscribeTime()));
        }
        user.setUnionid(wxMpUser.getUnionId());
        return user;
    }

    private Sex toSex(String sex) {
        if ("男".equals(sex)) {
            return Sex.male;
        }
        if ("女".equals(sex)) {
            return Sex.female;
        }
        return Sex.unknown;
    }

    @Override
    public String mediaUpload(WeiXinSession session, Media.Type mediaType, FileItem fileItem) throws WeiXinException {
        try {
            WxMediaUploadResult uploadMediaRes = getWeiXinDetails(session.getId()).getWxMpService().mediaUpload(mediaType.name(), WebUtil.getExtension(fileItem.getName()), fileItem.getInputStream());
            return mediaType == Media.Type.thumb ? uploadMediaRes.getThumbMediaId() : uploadMediaRes.getMediaId();
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (IOException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    public FileItem mediaDownload(WeiXinSession session, String mediaId) throws WeiXinException {
        try {
            File file = getWeiXinDetails(session.getId()).getWxMpService().mediaDownload(mediaId);
            if (file == null) {
                return null;
            }
            return new LocalFileManager.LocalFileItem(file);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void refreshMenu(WeiXinSession session, Menu... menus) throws WeiXinException {
        WxMenu wxMenu = new WxMenu();
        for (Menu menu : menus) {
            WxMenu.WxMenuButton wxMenuButton = new WxMenu.WxMenuButton();
            wxMenuButton.setName(menu.getName());
            wxMenuButton.setType(menu.getType().getValue());
            wxMenuButton.setUrl(menu.getUrl());
            wxMenuButton.setKey(menu.getKey());

            for (Menu subMenu : menu.getChildren()) {
                WxMenu.WxMenuButton subWxMenuButton = new WxMenu.WxMenuButton();
                subWxMenuButton.setName(subMenu.getName());
                subWxMenuButton.setType(subMenu.getType().getValue());
                subWxMenuButton.setUrl(subMenu.getUrl());
                subWxMenuButton.setKey(subMenu.getKey());
                wxMenuButton.getSubButtons().add(subWxMenuButton);
            }

            wxMenu.getButtons().add(wxMenuButton);
        }
        try {
            getWeiXinDetails(session.getId()).getWxMpService().menuCreate(wxMenu);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public List<Menu> getMenus(WeiXinSession session) throws WeiXinException {
        try {
            WxMenu wxMenu = getWeiXinDetails(session.getId()).getWxMpService().menuGet();
            List<Menu> menus = new ArrayList<Menu>(wxMenu.getButtons().size());
            for (WxMenu.WxMenuButton button : wxMenu.getButtons()) {
                if (button.getSubButtons().isEmpty()) {
                    menus.add(new Menu(Menu.MenuType.valueOf(button.getType().toUpperCase()), button.getName(), StringUtil.defaultValue(button.getKey(), button.getUrl())));
                } else {
                    List<Menu> subMenus = new ArrayList<Menu>();
                    for (WxMenu.WxMenuButton wxMenuButton : button.getSubButtons()) {
                        subMenus.add(new Menu(Menu.MenuType.valueOf(wxMenuButton.getType().toUpperCase()), wxMenuButton.getName(), StringUtil.defaultValue(wxMenuButton.getKey(), wxMenuButton.getUrl())));
                    }
                    menus.add(new Menu(Menu.MenuType.valueOf(button.getType().toUpperCase()), button.getName(), StringUtil.defaultValue(button.getKey(), button.getUrl()), subMenus.toArray(new Menu[subMenus.size()])));
                }
            }
            return menus;
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void clearMenu(WeiXinSession session) throws WeiXinException {
        try {
            getWeiXinDetails(session.getId()).getWxMpService().menuDelete();
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    private WeiXinDetails getWeiXinDetails(String appid) throws WeiXinException {
        if (!weiXinDetailsMap.containsKey(appid)) {
            throw new WeiXinException("[appid=" + appid + "]未注册！");
        }
        return weiXinDetailsMap.get(appid);
    }

    private static class WeiXinDetails {
        private WxMpService wxMpService;
        private WxMpConfigStorage wxMpConfigStorage;

        public WeiXinDetails(AccountDetails accountDetails) {
            this.wxMpService = new WxMpServiceImpl();

            WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
            wxMpConfigStorage.setAppId(accountDetails.getAppId());
            wxMpConfigStorage.setSecret(accountDetails.getSecret());
            wxMpConfigStorage.setToken(accountDetails.getToken());
            wxMpConfigStorage.setAesKey(accountDetails.getAesKey());

            wxMpService.setWxMpConfigStorage(this.wxMpConfigStorage = wxMpConfigStorage);
        }

        public WxMpService getWxMpService() {
            return wxMpService;
        }

        public WxMpConfigStorage getWxMpConfigStorage() {
            return wxMpConfigStorage;
        }

    }

}
