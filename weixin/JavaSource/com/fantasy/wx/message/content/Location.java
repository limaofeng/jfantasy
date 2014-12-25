package com.fantasy.wx.message.content;

/**
 * 地理位置
 */
public class Location {

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
    private int scale;
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

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
