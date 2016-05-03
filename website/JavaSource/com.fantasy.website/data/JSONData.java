package org.jfantasy.website.data;

import org.jfantasy.framework.jackson.JSON;

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
