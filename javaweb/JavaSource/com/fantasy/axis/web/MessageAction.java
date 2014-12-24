package com.fantasy.axis.web;

import com.fantasy.axis.bean.Message;
import com.fantasy.axis.service.MessageService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhx on 2014/12/23.
 * webService 接口 action
 */
public class MessageAction extends ActionSupport {

    @Resource(name="axis.MessageService")
    private MessageService messageService;

    public String index(){
        this.search(new Pager<Message>(),new ArrayList<PropertyFilter>());
        this.attrs.put("pager",this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    public String search(Pager<Message> pager,List<PropertyFilter> filters){
        this.attrs.put(ROOT, this.messageService.findPager(pager, filters));
        return JSONDATA;
    }

    public String view(String id){
       this.attrs.put("message",this.messageService.get(id));
       return SUCCESS;
    }


}
