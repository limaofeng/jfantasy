package com.fantasy.cms.ws.dto;

import com.fantasy.framework.ws.util.PagerResult;


public class ArticlePagerResult extends PagerResult<ArticleDTO> {

    private ArticleDTO[] pageItems;

    public ArticleDTO[] getPageItems() {
        return pageItems;
    }

    public void setPageItems(ArticleDTO[] pageItems) {
        this.pageItems = pageItems;
    }
}
