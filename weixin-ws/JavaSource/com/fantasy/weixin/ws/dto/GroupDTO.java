package com.fantasy.weixin.ws.dto;

/**
 * 微信用用户组
 * Created by zzzhong on 2014/6/19.
 */
public class GroupDTO {
    public GroupDTO() {
    }

    public GroupDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private Long id;
    private String name;
    private Long count;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
