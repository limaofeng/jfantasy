package com.fantasy.attr.web;

import com.fantasy.attr.bean.Attribute;
import com.fantasy.attr.bean.AttributeVersion;
import com.fantasy.attr.service.AttributeService;
import com.fantasy.attr.service.AttributeVersionService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.StringUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class AttributeAction extends ActionSupport {

	private static final long serialVersionUID = -3032805252418268707L;
	@Resource
	private transient AttributeService attributeService;

    @Resource
    private transient AttributeVersionService versionService;
	
	/**
	 * 首页
	 * @param pager
	 * @param filters
	 * @return
	 */
	public String index(Pager<Attribute> pager, List<PropertyFilter> filters) {
        Long vId =null;
        if(request.getParameter("vId")!=null) {
            vId = Long.valueOf(request.getParameter("vId"));

            AttributeVersion version = this.versionService.get(vId);

            if(version.getAttributes()!=null){
                for(Attribute attr:version.getAttributes()){
                    filters.add(new PropertyFilter("NEL_id",attr.getId()+""));
                }
            }

        }
        this.search(pager, filters);
		this.attrs.put("pager", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}
	/**
	 * 查询属性
	 * @param pager
	 * @param filters
	 * @return
	 */
	public String search(Pager<Attribute> pager, List<PropertyFilter> filters) {
		this.attrs.put(ROOT, attributeService.findPager(pager, filters));
		return JSONDATA;
	}
	/**
	 * 保存属性
	 * @param attribute
	 * @return
	 */
	public String save(Attribute attribute) {
        Long vId =null;
        if(request.getParameter("vId")!=null) {
           vId = Long.valueOf(request.getParameter("vId"));
        }
		this.attrs.put(ROOT, attributeService.save(attribute));
        //临时属性
        if(vId!=null){
            AttributeVersion version = this.versionService.get(vId);
            List<Attribute> attributes =version.getAttributes();

            if(attributes==null){
                attributes = new ArrayList<Attribute>();
            }
            //添加临时属性 到版本号中
            attributes.add(attribute);
            version.setAttributes(attributes);
            //保存版本
            if(StringUtil.isBlank(version.getAttributeSort())){
                version.setAttributeSort(attribute.getId()+"");
            }else{
                version.setAttributeSort(version.getAttributeSort()+","+attribute.getId());
            }
            this.versionService.save(version);
        }
		return JSONDATA;
	}
	/**
	 * 编辑
	 * @param id
	 * @return
	 */
	public String edit(Long id) {
		this.attrs.put("attribute", attributeService.get(id));
		return SUCCESS;
	}
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public String delete(Long[] ids) {
		this.attributeService.delete(ids);
		return JSONDATA;
	}

}
