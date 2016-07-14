package org.jfantasy.common.service;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.common.bean.Area;
import org.jfantasy.common.dao.AreaDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class AreaService {

    @Autowired
    private AreaDao areaDao;

    public Area save(Area area) {
        if (area.getParent() == null || StringUtil.isBlank(area.getParent().getId())) {// 如果新增在顶级
            area.setPath(area.getId());
        } else {
            Area parent = this.areaDao.get(area.getParent().getId());// 查询上级
            area.setPath(parent.getPath() + Area.PATH_SEPARATOR + area.getId());// 设置path
        }
        area.setLayer(ObjectUtil.defaultValue(area.getPath().split(Area.PATH_SEPARATOR).length, 1) - 1);// 设置层级
        StringBuilder displayName = new StringBuilder();
        List<Area> parentAreas = !area.getPath().contains(Area.PATH_SEPARATOR) ? Collections.<Area>emptyList() : this.areaDao.find(Restrictions.in("id", StringUtils.substringBeforeLast(area.getPath(), Area.PATH_SEPARATOR).split(Area.PATH_SEPARATOR)));
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
     * 分页
     *
     * @param pager   分页对象
     * @param filters 过滤条件
     * @return Pager<Area>
     */
    public Pager<Area> findPager(Pager<Area> pager, List<PropertyFilter> filters) {
        return this.areaDao.findPager(pager, filters);
    }

    public void delete(String... ids) {
        for (String id : ids) {
            Area area = this.areaDao.get(id);
            if (area == null) {
                continue;
            }
            Area parent = area.getParent();
            //转移子集到本级节点
            if (parent != null) {
                for (Area cren : this.areaDao.find(Restrictions.eq("parent.id", id))) {
                    this.move(cren.getId(), parent.getId());
                }
            }
            //执行删除操作
            this.areaDao.delete(id);
        }
    }

    private void move(String id, String pid) {
        Area area = this.areaDao.get(id);
        Area parent = this.areaDao.get(pid);

        //重新 path / displayName / layer
        area.setParent(parent);
        area.setPath(parent.getPath() + Area.PATH_SEPARATOR + area.getId());// 设置path
        area.setLayer(ObjectUtil.defaultValue(area.getPath().split(Area.PATH_SEPARATOR).length, 1) - 1);// 设置层级
        // 设置完整地区名称
        StringBuilder displayName = new StringBuilder();
        List<Area> parentAreas = !area.getPath().contains(Area.PATH_SEPARATOR) ? Collections.<Area>emptyList() : this.areaDao.find(Restrictions.in("id", StringUtils.substringBeforeLast(area.getPath(), Area.PATH_SEPARATOR).split(Area.PATH_SEPARATOR)));
        if (!parentAreas.isEmpty()) {
            for (Area parentArea : parentAreas) {
                displayName.append(parentArea.getName());
            }
        }
        displayName.append(area.getName());
        area.setDisplayName(displayName.toString());

        for (Area ch : this.areaDao.find(Restrictions.eq("parent.id", id))) {
            this.move(ch.getId(), area.getId());
        }
    }

}
