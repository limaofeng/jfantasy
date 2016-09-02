package org.jfantasy.member.rest.models;


public class CommentShowForm {
    private boolean show;
    private String notes;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
