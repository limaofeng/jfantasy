package com.fantasy.remind.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.remind.bean.Notice;
import com.fantasy.remind.service.NoticeService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 公告 action
 */

public class NoticeAction extends ActionSupport {

    @Resource
    private NoticeService noticeService;

    public String index(){
        this.search(new Pager<Notice>(),new ArrayList<PropertyFilter>());
        this.attrs.put("pager",this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    public String search(Pager<Notice> pager,List<PropertyFilter> filters){
        if(pager.getOrderBy()==null){
            pager.setOrderBy("createTime");
            pager.setOrder(Pager.Order.desc);
        }
        this.attrs.put(ROOT,this.noticeService.findPager(pager,filters));
        return JSONDATA;
    }



    public String save(Notice notice){
        Date startDate = null;
        Date endDate = null;
        Long id = notice.getId();
        if(notice.getId()!=null){
            startDate=notice.getStartDate();
            endDate = notice.getEndDate();
        }
        this.noticeService.save(notice);
        if(id!=null){
            if(startDate==null) {
                notice.setStartDate(null);
            }else if(endDate==null){
                notice.setEndDate(null);
            }
            this.noticeService.update(notice);
        }
        return JSONDATA;
    }

    public String edit(Long id){
       this.attrs.put("notice",this.noticeService.get(id));
       return SUCCESS;
    }


    public String view(Long id){
        this.attrs.put("notice",this.noticeService.get(id));
        return SUCCESS;
    }

    public String delete(Long[] ids){
        this.noticeService.delete(ids);
        return JSONDATA;
    }




}
