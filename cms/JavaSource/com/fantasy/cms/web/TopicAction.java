package com.fantasy.cms.web;

import com.fantasy.cms.bean.Topic;
import com.fantasy.cms.service.TopicService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * 专题Action
 */
public class TopicAction extends ActionSupport {

    @Resource
    private TopicService topicService;

    /**
     * 用于显示列表页面
     *
     * @param pager   翻页对象
     * @param filters 筛选条件
     * @return struts 返回码
     */
    public String index(Pager<Topic> pager, List<PropertyFilter> filters) {
        this.search(pager,filters);
        this.attrs.put("pager",this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

    /**
     * 用于异步查询
     *
     * @param pager   翻页对象
     * @param filters 筛选条件
     * @return struts 返回码
     */
    public String search(Pager<Topic> pager, List<PropertyFilter> filters) {
        this.attrs.put(ROOT,this.topicService.findPager(pager,filters));
        return JSONDATA;
    }

    /**
     * 根据 id 查询对应的专题，一般用于查看或者编辑
     *
     * @param code 主键
     * @return struts 返回码
     */
    public String view(String code) {
        this.attrs.put("topic",this.topicService.get(code));
        return SUCCESS;
    }

    /**
     * 保存专题
     *
     * @param topic 专题对象
     * @return struts 返回码
     */
    public String save(Topic topic) {
        this.topicService.save(topic);
        return JSONDATA;
    }

    /**
     * @param codes 主键数组
     * @return struts 返回码
     */
    public String delete(String... codes) {
        this.topicService.delete(codes);
        return SUCCESS;
    }

}
