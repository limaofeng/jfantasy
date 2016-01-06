package org.jfantasy.website.bean;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.website.IPageItem;
import org.jfantasy.website.exception.SwpException;
import org.jfantasy.website.service.PageItemBeanService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class PageItemBean implements IPageItem {

    PageItemBeanService pageItemBeanService = SpringContextUtil.getBeanByType(PageItemBeanService.class);

    private PageItem pageItem;

    @Override
    public void refash() throws SwpException, IOException {
        this.pageItemBeanService.refash(this.pageItem.getId());
    }

    @Override
    public void refash(List<Data> datas) throws SwpException {

    }

    public PageItem getPageItem() {
        return pageItem;
    }

    public void setPageItem(PageItem pageItem) {
        this.pageItem = pageItem;
    }
}
