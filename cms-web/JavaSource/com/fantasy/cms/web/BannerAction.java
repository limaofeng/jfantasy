package com.fantasy.cms.web;

import com.fantasy.cms.bean.Banner;
import com.fantasy.cms.service.BannerService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 轮播图Action
 *
 * @author li_huang
 */
public class BannerAction extends ActionSupport {

    private static final long serialVersionUID = 3799834983783507214L;

    @Autowired
    private transient BannerService bannerService;

    /**
     * 搜索
     *
     * @param pager
     * @param filters
     * @return
     */
    public String search(Pager<Banner> pager, List<PropertyFilter> filters) {
        this.attrs.put(ROOT, bannerService.findPager(pager, filters));
        return SUCCESS;
    }

    /**
     * 保存
     *
     * @param banner
     * @return
     */
    public String create(Banner banner) {
        this.attrs.put(ROOT, bannerService.save(banner));
        return SUCCESS;
    }

    /**
     * 更新
     *
     * @param banner
     * @return
     */
    public String update(Banner banner) {
        this.attrs.put(ROOT, bannerService.save(banner));
        return SUCCESS;
    }

    /**
     * 根据ID查询对象
     *
     * @param id
     * @return
     */
    public String view(String id) {
        this.attrs.put(ROOT, this.bannerService.get(id));
        return SUCCESS;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */

    public String delete(String... id) {
        this.bannerService.delete(id);
        return SUCCESS;
    }

}