package com.fantasy.swp.bean;

import com.fantasy.swp.IData;

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
