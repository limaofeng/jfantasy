package com.fantasy.system.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.system.bean.WebAccessLog;
import com.fantasy.system.service.WebAccessLogService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hebo on 2014/8/7.
 */
public class WebAccessLogAction extends ActionSupport {

    @Resource
    private WebAccessLogService webAccessLogService;

    public String index(Pager<WebAccessLog> pager,List<PropertyFilter> filters){
        this.search(pager, filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    public String search(Pager<WebAccessLog> pager, List<PropertyFilter> filters) {
        if (StringUtil.isBlank(pager.getOrderBy())) {
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        this.attrs.put(ROOT, webAccessLogService.findPager(pager, filters));
        return JSONDATA;
    }

    public String delete(Long[] ids){
        this.webAccessLogService.delete(ids);
        return JSONDATA;
    }


    public String access(){
        this.attrs.put("browser",this.webAccessLogService.queryBrowser());
        this.attrs.put("num",this.webAccessLogService.queryNum());
        this.attrs.put("ipnum",this.webAccessLogService.queryIpClickNum());
        return SUCCESS;
    }
}
