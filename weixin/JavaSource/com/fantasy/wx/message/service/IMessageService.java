package com.fantasy.wx.message.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.exception.WxException;
import com.fantasy.wx.message.bean.Message;

import java.util.List;

/**
 * Created by zzzhong on 2014/12/4.
 */
public interface IMessageService {
    /**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
    public Pager<Message> findPager(Pager<Message> pager, List<PropertyFilter> filters);

    /**
     * 删除消息对象
     *
     * @param ids id数组
     */
    public void delete(Long... ids);

    /**
     * 获取消息对象
     *
     * @param id
     * @return
     */
    public Message getMessage(Long id);

    /**
     * 保存消息对象
     *
     * @param message
     * @return
     */
    public Message save(Message message);

    /**
     * 根据消息类型保存消息对象 更具msgType保存mediaId的消息的媒体文件
     * @param message
     * @return
     * @throws WxException
     */
    public Message saveByType(Message message) throws WxException;

    /**
     * 发送文本消息
     *
     * @param touser  发送人openId
     * @param content 发送内容
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
    public int sendTextMessage(String touser, String content);
}
