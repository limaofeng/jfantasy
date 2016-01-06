package org.jfantasy.website.bean;

import org.jfantasy.website.IData;

public class DataBean implements IData {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
