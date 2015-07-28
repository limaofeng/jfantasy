package com.fantasy.common.service;

import com.fantasy.common.bean.Area;
import com.fantasy.common.dao.AreaDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class AreaService{

    @Autowired
    private AreaDao areaDao;

    @SuppressWarnings("unchecked")
    @CacheEvict(value = {"fantasy.common.AreaService"}, allEntries = true)
    public Area save(Area area) {
        if (area.getParent() == null || StringUtil.isBlank(area.getParent().getId())) {// 如果新增在顶级
            area.setPath(area.getId());
        } else {
            Area parent = this.areaDao.get(area.getParent().getId());// 查询上级
            area.setPath(parent.getPath() + Area.PATH_SEPARATOR + area.getId());// 设置path
        }
        area.setLayer(ObjectUtil.defaultValue(area.getPath().split(Area.PATH_SEPARATOR).length, 1) - 1);// 设置层级
        StringBuilder displayName = new StringBuilder();
        List<Area> parentAreas = !area.getPath().contains(Area.PATH_SEPARATOR) ? Collections.EMPTY_LIST : this.areaDao.find(Restrictions.in("id", StringUtils.substringBeforeLast(area.getPath(), Area.PATH_SEPARATOR).split(Area.PATH_SEPARATOR)));
        if (!parentAreas.isEmpty()) {
            for (Area parentArea : parentAreas) {
                displayName.append(parentArea.getName());
            }
        }
        displayName.append(area.getName());
        area.setDisplayName(displayName.toString());// 设置完整地区名称
        if (area.getSort() == null) {
            area.setSort(this.areaDao.getSortMax() + 1);
        } else {
            Area areaSort = this.areaDao.findUniqueBy("sort", area.getSort());
            if (areaSort != null && !areaSort.getId().equals(area.getId())) {
                areaSort.setSort(this.areaDao.getSortMax() + 1);
                this.areaDao.save(areaSort);
            }
        }
        return this.areaDao.save(area);// 保存
    }

    public Area get(String id) {
        return this.areaDao.get(id);
    }

    /**
     * 获取全部的地区
     *
     * @return
     */
    @Cacheable(value = "fantasy.common.AreaService", key = "'allAreas'")
    public List<Area> allAreas() {
        return this.areaDao.getAll();
    }

    /**
     * 分页
     *
     * @param pager
     * @param filters
     * @return
     */
    public Pager<Area> findPager(Pager<Area> pager, List<PropertyFilter> filters) {
        return this.areaDao.findPager(pager, filters);
    }

    @CacheEvict(value = {"fantasy.common.AreaService"}, allEntries = true)
    public void delete(String[] ids) {
        for (String id : ids) {
            this.areaDao.delete(id);
        }
    }

    /**
     * 查询所有区域
     *
     * @return
     */
    public List<Area> find(List<PropertyFilter> filters) {
        return this.areaDao.find(filters);
    }

    /**
     * 返回地区查询列表
     *
     * @return
     */
    public static List<Area> list(String parentId) {
        AreaService areaService = SpringContextUtil.getBeanByType(AreaService.class);
        List<Area> areas = new ArrayList<Area>();
        for (Area area : areaService.allAreas()) {
            if (StringUtil.isNotBlank(parentId) && area.getParent() != null && parentId.equals(area.getParent().getId())) {
                areas.add(area);
            } else if (StringUtil.isBlank(parentId) && StringUtil.isNull(area.getParent())) {
                areas.add(area);
            }
        }
        return areas;
    }

}
