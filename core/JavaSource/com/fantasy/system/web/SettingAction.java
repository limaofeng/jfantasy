package com.fantasy.system.web;

import java.util.List;

import javax.annotation.Resource;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.system.bean.Setting;
import com.fantasy.system.service.SettingService;

public class SettingAction extends ActionSupport {
	
	private static final long serialVersionUID = 8465525245080349916L;
	
	@Resource
	private transient SettingService settingService;
	
	/**
	 * 首页
	 * 
	 * @return
	 */
	public String index(Pager<Setting> pager, List<PropertyFilter> filters) {
		this.search(pager, filters);
		this.attrs.put("pager", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}

	/**
	 * 搜索
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
	public String search(Pager<Setting> pager, List<PropertyFilter> filters) {
		this.attrs.put(ROOT, settingService.findPager(pager, filters));
		return JSONDATA;
	}

    /**
     * 查询某个网站下面的配置信息
     * @param websiteId
     * @return
     */
    public String add(Long websiteId){
        this.attrs.put("notExitList",this.settingService.queryNotExitSetting(websiteId));
        return SUCCESS;
    }

	/**
	 * 保存
	 * 
	 * @param article
	 * @return
	 */
	public String save(Setting setting) {
		this.attrs.put(ROOT, settingService.save(setting));
		return JSONDATA;
	}

	/**
	 * 修改
	 * 
	 * @param id
	 * @return
	 */
	public String edit(Long id) {
		this.attrs.put("setting", this.settingService.get(id));
		return SUCCESS;
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public String delete(Long[] ids) {
		this.settingService.delete(ids);
		return JSONDATA;
	}

}
