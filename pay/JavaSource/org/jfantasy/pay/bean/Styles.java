package org.jfantasy.pay.bean;

import java.io.Serializable;

/**
 * 卡样式
 */
public class Styles implements Serializable {

    private String bg_front;
    private String bg_back;

    public Styles(){}

    public Styles(String bg_front, String bg_back) {
        this.bg_front = bg_front;
        this.bg_back = bg_back;
    }

    public String getBg_front() {
        return bg_front;
    }

    public void setBg_front(String bg_front) {
        this.bg_front = bg_front;
    }

    public String getBg_back() {
        return bg_back;
    }

    public void setBg_back(String bg_back) {
        this.bg_back = bg_back;
    }

}
