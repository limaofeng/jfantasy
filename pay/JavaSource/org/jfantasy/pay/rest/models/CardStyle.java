package org.jfantasy.pay.rest.models;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.jfantasy.pay.bean.Styles;

public class CardStyle {
    private String id;
    @JsonUnwrapped
    private Styles styles;

    public CardStyle(String key, Styles styles) {
        this.id = key;
        this.styles = styles;
    }

    public String getId() {
        return id;
    }

    public Styles getStyles() {
        return styles;
    }

}
