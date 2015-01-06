package com.fantasy.wx.framework.message.user;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户列表对象
 */
public class OpenIdList {

    /**
     * 关注该公众账号的总用户数
     */
    private int total = -1;
    /**
     * 拉取的OPENID个数，最大值为10000
     */
    private int count = -1;
    /**
     * 列表数据，OPENID的列表
     */
    private List<String> openIds = new ArrayList<String>();
    /**
     * 拉取列表的后一个用户的OPENID
     */
    private String nextOpenId;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getOpenIds() {
        return openIds;
    }

    public void setOpenIds(List<String> openIds) {
        this.openIds = openIds;
    }

    public String getNextOpenId() {
        return nextOpenId;
    }

    public void setNextOpenId(String nextOpenId) {
        this.nextOpenId = nextOpenId;
    }
}
