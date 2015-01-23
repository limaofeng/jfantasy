package com.fantasy.swp.data;

import com.fantasy.framework.util.jackson.JSON;

public class JSONData extends AbstractTemplateData {

    private String json;

    public JSONData(String json){
        this.json = json;
    }

    @Override
    public Object getValue() {
        return JSON.deserialize(json);
    }

}
