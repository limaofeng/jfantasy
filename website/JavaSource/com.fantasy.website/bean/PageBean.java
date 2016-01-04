package org.jfantasy.website.bean;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.website.IPage;
import org.jfantasy.website.IPageItem;
import org.jfantasy.website.exception.SwpException;
import org.jfantasy.website.service.PageBeanService;

import java.util.List;

public class PageBean implements IPage {

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
        return this.pageBeanService.getPageItem(path, this.page.getId());
    }
}
