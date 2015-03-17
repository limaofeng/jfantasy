package com.fantasy.system.web;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.system.bean.Website;
import com.fantasy.system.service.WebsiteService;

import javax.annotation.Resource;

public class WebsiteAction extends ActionSupport {

    private static final long serialVersionUID = 8465525245080349916L;

    @Autowired
    private transient WebsiteService websiteService;

    /**
     * 首页
     *
     * @return
     */
    public String index() {
        this.search();
        this.attrs.put("list", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 搜索
     *
     * @return
     */
    public String search() {
        this.attrs.put(ROOT, websiteService.getAll());
        return JSONDATA;
    }

    /**
     * 保存
     *
     * @param website
     * @return
     */
    public String save(Website website) {
        this.attrs.put(ROOT, websiteService.save(website));
        return JSONDATA;
    }

    /**
     * 修改
     *
     * @param id
     * @return
     */
    public String view(Long id) {
        this.attrs.put(ROOT, this.websiteService.get(id));
        return JSONDATA;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    public String delete(Long[] ids) {
        this.websiteService.delete(ids);
        return JSONDATA;
    }

}
