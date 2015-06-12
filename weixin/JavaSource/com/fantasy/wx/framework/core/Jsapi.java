package com.fantasy.wx.framework.core;


import com.fantasy.wx.framework.exception.WeiXinException;

import java.io.Serializable;

/**
 * 微信 Jsapi
 */
public interface Jsapi {

    String getTicket() throws WeiXinException;

    String getTicket(boolean forceRefresh) throws WeiXinException;

    Signature signature(String url) throws WeiXinException;

    class Signature implements Serializable {
        private String noncestr;
        private String ticket;
        private long timestamp;
        private String url;
        private String signature;

        public Signature(String noncestr, String ticket, long timestamp, String url, String signature) {
            this.noncestr = noncestr;
            this.ticket = ticket;
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

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
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
