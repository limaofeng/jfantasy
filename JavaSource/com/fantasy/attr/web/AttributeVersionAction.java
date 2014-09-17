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

public class AttributeVersionAction extends ActionSupport {

	private static final long serialVersionUID = -3032805252418268707L;
	@Resource
	private AttributeVersionService versionService;

    @Resource
    private AttributeService attributeService;

	/**
	 * 首页
	 * @param pager
	 * @param filters
	 * @return
	 */
	public String index(Pager<AttributeVersion> pager, List<PropertyFilter> filters) {
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
	public String search(Pager<AttributeVersion> pager, List<PropertyFilter> filters) {
		this.attrs.put(ROOT, versionService.findPager(pager, filters));
		return JSONDATA;
	}
	/**
	 * 保存属性
	 * @param attribute
	 * @return
	 */
	public String save(AttributeVersion attribute) {
		this.attrs.put(ROOT, versionService.save(attribute));
		return JSONDATA;
	}
	/**
	 * 编辑
	 * @param id
	 * @return
	 */
	public String edit(Long id) {
        AttributeVersion version =versionService.get(id);
		this.attrs.put("version", version);
		return SUCCESS;
	}
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public String delete(Long[] ids) {
		this.versionService.delete(ids);
		return JSONDATA;
	}

    /**
     *添加非临时属性
     * @param vid 版本id
     * @param id 属性id
     * @return
     */
    public String attrSave(Long vid,Long id){
        //版本
        AttributeVersion version = this.versionService.get(vid);
        List<Attribute> attributes = version.getAttributes();
        if(attributes==null){
            attributes=new ArrayList<Attribute>();
        }
        //属性
        Attribute attr= this.attributeService.get(id);
        //添加
        attributes.add(attr);
        //版本里面添加属性
        version.setAttributes(attributes);
        //保存版本
        if(StringUtil.isBlank(version.getAttributeSort())){
            version.setAttributeSort(attr.getId()+"");
        }else{
            version.setAttributeSort(version.getAttributeSort()+","+attr.getId());
        }
        this.versionService.save(version);
        return JSONDATA;
    }

    /**
     *移除属性
     * @param vid 版本id
     * @param id 属性id
     * @return
     */
    public String attrDel(Long vid,Long id){
        //版本
        AttributeVersion version = this.versionService.get(vid);
        List<Attribute> attributes = version.getAttributes();
        //属性
        Attribute attr= this.attributeService.get(id);
        //移除
        attributes.remove(attr);
        //版本里面添加属性
        version.setAttributes(attributes);

        int index =version.getAttributeSort().indexOf(attr.getId()+"");

        if(index==0){
            if(version.getAttributeSort().indexOf(",")==-1){
                version.setAttributeSort(null);
            }else{
                version.setAttributeSort(version.getAttributeSort().replace(attr.getId()+",",""));
            }
        }else{
            version.setAttributeSort(version.getAttributeSort().replace(","+attr.getId(),""));
        }
        //保存版本
        this.versionService.save(version);

        //移除临时属性
        if(!attr.getNotTemporary()){
            this.attributeService.delete(new Long[]{attr.getId()});
        }
        return JSONDATA;
    }

    public String attrUpOrDown(Long id,String attrIds){
        AttributeVersion version = this.versionService.get(id);

        version.setAttributeSort(attrIds);
        //保存版本
        this.versionService.save(version);
        return JSONDATA;
    }

}
