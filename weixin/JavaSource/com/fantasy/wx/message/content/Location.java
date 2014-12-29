package com.fantasy.wx.message.content;

/**
 * 地理位置
 */
public class Location {

    public Location(Double x, Double y, Double scale, String label) {
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.label = label;
    }

    /**
     * 地理位置维度
     */
    private Double x;
    /**
     * 地理位置经度
     */
    private Double y;
    /**
     * 地图缩放大小
     */
    private Double scale;
    /**
     * 地理位置信息
     */
    private String label;

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getScale() {
        return scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", scale=" + scale +
                ", label='" + label + '\'' +
                '}';
    }
}
