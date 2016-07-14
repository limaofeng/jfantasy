package org.jfantasy.pay.rest.models;

import java.io.Serializable;

public class LogForm implements Serializable {

    private String notes;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
