package com.fantasy.mall.goods.ws.dto;

import com.fantasy.framework.ws.util.PagerResult;

/**
 * 商品翻页结果集
 */
public class GoodsPagerResult extends PagerResult<GoodsDTO> {

    private GoodsDTO[] pageItems;

    @Override
    public GoodsDTO[] getPageItems() {
        return this.pageItems;
    }

    @Override
    public void setPageItems(GoodsDTO[] pageItems) {
        this.pageItems = pageItems;
    }
}
