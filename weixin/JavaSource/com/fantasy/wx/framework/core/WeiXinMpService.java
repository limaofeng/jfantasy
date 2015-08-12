package com.fantasy.wx.framework.core;

import com.fantasy.file.FileItem;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.security.bean.enums.Sex;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.message.*;
import com.fantasy.wx.framework.message.content.*;
import com.fantasy.wx.framework.message.user.Group;
import com.fantasy.wx.framework.message.user.OpenIdList;
import com.fantasy.wx.framework.message.user.User;
import com.fantasy.wx.framework.oauth2.AccessToken;
import com.fantasy.wx.framework.oauth2.Scope;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.http.URIUtil;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.*;
import me.chanjar.weixin.mp.bean.custombuilder.MusicBuilder;
import me.chanjar.weixin.mp.bean.custombuilder.VideoBuilder;
import me.chanjar.weixin.mp.bean.outxmlbuilder.NewsBuilder;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WeiXinMpService implements WeiXinService {

    private WxMpService wxMpService;
    private WxMpConfigStorage wxMpConfigStorage;
    private Jsapi jsapi;
    private final static Log LOG = LogFactory.getLog(WeiXinMpService.class);

    public WeiXinMpService(WxMpService wxMpService, WxMpConfigStorage wxMpConfigStorage) {
        this.wxMpService = wxMpService;
        this.wxMpConfigStorage = wxMpConfigStorage;
        this.jsapi = new DefaultJsapi(this);
    }

    @Override
    public String getJsapiTicket() throws WeiXinException {
        try {
            return wxMpService.getJsapiTicket();
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public String getJsapiTicket(boolean forceRefresh) throws WeiXinException {
        try {
            return wxMpService.getJsapiTicket(forceRefresh);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public Jsapi.Signature createJsapiSignature(String url) throws WeiXinException {
        try {
            WxJsapiSignature wxJsapiSignature = wxMpService.createJsapiSignature(url);
            return new Jsapi.Signature(wxJsapiSignature.getNoncestr(), wxJsapiSignature.getJsapiTicket(), wxJsapiSignature.getTimestamp(), wxJsapiSignature.getUrl(), wxJsapiSignature.getSignature());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public WeiXinMessage parseInMessage(HttpServletRequest request) throws WeiXinException {
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


        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
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
            inMessage = WxMpXmlMessage.fromEncryptedXml(input, wxMpConfigStorage, timestamp, nonce, msg_signature);
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
    public void sendImageMessage(Image content, long toGroup) throws WeiXinException {
        try {
            //上传图片文件
            Media media = content.getMedia();
            media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
            WxMpMassGroupMessage groupMessage = new WxMpMassGroupMessage();
            groupMessage.setMsgtype(WxConsts.MASS_MSG_IMAGE);
            if (toGroup != -1) {
                groupMessage.setGroupId(toGroup);
            }
            groupMessage.setMediaId(media.getId());
            this.wxMpService.massGroupMessageSend(groupMessage);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public String mediaUpload(Media.Type mediaType, FileItem fileItem) throws WeiXinException {
        try {
            WxMediaUploadResult uploadMediaRes = this.wxMpService.mediaUpload(mediaType.name(), WebUtil.getExtension(fileItem.getName()), fileItem.getInputStream());
            return mediaType == Media.Type.thumb ? uploadMediaRes.getThumbMediaId() : uploadMediaRes.getMediaId();
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (IOException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendImageMessage(Image content, String... toUsers) throws WeiXinException {
        try {
            if (toUsers.length == 0) {
                this.sendImageMessage(content, -1);
                return;
            }
            //上传图片文件
            Media media = content.getMedia();
            media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
            if (toUsers.length == 1) {
                this.wxMpService.customMessageSend(WxMpCustomMessage.IMAGE().toUser(toUsers[0]).mediaId(media.getId()).build());
            } else {
                WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
                openIdsMessage.setMsgType(WxConsts.MASS_MSG_IMAGE);
                for (String toUser : toUsers) {
                    openIdsMessage.addUser(toUser);
                }
                openIdsMessage.setMediaId(media.getId());
                this.wxMpService.massOpenIdsMessageSend(openIdsMessage);
            }
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public String parseInMessage(String encryptType, WeiXinMessage message) throws WeiXinException {
        encryptType = StringUtils.isBlank(encryptType) ? "raw" : encryptType;
        WxMpXmlOutMessage outMessage;
        if (message instanceof TextMessage) {
            outMessage = WxMpXmlOutMessage.TEXT()
                    .content(((TextMessage) message).getContent())
                    .fromUser(message.getFromUserName())
                    .toUser(message.getToUserName())
                    .build();
        } else if (message instanceof ImageMessage) {
            Media media = ((ImageMessage) message).getContent().getMedia();
            media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
            outMessage = WxMpXmlOutMessage.IMAGE().mediaId(media.getId()).fromUser(message.getFromUserName())
                    .toUser(message.getToUserName())
                    .build();
        } else if (message instanceof VoiceMessage) {
            Media media = ((VoiceMessage) message).getContent().getMedia();
            media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
            outMessage = WxMpXmlOutMessage.VOICE().mediaId(media.getId()).fromUser(message.getFromUserName())
                    .toUser(message.getToUserName())
                    .build();
        } else if (message instanceof VideoMessage) {
            Media media = ((VideoMessage) message).getContent().getMedia();
            media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
            outMessage = WxMpXmlOutMessage.VIDEO().mediaId(media.getId()).fromUser(message.getFromUserName())
                    .toUser(message.getToUserName())
                    .build();
        } else if (message instanceof MusicMessage) {
            Music music = ((MusicMessage) message).getContent();
            Media thumb = music.getThumb();
            thumb.setId(this.mediaUpload(thumb.getType(), thumb.getFileItem()));
            outMessage = WxMpXmlOutMessage.MUSIC().musicUrl(music.getUrl()).hqMusicUrl(music.getHqUrl()).title(music.getTitle()).description(music.getDescription()).thumbMediaId(thumb.getId()).fromUser(message.getFromUserName())
                    .toUser(message.getToUserName())
                    .build();
        } else if (message instanceof NewsMessage) {
            List<News> newses = ((NewsMessage) message).getContent();
            NewsBuilder newsBuilder = WxMpXmlOutMessage.NEWS();
            for (News news : newses) {
                WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
                item.setTitle(news.getLink().getTitle());
                item.setDescription(news.getLink().getDescription());
                item.setPicUrl(news.getPicUrl());
                item.setUrl(news.getLink().getUrl());
                newsBuilder.addArticle(item);
            }
            outMessage = newsBuilder.fromUser(message.getFromUserName())
                    .toUser(message.getToUserName())
                    .build();
        } else {
            throw new WeiXinException("不支持的消息类型");
        }
        if ("raw".equals(encryptType)) {
            return outMessage.toXml();
        } else if ("aes".equals(encryptType)) {
            return outMessage.toEncryptedXml(wxMpConfigStorage);
        } else {
            throw new WeiXinException("不可识别的加密类型");
        }
    }

    @Override
    public void sendVoiceMessage(Voice content, String... toUsers) throws WeiXinException {
        try {
            if (toUsers.length == 0) {
                this.sendVoiceMessage(content, -1);
                return;
            }
            //上传语言文件
            Media media = content.getMedia();
            media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
            if (toUsers.length == 1) {
                wxMpService.customMessageSend(WxMpCustomMessage.VOICE().toUser(toUsers[0]).mediaId(media.getId()).build());
            } else {
                WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
                openIdsMessage.setMsgType(WxConsts.MASS_MSG_VOICE);
                for (String toUser : toUsers) {
                    openIdsMessage.addUser(toUser);
                }
                openIdsMessage.setMediaId(media.getId());
                wxMpService.massOpenIdsMessageSend(openIdsMessage);
            }
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendVoiceMessage(Voice content, long toGroup) throws WeiXinException {
        try {
            //上传语言文件
            Media media = content.getMedia();
            media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
            WxMpMassGroupMessage groupMessage = new WxMpMassGroupMessage();
            groupMessage.setMsgtype(WxConsts.MASS_MSG_VOICE);
            if (toGroup != -1) {
                groupMessage.setGroupId(toGroup);
            }
            groupMessage.setMediaId(media.getId());
            wxMpService.massGroupMessageSend(groupMessage);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendVideoMessage(Video content, String... toUsers) throws WeiXinException {
        try {
            if (toUsers.length == 0) {
                this.sendVideoMessage(content, -1);
                return;
            }
            //上传视频
            Media media = content.getMedia();
            media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
            //发送消息
            if (toUsers.length == 1) {
                //上传缩略图
                Media thumb = content.getThumb();
                thumb.setId(this.mediaUpload(thumb.getType(), thumb.getFileItem()));
                VideoBuilder videoBuilder = WxMpCustomMessage.VIDEO().toUser(toUsers[0]).mediaId(media.getId()).thumbMediaId(thumb.getId());
                if (StringUtil.isNotBlank(content.getTitle())) {
                    videoBuilder.title(content.getTitle());
                }
                if (StringUtil.isNotBlank(content.getDescription())) {
                    videoBuilder.description(content.getDescription());
                }
                wxMpService.customMessageSend(videoBuilder.build());
            } else {
                WxMpMassVideo massVideo = new WxMpMassVideo();
                massVideo.setMediaId(media.getId());
                massVideo.setTitle(content.getTitle());
                massVideo.setDescription(content.getDescription());
                WxMpMassUploadResult result = wxMpService.massVideoUpload(massVideo);
                WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
                openIdsMessage.setMsgType(WxConsts.MASS_MSG_VIDEO);
                for (String toUser : toUsers) {
                    openIdsMessage.addUser(toUser);
                }
                openIdsMessage.setMediaId(result.getMediaId());
                wxMpService.massOpenIdsMessageSend(openIdsMessage);
            }
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendVideoMessage(Video content, long toGroup) throws WeiXinException {
        try {
            //上传视频
            Media media = content.getMedia();
            media.setId(this.mediaUpload(media.getType(), media.getFileItem()));
            //发送消息
            WxMpMassVideo massVideo = new WxMpMassVideo();
            massVideo.setMediaId(media.getId());
            massVideo.setTitle(content.getTitle());
            massVideo.setDescription(content.getDescription());
            WxMpMassUploadResult result = wxMpService.massVideoUpload(massVideo);
            WxMpMassGroupMessage groupMessage = new WxMpMassGroupMessage();
            groupMessage.setMsgtype(WxConsts.MASS_MSG_VIDEO);
            if (toGroup != -1) {
                groupMessage.setGroupId(toGroup);
            }
            groupMessage.setMediaId(result.getMediaId());
            wxMpService.massGroupMessageSend(groupMessage);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendMusicMessage(Music content, String toUser) throws WeiXinException {
        try {
            //上传缩略图
            Media thumb = content.getThumb();
            thumb.setId(this.mediaUpload(thumb.getType(), thumb.getFileItem()));
            //发送消息
            MusicBuilder musicBuilder = WxMpCustomMessage.MUSIC().toUser(toUser).musicUrl(content.getUrl()).hqMusicUrl(content.getHqUrl()).thumbMediaId(thumb.getId());
            if (StringUtil.isNotBlank(content.getTitle())) {
                musicBuilder.title(content.getTitle());
            }
            if (StringUtil.isNotBlank(content.getDescription())) {
                musicBuilder.description(content.getDescription());
            }
            wxMpService.customMessageSend(musicBuilder.build());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendNewsMessage(List<News> content, String toUser) throws WeiXinException {
        try {
            me.chanjar.weixin.mp.bean.custombuilder.NewsBuilder newsBuilder = WxMpCustomMessage.NEWS().toUser(toUser);
            for (News news : content) {
                WxMpCustomMessage.WxArticle article = new WxMpCustomMessage.WxArticle();
                article.setPicUrl(news.getPicUrl());
                article.setTitle(news.getLink().getTitle());
                article.setDescription(news.getLink().getDescription());
                article.setUrl(news.getLink().getUrl());
                newsBuilder.addArticle(article);
            }
            wxMpService.customMessageSend(newsBuilder.build());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendNewsMessage(List<Article> articles, String... toUsers) throws WeiXinException {
        if (toUsers.length == 0) {
            this.sendNewsMessage(articles, -1);
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
                article.getThumb().setId(this.mediaUpload(article.getThumb().getType(), article.getThumb().getFileItem()));
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
            WxMpMassUploadResult result = wxMpService.massNewsUpload(massNews);
            openIdsMessage.setMediaId(result.getMediaId());
            wxMpService.massOpenIdsMessageSend(openIdsMessage);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendNewsMessage(List<Article> articles, long toGroup) throws WeiXinException {
        try {
            WxMpMassGroupMessage groupMessage = new WxMpMassGroupMessage();
            groupMessage.setMsgtype(WxConsts.MASS_MSG_NEWS);
            if (toGroup != -1) {
                groupMessage.setGroupId(toGroup);
            }
            WxMpMassNews massNews = new WxMpMassNews();
            for (Article article : articles) {
                WxMpMassNews.WxMpMassNewsArticle newsArticle = new WxMpMassNews.WxMpMassNewsArticle();
                article.getThumb().setId(this.mediaUpload(article.getThumb().getType(), article.getThumb().getFileItem()));
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
            WxMpMassUploadResult result = wxMpService.massNewsUpload(massNews);
            groupMessage.setMediaId(result.getMediaId());
            wxMpService.massGroupMessageSend(groupMessage);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public String oauth2buildAuthorizationUrl(String redirectUri, Scope scope, String state) throws WeiXinException {
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
        url += "appid=" + wxMpConfigStorage.getAppId();
        url += "&redirect_uri=" + URIUtil.encodeURIComponent(redirectUri);
        url += "&response_type=code";
        url += "&scope=" + scope.getValue();
        if (StringUtil.isNotBlank(state)) {
            url += "&state=" + state;
        }
        url += "#wechat_redirect";
        return url;
    }

    private AccessToken oauth2getAccessToken(String code) throws WeiXinException {
        try {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            return new AccessToken(wxMpOAuth2AccessToken.getAccessToken(), wxMpOAuth2AccessToken.getExpiresIn(), wxMpOAuth2AccessToken.getRefreshToken(), wxMpOAuth2AccessToken.getOpenId(), wxMpOAuth2AccessToken.getScope());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    public User getOauth2User(String code) throws WeiXinException {
        AccessToken accessToken = oauth2getAccessToken(code);
        if (accessToken == null) {
            throw new WeiXinException(code + " ==> AccessToken is null ");
        }
        if (Scope.userinfo == accessToken.getScope()) {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
            wxMpOAuth2AccessToken.setAccessToken(accessToken.getToken());
            wxMpOAuth2AccessToken.setExpiresIn(accessToken.getExpiresIn());
            wxMpOAuth2AccessToken.setOpenId(accessToken.getOpenId());
            wxMpOAuth2AccessToken.setRefreshToken(accessToken.getRefreshToken());
            wxMpOAuth2AccessToken.setScope(accessToken.getScope().name());
            try {
                return toUser(wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, "zh_CN"));
            } catch (WxErrorException e) {
                throw new WeiXinException(e.getMessage(), e);
            }
        } else {
            return getUser(accessToken.getOpenId());
        }
    }

    public void sendTemplateMessage(Template content, String toUser) throws WeiXinException {
        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        List<WxMpTemplateData> wxMpTemplateDatas = new ArrayList<WxMpTemplateData>();
        for (Map.Entry<String, Template.Data> entry : content.getDatas().entrySet()) {
            WxMpTemplateData wxMpTemplateData = new WxMpTemplateData();
            wxMpTemplateData.setName(entry.getKey());
            wxMpTemplateData.setValue(entry.getValue().getValue());
            wxMpTemplateData.setColor(entry.getValue().getColor());
            wxMpTemplateDatas.add(wxMpTemplateData);
        }
        try {
            wxMpTemplateMessage.setTemplateId(content.getTemplateId());
            wxMpTemplateMessage.setDatas(wxMpTemplateDatas);
            wxMpTemplateMessage.setTopColor(content.getTopColor());
            wxMpTemplateMessage.setUrl(content.getUrl());
            wxMpTemplateMessage.setToUser(toUser);
            wxMpService.templateSend(wxMpTemplateMessage);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendTextMessage(String content, String... toUsers) throws WeiXinException {
        try {
            if (toUsers.length == 0) {
                sendTextMessage(content, -1);
            } else if (toUsers.length == 1) {
                wxMpService.customMessageSend(WxMpCustomMessage.TEXT().toUser(toUsers[0]).content(content).build());
            } else {
                WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
                openIdsMessage.setMsgType(WxConsts.MASS_MSG_TEXT);
                for (String toUser : toUsers) {
                    openIdsMessage.addUser(toUser);
                }
                openIdsMessage.setContent(content);
                wxMpService.massOpenIdsMessageSend(openIdsMessage);
            }
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendTextMessage(String content, long toGroup) throws WeiXinException {
        try {
            WxMpMassGroupMessage groupMessage = new WxMpMassGroupMessage();
            groupMessage.setMsgtype(WxConsts.MASS_MSG_TEXT);
            if (toGroup != -1) {
                groupMessage.setGroupId(toGroup);
            }
            groupMessage.setContent(content);
            wxMpService.massGroupMessageSend(groupMessage);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public List<Group> getGroups() throws WeiXinException {
        try {
            List<WxMpGroup> list = wxMpService.groupGet();
            List<Group> groups = new ArrayList<Group>();
            for (WxMpGroup wxMpGroup : list) {
                groups.add(new Group(wxMpGroup.getId(), wxMpGroup.getName(), wxMpGroup.getCount()));
            }
            return groups;
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public Group groupCreate(String groupName) throws WeiXinException {
        try {
            WxMpGroup res = wxMpService.groupCreate(groupName);
            return new Group(res.getId(), res.getName(), res.getCount());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void groupUpdate(long groupId, String groupName) throws WeiXinException {
        try {
            WxMpGroup wxMpGroup = new WxMpGroup();
            wxMpGroup.setId(groupId);
            wxMpGroup.setName(groupName);
            wxMpService.groupUpdate(wxMpGroup);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void userUpdateGroup(String userId, long groupId) throws WeiXinException {
        try {
            wxMpService.userUpdateGroup(userId, groupId);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public List<User> getUsers() throws WeiXinException {
        try {
            List<User> users = new ArrayList<User>();
            WxMpUserList userList = wxMpService.userList(null);
            if (userList.getTotal() > userList.getCount()) {
                throw new WeiXinException("微信关注粉丝超出程序设计值，请联系开发人员。重新设计");
            }
            List<Group> groups = getGroups();
            for (String openId : userList.getOpenIds()) {
                User user = getUser(openId);
                if (groups.size() > 1) {
                    Group group = ObjectUtil.find(groups, "getId()", this.getGroupIdByUserId(openId));
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
    public OpenIdList getOpenIds() {
        return null;
    }

    @Override
    public OpenIdList getOpenIds(String nextOpenId) {
        return null;
    }

    @Override
    public Long getGroupIdByUserId(String openId) throws WeiXinException {
        try {
            return wxMpService.userGetGroup(openId);
        } catch (WxErrorException e) {
            throw WeiXinException.wxExceptionBuilder(e);
        }
    }

    @Override
    public User getUser(String userId) throws WeiXinException {
        try {
            return toUser(wxMpService.userInfo(userId, null));
        } catch (WxErrorException e) {
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

    private LocalFileManager fileManager = new LocalFileManager(System.getProperty("java.io.tmpdir"));

    public FileItem mediaDownload(String mediaId) throws WeiXinException {
        try {
            File file = wxMpService.mediaDownload(mediaId);
            if (file == null) {
                return null;
            }
            return fileManager.retrieveFileItem(file);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void refreshMenu(Menu... menus) throws WeiXinException {
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
            wxMpService.menuCreate(wxMenu);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    public Jsapi getJsapi() throws WeiXinException {
        return this.jsapi;
    }

    @Override
    public List<Menu> getMenus() throws WeiXinException {
        try {
            WxMenu wxMenu = wxMpService.menuGet();
            List<Menu> menus = new ArrayList<Menu>(wxMenu.getButtons().size());
            for (WxMenu.WxMenuButton button : wxMenu.getButtons()) {
                Menu.MenuType type = StringUtil.isBlank(button.getType()) ? Menu.MenuType.UNKNOWN : Menu.MenuType.valueOf(button.getType().toUpperCase());
                if (button.getSubButtons().isEmpty()) {
                    menus.add(new Menu(type, button.getName(), StringUtil.defaultValue(button.getKey(), button.getUrl())));
                } else {
                    List<Menu> subMenus = new ArrayList<Menu>();
                    for (WxMenu.WxMenuButton wxMenuButton : button.getSubButtons()) {
                        subMenus.add(new Menu(Menu.MenuType.valueOf(wxMenuButton.getType().toUpperCase()), wxMenuButton.getName(), StringUtil.defaultValue(wxMenuButton.getKey(), wxMenuButton.getUrl())));
                    }
                    menus.add(new Menu(type, button.getName(), StringUtil.defaultValue(button.getKey(), button.getUrl()), subMenus.toArray(new Menu[subMenus.size()])));
                }
            }
            return menus;
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void clearMenu() throws WeiXinException {
        try {
            wxMpService.menuDelete();
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

}
