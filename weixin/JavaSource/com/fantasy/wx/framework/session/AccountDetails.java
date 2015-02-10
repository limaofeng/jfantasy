package com.fantasy.wx.framework.session;

/**
 * 微信号，账号详细质料
 */
public interface AccountDetails {


    public static enum Type {
        /**
         * 服务号
         */
        service,
        /**
         * 订阅号
         */
        subscription,
        /**
         * 企业号
         */
        enterprise
    }

    /**
     * 微信申请的appid
     *
     * @return appid
     */
    String getAppId();

    /**
     * 公众号类型
     *
     * @return type
     */
    public Type getType();

    /**
     * 名称
     *
     * @return String
     */
    public String getName();

    /**
     * 密钥
     *
     * @return String
     */
    public String getSecret();

    /**
     * 令牌 name
     *
     * @return String
     */
    public String getToken();

    /**
     * 安全验证码
     *
     * @return String
     */
    public String getAesKey();

    /**
     * 原始ID 用户消息回复时的 formusername
     *
     * @return String
     */
    public String getPrimitiveId();

}
