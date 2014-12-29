package com.fantasy.wx.message.user;

/**
 * 用户分组
 */
public class Group {
    private long id = -1L;
    private String name;
    private long count;

    public Group(long id, String name, long count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
