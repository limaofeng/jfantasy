package com.fantasy.website.bean;

import com.fantasy.website.IData;

/**
 * Created by wuzhiyong on 2015/3/3.
 */
public class DataBean implements IData {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
