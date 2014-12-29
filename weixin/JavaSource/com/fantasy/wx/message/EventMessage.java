package com.fantasy.wx.message;

/**
 * 事件消息接口
 */
public interface EventMessage<T> extends WeiXinMessage<T> {

    public enum EventType {
        /**
         * 订阅 / 扫描带参数二维码事件
         */
        subscribe,
        /**
         * 取消订阅
         */
        unsubscribe,
        /**
         * 用户已关注时的事件推送
         */
        SCAN,
        /**
         * 点击菜单拉取消息时的事件推送
         */
        CLICK,
        /**
         * 点击菜单跳转链接时的事件推送
         */
        VIEW
    }

    public EventType getEventType();

}
