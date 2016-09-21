package org.jfantasy.wx.framework.core;


import org.jfantasy.wx.framework.exception.WeiXinException;
import java.io.Serializable;

/**
 * 微信 Jsapi
 */
public interface Jsapi {

    String getTicket() throws WeiXinException;

    String getTicket(boolean forceRefresh) throws WeiXinException;

    Signature signature(String url) throws WeiXinException;

    /**
     * JS API 签名
     */
    class Signature implements Serializable {

        private static final long serialVersionUID = -6144963095625586529L;

        /** 随机字符串 **/
        private String noncestr;
        /** appid **/
        private String appid;
        /** 时间戳 **/
        private long timestamp;
        /** URL **/
        private String url;
        /** 签名串 **/
        private String signature;

        Signature(String noncestr, String appid, long timestamp, String url, String signature) {
            this.noncestr = noncestr;
            this.appid = appid;
            this.timestamp = timestamp;
            this.url = url;
            this.signature = signature;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

}
