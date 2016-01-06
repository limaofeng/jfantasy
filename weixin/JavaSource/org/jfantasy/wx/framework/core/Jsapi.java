package org.jfantasy.wx.framework.core;


import org.jfantasy.wx.framework.exception.WeiXinException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 微信 Jsapi
 */
public interface Jsapi {

    String getTicket() throws WeiXinException;

    String getTicket(boolean forceRefresh) throws WeiXinException;

    Signature signature(String url) throws WeiXinException;

    @ApiModel(value = "JS API 签名", description = "微信 JS API 签名")
    class Signature implements Serializable {
        @ApiModelProperty("随机字符串")
        private String noncestr;
        @ApiModelProperty("ticket")
        private String ticket;
        @ApiModelProperty("时间戳")
        private long timestamp;
        @ApiModelProperty("URL")
        private String url;
        @ApiModelProperty("签名串")
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
