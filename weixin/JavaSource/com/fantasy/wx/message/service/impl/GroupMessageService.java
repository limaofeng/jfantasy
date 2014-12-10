package com.fantasy.wx.message.service.impl;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.wx.config.init.WeixinConfigInit;
import com.fantasy.wx.message.bean.GroupMessage;
import com.fantasy.wx.message.dao.OutMessageDao;
import com.fantasy.wx.message.service.IGroupMessageService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.WxMpMassGroupMessage;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Service
@Transactional
public class GroupMessageService implements IGroupMessageService {
    @Resource
    private OutMessageDao outMessageDao;
    @Resource
    private WeixinConfigInit config;

    @Override
    public Pager<GroupMessage> findPager(Pager<GroupMessage> pager, List<PropertyFilter> filters) {
        return this.outMessageDao.findPager(pager, filters);
    }

    @Override
    public void delete(Long... ids) {
        for (Long id : ids) {
            outMessageDao.delete(id);
        }
    }

    @Override
    public GroupMessage getGroupMessage(Long id) {
        return outMessageDao.get(id);
    }

    @Override
    public GroupMessage save(GroupMessage om) {
        this.outMessageDao.save(om);
        return om;
    }

    @Override
    public WxMpMassGroupMessage createGroupMessage(Long groupId, String msgType) {
        WxMpMassGroupMessage groupM = new WxMpMassGroupMessage();
        groupM.setGroupId(groupId);
        groupM.setMsgtype(msgType);
        return groupM;
    }

    @Override
    public WxMpMassOpenIdsMessage createOpenIdsMessage(List<String> openId, String msgType) {
        WxMpMassOpenIdsMessage openIdsMessage = new WxMpMassOpenIdsMessage();
        openIdsMessage.setMsgType(msgType);
        for (String s : openId) {
            openIdsMessage.addUser(s);
        }
        return openIdsMessage;
    }

    @Override
    public int sendTextGroupMessage(Long groupId, String content) {
        WxMpMassGroupMessage groupM = createGroupMessage(groupId, WxConsts.MASS_MSG_TEXT);
        groupM.setContent(content);
        save(BeanUtil.copyProperties(new GroupMessage(), groupM));
        try {
            WxMpMassSendResult result = config.getUtil().massGroupMessageSend(groupM);
            return Integer.parseInt(result.getErrorCode());
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
    }

    @Override
    public int sendTextOpenIdMessage(List<String> openid, String content) {
        WxMpMassOpenIdsMessage message = createOpenIdsMessage(openid, WxConsts.MASS_MSG_TEXT);
        message.setContent(content);
        save(BeanUtil.copyProperties(new GroupMessage(), message));
        try {
            WxMpMassSendResult massResult = config.getUtil().massOpenIdsMessageSend(message);
            return Integer.parseInt(massResult.getErrorCode());
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
    }

}
