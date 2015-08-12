package com.fantasy.wx.framework.message.content;

import java.util.HashMap;
import java.util.Map;

/**
 * 模板消息
 */
public class Template {
    private String templateId;
    private String url;
    private String topColor = "#FF0000";
    private Map<String, Data> datas = new HashMap<String, Data>();

    public Template(String templateId, String url) {
        this(templateId, url, "#FF0000");
    }

    public Template(String templateId, String url, String topColor) {
        this.setTemplateId(templateId);
        this.setUrl(url);
        this.setTopColor(topColor);
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopColor() {
        return topColor;
    }

    public void setTopColor(String topColor) {
        this.topColor = topColor;
    }

    public Map<String, Data> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, Data> datas) {
        this.datas = datas;
    }

    public Template add(String name, String value) {
        this.datas.put(name, new Data(value));
        return this;
    }

    public Template add(String name, String value, String color) {
        this.datas.put(name, new Data(value, color));
        return this;
    }

    public static class Data {
        private String value;
        private String color;

        public Data(String value) {
            this.value = value;
        }

        public Data(String value, String color) {
            this.value = value;
            this.color = color;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
