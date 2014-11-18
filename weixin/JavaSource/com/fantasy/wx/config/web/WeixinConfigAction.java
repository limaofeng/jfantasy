package com.fantasy.wx.config.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.wx.config.bean.WeixinConfig;
import com.fantasy.wx.config.service.WeixinConfigService;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 微信app配置
 *
 * @功能描述 管理配置
 * @author 钟振振
 * @since 2014-09-19 上午17:36:52
 * @version 1.0
 */
public class WeixinConfigAction extends ActionSupport {


    @Resource
    private WeixinConfigService wcService;

    public String index() {
        this.search(new Pager<WeixinConfig>(), new ArrayList<PropertyFilter>());
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }
    public String search(Pager<WeixinConfig> pager, List<PropertyFilter> filters) {
        if(StringUtil.isNotBlank(pager.getOrderBy())){
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        pager = wcService.findPager(pager, filters);
        this.attrs.put(ROOT, pager);
        return JSONDATA;
    }

    public String save(final WeixinConfig at) throws JobExecutionException {
        this.attrs.put(ROOT, wcService.save(at));
        return JSONDATA;
    }
    public String edit(String id) {
        WeixinConfig at = wcService.get(id);
        this.attrs.put("at", at);
        return SUCCESS;
    }
    public String delete(String... ids) {
        this.wcService.delete(ids);
        return JSONDATA;
    }

}
