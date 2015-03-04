package com.fantasy.swp.bean;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.swp.IPage;
import com.fantasy.swp.IPageItem;
import com.fantasy.swp.exception.SwpException;
import com.fantasy.swp.service.PageBeanService;

import java.util.List;

/**
 *
 */
public class PageBean implements IPage{

    private PageBeanService pageBeanService = SpringContextUtil.getBeanByType(PageBeanService.class);

    private Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public List<IPageItem> createPageItems() throws SwpException {
        return this.pageBeanService.execute(this.page);
    }

    @Override
    public void removePageItem() {

    }

    @Override
    public IPageItem getPageItem(String path) {
        return this.pageBeanService.getPageItem(path,this.page.getId());
    }
}
