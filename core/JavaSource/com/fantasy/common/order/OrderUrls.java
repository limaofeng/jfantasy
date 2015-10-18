package com.fantasy.common.order;

public class OrderUrls {
    /**
     * 订单详情链接
     */
    private String detailsUrl;
    /**
     * 支付成功的跳转地址
     */
    private String resultUrl;

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }
}
