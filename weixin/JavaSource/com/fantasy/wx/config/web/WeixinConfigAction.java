package com.fantasy.wx.config.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.wx.config.bean.WeixinConfig;
import com.fantasy.wx.config.service.IConfigService;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 微信app配置
 *
 * @author 钟振振
 * @version 1.0
 * @功能描述 管理配置
 * @since 2014-09-19 上午17:36:52
 */
public class WeixinConfigAction extends ActionSupport {


    @Resource
    private IConfigService iConfigService;

    public String index() {
        this.search(new Pager<WeixinConfig>(), new ArrayList<PropertyFilter>());
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    public String search(Pager<WeixinConfig> pager, List<PropertyFilter> filters) {
        if (StringUtil.isNotBlank(pager.getOrderBy())) {
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        pager = iConfigService.findPager(pager, filters);
        this.attrs.put(ROOT, pager);
        return JSONDATA;
    }

    public String save(final WeixinConfig at) throws JobExecutionException {
        this.attrs.put(ROOT, iConfigService.save(at));
        return JSONDATA;
    }

    public String edit(String id) {
        WeixinConfig at = iConfigService.get(id);
        this.attrs.put("at", at);
        return SUCCESS;
    }

    public String delete(String... ids) {
        this.iConfigService.delete(ids);
        return JSONDATA;
    }

}
