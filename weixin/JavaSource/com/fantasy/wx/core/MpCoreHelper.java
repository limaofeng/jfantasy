package com.fantasy.wx.core;

import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.message.MessageFactory;
import com.fantasy.wx.message.TextMessage;
import com.fantasy.wx.message.WeiXinMessage;
import com.fantasy.wx.message.content.*;
import com.fantasy.wx.message.user.OpenIdList;
import com.fantasy.wx.session.AccountDetails;
import com.fantasy.wx.session.WeiXinSession;
import com.fantasy.wx.message.user.Group;
import com.fantasy.wx.message.user.User;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import me.chanjar.weixin.mp.bean.WxMpGroup;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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
            throw new WeiXinException(e.getMessage());
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
        throw new WeiXinException("该功能未实现");
    }

    @Override
    public void sendVoiceMessage(WeiXinSession session, Voice content, String toUser) throws WeiXinException {
        throw new WeiXinException("该功能未实现");
    }

    @Override
    public void sendVideoMessage(WeiXinSession session, Video content, String toUser) throws WeiXinException {
        throw new WeiXinException("该功能未实现");
    }

    @Override
    public void sendMusicMessage(WeiXinSession session, Music content, String toUser) throws WeiXinException {
        throw new WeiXinException("该功能未实现");
    }

    @Override
    public void sendNewsMessage(WeiXinSession session, News content, String toUser) throws WeiXinException {
        throw new WeiXinException("该功能未实现");
    }

    @Override
    public void sendTextMessage(WeiXinSession session, String content, String toUser) throws WeiXinException {
        try {
            getWeiXinDetails(session.getId()).getWxMpService().customMessageSend(WxMpCustomMessage.TEXT().toUser(toUser).content(content).build());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage());
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage());
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
            throw new WeiXinException(e.getMessage());
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage());
        }
    }

    @Override
    public Group groupCreate(WeiXinSession session, String groupName) throws WeiXinException {
        try {
            WxMpGroup res = getWeiXinDetails(session.getId()).getWxMpService().groupCreate(groupName);
            return new Group(res.getId(), res.getName(), res.getCount());
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage());
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage());
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
            throw new WeiXinException(e.getMessage());
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage());
        }
    }

    @Override
    public void userUpdateGroup(WeiXinSession session, String userId, Long groupId) throws WeiXinException {
        try {
            getWeiXinDetails(session.getId()).getWxMpService().userUpdateGroup(userId, groupId);
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage());
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage());
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
            throw new WeiXinException(e.getMessage());
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage());
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
//            throw new WeiXinException(e.getMessage());
//        } catch (WeiXinException e) {
//            throw new WeiXinException(e.getMessage());
//        }
//        return null;
//    }

    @Override
    public User getUser(WeiXinSession session, String userId) throws WeiXinException {
        try {
            getWeiXinDetails(session.getId()).getWxMpService().userInfo(userId, null);
            return new User();
        } catch (WxErrorException e) {
            throw new WeiXinException(e.getMessage());
        } catch (WeiXinException e) {
            throw new WeiXinException(e.getMessage());
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
