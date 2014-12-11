package com.fantasy.cms.ws.server;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.ws.util.PagerDTO;

/**
 * Created by hebo on 2014/11/26.
 */
public class PagerUitl {

    private PagerUitl(){
    }

    public static <T>Pager<T> statusPager(PagerDTO pagerDTO,Pager<T> pager){
        pager.setPageSize(pagerDTO.getPageSize());
        pager.setOrderBy(pagerDTO.getOrderBy());
        pager.setCurrentPage(pagerDTO.getCurrentPage());
        pager.setOrder(pagerDTO.getOrder());
        return  pager;
    }
}
