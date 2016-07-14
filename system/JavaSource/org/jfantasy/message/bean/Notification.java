package org.jfantasy.message.bean;

import org.jfantasy.framework.dao.BaseBusEntity;

import java.util.Date;

/**
 *  通知
 */
public class Notification extends BaseBusEntity{

    private String title;

    private String description;

    private Date time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
