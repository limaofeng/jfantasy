package com.fantasy.axis.web;

import com.fantasy.axis.bean.Message;
import com.fantasy.axis.service.MessageService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yhx on 2014/12/23.
 * webService 接口 action
 */
public class MessageAction extends ActionSupport {

    @Resource(name="axis.MessageService")
    private MessageService messageService;

    /**
     * 首页 （列表页）
     * @param pager
     * @param filters
     * @return
     */
    public String index(Pager<Message> pager,List<PropertyFilter> filters){
        this.search(pager,filters);
        this.attrs.put("pager",this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 查询方法
     * @param pager
     * @param filters
     * @return
     */
    public String search(Pager<Message> pager,List<PropertyFilter> filters){
        this.attrs.put(ROOT, this.messageService.findPager(pager, filters));
        return JSONDATA;
    }

    /**
     * 详情界面
     * @param id
     * @return
     */
    public String view(String id){
       this.attrs.put("message",this.messageService.get(id));
       return SUCCESS;
    }

    /**
     * 删除消息
     * @param ids
     * @return
     */
    public String delete(String... ids){
        this.messageService.delete(ids);
        return JSONDATA;
    }


}
