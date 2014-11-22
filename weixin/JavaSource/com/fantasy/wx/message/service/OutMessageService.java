package com.fantasy.wx.message.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.wx.config.init.WeixinConfigInit;
import com.fantasy.wx.message.bean.OutMessage;
import com.fantasy.wx.message.dao.OutMessageDao;
import com.fantasy.wx.user.bean.UserInfo;
import com.fantasy.wx.user.service.UserInfoService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.WxMpMassGroupMessage;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by zzzhong on 2014/8/28.
 */
@Service
@Transactional
public class OutMessageService {
    @Resource
    private OutMessageDao outMessageDao;
    @Resource
    private WeixinConfigInit config;

    /**
     * 列表查询
     *
     * @param pager
     *            分页
     * @param filters
     *            查询条件
     * @return
     */
    public Pager<OutMessage> findPager(Pager<OutMessage> pager, List<PropertyFilter> filters) {
        return this.outMessageDao.findPager(pager, filters);
    }
    public void delete(Long... ids){
        for(Long id:ids){
            outMessageDao.delete(id);
        }
    }
    public OutMessage getOutMessage(Long id){
        return outMessageDao.get(id);
    }
    public OutMessage save(OutMessage om){
        this.outMessageDao.save(om);
        return om;
    }

    /**
     * 创建分组群发消息
     * @param groupId 分组id
     * @param msgType 消息类型
     * @return
     */
    public WxMpMassGroupMessage createGroupMessage(Long groupId,String msgType){
        WxMpMassGroupMessage groupM=new WxMpMassGroupMessage();
        groupM.setGroupId(groupId);
        groupM.setMsgtype(msgType);
        return groupM;
    }
    /**
     * 创建用户列表群发消息
     * @param openId 用户id集合
     * @param msgType 消息类型
     * @return
     */
    public WxMpMassOpenIdsMessage createOpenIdsMessage(List<String> openId,String msgType){
        WxMpMassOpenIdsMessage openIdsMessage=new WxMpMassOpenIdsMessage();
        openIdsMessage.setMsgType(msgType);
        for(String s:openId){
            openIdsMessage.addUser(s);
        }
        return openIdsMessage;
    }

    /**
     * 发送文本消息，分组群发消息
     * @param groupId 分组id
     * @param content 发送文本
     * @return
     */
    public int sendGroupMessage(Long groupId,String content){
        WxMpMassGroupMessage groupM=createGroupMessage(groupId,WxConsts.MASS_MSG_TEXT);
        groupM.setContent(content);
        save(BeanUtil.copyProperties(new OutMessage(),groupM));
        try {
            WxMpMassSendResult result=config.getUtil().massGroupMessageSend(groupM);
            return Integer.parseInt(result.getErrorCode());
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
    }
    /**
     * 发送文本消息，用户列表群发消息
     * @param openid 永固id
     * @param content 发送文本
     * @return
     */
    public int sendOpenIdMessage(List<String> openid,String content){
        WxMpMassOpenIdsMessage message=createOpenIdsMessage(openid,WxConsts.MASS_MSG_TEXT);
        message.setContent(content);
        save(BeanUtil.copyProperties(new OutMessage(),message));
        try {
            WxMpMassSendResult massResult = config.getUtil().massOpenIdsMessageSend(message);
            return Integer.parseInt(massResult.getErrorCode());
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getError().getErrorCode();
        }
    }

}
