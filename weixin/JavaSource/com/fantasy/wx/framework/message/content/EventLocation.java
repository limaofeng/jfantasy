package com.fantasy.wx.framework.message.content;

/**
 * 上报地理位置事件
 */
public class EventLocation extends Event {

    /**
     * 地理位置维度
     */
    private Double latitude;
    /**
     * 地理位置经度
     */
    private Double longitude;
    /**
     * 地理位置精度
     */
    private Double precision;

    public EventLocation(String event, Double latitude, Double longitude, Double precision) {
        super(event);
        this.latitude = latitude;
        this.longitude = longitude;
        this.precision = precision;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getPrecision() {
        return precision;
    }

    public void setPrecision(Double precision) {
        this.precision = precision;
    }

    @Override
    public String toString() {
        return "EventLocation{" + super.toString() +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", precision=" + precision +
                '}';
    }
}
