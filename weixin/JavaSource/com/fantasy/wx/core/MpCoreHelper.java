package com.fantasy.wx.core;

import com.fantasy.file.FileItem;
import com.fantasy.file.manager.LocalFileManager;
import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.common.file.FileUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.web.WebUtil;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.message.MessageFactory;
import com.fantasy.wx.message.TextMessage;
import com.fantasy.wx.message.WeiXinMessage;
import com.fantasy.wx.message.content.*;
import com.fantasy.wx.message.user.Group;
import com.fantasy.wx.message.user.OpenIdList;
import com.fantasy.wx.message.user.User;
import com.fantasy.wx.session.AccountDetails;
import com.fantasy.wx.session.WeiXinSession;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.*;
import me.chanjar.weixin.mp.bean.custombuilder.MusicBuilder;
import me.chanjar.weixin.mp.bean.custombuilder.VideoBuilder;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
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
            return MessageFactory.createImageMessage(inMessage.getMsgId(), inMessage.getFromUserName(), new Date(inMessage.getCreateTime()), inMessage.getMediaId(), inMessage.getUrl());
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
    public void sendImageMessage(WeiXinSession session, Image content, String toUser) throws WeiXinException {
        try {
            //上传图片文件
            Media media = content.getMedia();
            media.setId(this.mediaUpload(session, media.getType(), media.getFileItem()));
            getWeiXinDetails(session.getId()).getWxMpService().customMessageSend(WxMpCustomMessage.IMAGE().toUser(toUser).mediaId(media.getId()).build());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendVoiceMessage(WeiXinSession session, Voice content, String toUser) throws WeiXinException {
        try {
            //上传音乐文件
            Media media = content.getMedia();
            media.setId(this.mediaUpload(session, media.getType(), media.getFileItem()));
            getWeiXinDetails(session.getId()).getWxMpService().customMessageSend(WxMpCustomMessage.VOICE().toUser(toUser).mediaId(media.getId()).build());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
    }

    @Override
    public void sendVideoMessage(WeiXinSession session, Video content, String toUser) throws WeiXinException {
        try {
            //上传视频
            Media media = content.getMedia();
            media.setId(this.mediaUpload(session, media.getType(), media.getFileItem()));
            //上传缩略图
            Media thumb = content.getThumb();
            thumb.setId(this.mediaUpload(session, thumb.getType(), thumb.getFileItem()));
            //发送消息
            VideoBuilder videoBuilder = WxMpCustomMessage.VIDEO().toUser(toUser).mediaId(media.getId()).thumbMediaId(thumb.getId());
            if (StringUtil.isNotBlank(content.getTitle())) {
                videoBuilder.title(content.getTitle());
            }
            if (StringUtil.isNotBlank(content.getDescription())) {
                videoBuilder.description(content.getDescription());
            }
            getWeiXinDetails(session.getId()).getWxMpService().customMessageSend(videoBuilder.build());
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
    public void sendNewsMessage(WeiXinSession session, News content, String... toUsers) throws WeiXinException {
        try {
            if (toUsers.length == 0) {
                throw new WeiXinException("消息接收人为空!");
            }
            if (toUsers.length == 1) {
                WxMpCustomMessage.WxArticle article = new WxMpCustomMessage.WxArticle();
                article.setPicUrl(content.getPicUrl());
                article.setTitle(content.getLink().getTitle());
                article.setDescription(content.getLink().getDescription());
                article.setUrl(content.getLink().getUrl());
                getWeiXinDetails(session.getId()).getWxMpService().customMessageSend(WxMpCustomMessage.NEWS().toUser(toUsers[0]).addArticle(article).build());
            } else {
                WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
                openIdsMessage.setMsgType(WxConsts.MASS_MSG_NEWS);
                for (String toUser : toUsers) {
                    openIdsMessage.addUser(toUser);
                }
                WxMpMassNews massNews = new WxMpMassNews();

                WxMpMassUploadResult result =  getWeiXinDetails(session.getId()).getWxMpService().massNewsUpload(massNews);

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
    public void sendTextMessage(WeiXinSession session, String content, String... toUsers) throws WeiXinException {
        try {
            if (toUsers.length == 0) {
                throw new WeiXinException("消息接收人为空!");
            }
            if (toUsers.length == 1) {
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
    public void sendTextMessage(WeiXinSession session, String content, Long toGroup) throws WeiXinException {
        try {
            WxMpMassGroupMessage groupMessage = new WxMpMassGroupMessage();
            groupMessage.setMsgtype(WxConsts.MASS_MSG_TEXT);
            groupMessage.setGroupId(toGroup);
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
    public void userUpdateGroup(WeiXinSession session, String userId, Long groupId) throws WeiXinException {
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
            for (String openId : userList.getOpenIds()) {
                users.add(getUser(session, openId));
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
    public User getUser(WeiXinSession session, String userId) throws WeiXinException {
        try {
            getWeiXinDetails(session.getId()).getWxMpService().userInfo(userId, null);
            return new User();
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage(), e);
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage(), e);
        }
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

    private static class WeiXinMediaInputStream extends InputStream {

        private InputStream inputStream;
        private File file;

        public WeiXinMediaInputStream(File file) {
            this.file = file;
            try {
                this.inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new IgnoreException(e.getMessage());
            }
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public void close() throws IOException {
            super.close();
            //流读取完成后删除临时文件
            FileUtil.delFile(file);
        }
    }

}
