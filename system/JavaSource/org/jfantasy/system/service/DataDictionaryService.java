package org.jfantasy.system.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.BeanUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.schedule.service.ScheduleService;
import org.jfantasy.system.bean.DataDictionary;
import org.jfantasy.system.bean.DataDictionaryKey;
import org.jfantasy.system.bean.DataDictionaryType;
import org.jfantasy.system.dao.DataDictionaryDao;
import org.jfantasy.system.dao.DataDictionaryTypeDao;
import org.quartz.JobKey;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DataDictionaryService implements InitializingBean {

    private static final Log LOGGER = LogFactory.getLog(DataDictionaryService.class);

    public static final JobKey jobKey = JobKey.jobKey("DataDictionary", "SYSTEM");

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private DataDictionaryTypeDao dataDictionaryTypeDao;

    @Autowired
    private DataDictionaryDao dataDictionaryDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!this.scheduleService.isStartTimerTisk()) {
            LOGGER.error(" scheduler 定时任务未启动！");
            return;
        }
        if (this.scheduleService.checkExists(jobKey)) {
            this.scheduleService.deleteJob(jobKey);
        }
        LOGGER.debug("添加用于生成 json 文件的 Job ");
    }

    public DataDictionary get(DataDictionaryKey key) {
        return dataDictionaryDao.get(key);
    }

    public DataDictionary get(String key) {
        String[] array = key.split(":");
        return dataDictionaryDao.get(DataDictionaryKey.newInstance(array[1], array[0]));
    }

    public DataDictionaryType getDataDictionaryType(String code) {
        return dataDictionaryTypeDao.findUniqueBy("code", code);
    }

    /**
     * 通过分类及上级编码查询配置项
     *
     * @param type       分类
     * @param parentCode 上级编码
     * @return {List}
     */
    public List<DataDictionary> list(String type, String parentCode) {
        Criterion[] criterions = new Criterion[]{Restrictions.eq("type", type)};
        if (StringUtil.isNotBlank(parentCode)) {
            criterions = ObjectUtil.join(criterions, Restrictions.eq("parent.code", parentCode));
        }
        return dataDictionaryDao.find(criterions, "sort", "asc");
    }

    /**
     * 分页查询方法
     *
     * @param pager   分页对象
     * @param filters 过滤条件
     * @return {List}
     */
    public Pager<DataDictionary> findPager(Pager<DataDictionary> pager, List<PropertyFilter> filters) {
        return this.dataDictionaryDao.findPager(pager, filters);
    }

    public Pager<DataDictionaryType> findDataDictionaryTypePager(Pager<DataDictionaryType> pager, List<PropertyFilter> filters) {
        return this.dataDictionaryTypeDao.findPager(pager, filters);
    }

    /**
     * 添加及更新配置项
     *
     * @param dataDictionary 数据字典项
     */
    public DataDictionary save(DataDictionary dataDictionary) {
        return this.dataDictionaryDao.save(dataDictionary);
    }

    public DataDictionary update(DataDictionary dataDictionary) {
        return this.dataDictionaryDao.update(dataDictionary);
    }

    private List<DataDictionaryType> initEntity(DataDictionaryType dataDictionaryType) {
        if (dataDictionaryType.getParent() == null || StringUtil.isBlank(dataDictionaryType.getParent().getCode())) {
            dataDictionaryType.setLayer(0);
            dataDictionaryType.setPath(dataDictionaryType.getCode() + DataDictionaryType.PATH_SEPARATOR);
            return ObjectUtil.sort(dataDictionaryTypeDao.find(Restrictions.isNull("parent")), "sort", "asc");
        } else {
            DataDictionaryType parentCategory = this.dataDictionaryTypeDao.get(dataDictionaryType.getParent().getCode());
            dataDictionaryType.setLayer(parentCategory.getLayer() + 1);
            dataDictionaryType.setPath(parentCategory.getPath() + dataDictionaryType.getCode() + DataDictionaryType.PATH_SEPARATOR);// 设置path
            return ObjectUtil.sort(dataDictionaryTypeDao.findBy("parent.code", parentCategory.getCode()), "sort", "asc");
        }
    }

    /**
     * 添加及更新配置项分类方法
     *
     * @param dataDictionaryType 数据字典分类
     */
    public DataDictionaryType save(DataDictionaryType dataDictionaryType) {
        List<DataDictionaryType> types = initEntity(dataDictionaryType);
        dataDictionaryType.setSort(types.size() + 1);
        dataDictionaryType = this.dataDictionaryTypeDao.save(dataDictionaryType);
        return dataDictionaryType;
    }

    public DataDictionaryType update(DataDictionaryType dataDictionaryType) {
        List<DataDictionaryType> types;
        boolean root = dataDictionaryType.getLayer() == 0;
        if (dataDictionaryType.getParent() == null || StringUtil.isBlank(dataDictionaryType.getParent().getCode())) {
            dataDictionaryType.setLayer(0);
            dataDictionaryType.setPath(dataDictionaryType.getCode() + DataDictionaryType.PATH_SEPARATOR);
            root = true;
            types = ObjectUtil.sort(dataDictionaryTypeDao.find(Restrictions.isNull("parent")), "sort", "asc");
        } else {
            DataDictionaryType parentCategory = this.dataDictionaryTypeDao.get(dataDictionaryType.getParent().getCode());
            dataDictionaryType.setLayer(parentCategory.getLayer() + 1);
            dataDictionaryType.setPath(parentCategory.getPath() + dataDictionaryType.getCode() + DataDictionaryType.PATH_SEPARATOR);// 设置path
            types = ObjectUtil.sort(dataDictionaryTypeDao.findBy("parent.code", parentCategory.getCode()), "sort", "asc");
        }
        DataDictionaryType old = this.dataDictionaryTypeDao.get(dataDictionaryType.getCode());
        if (dataDictionaryType.getSort() != null && (ObjectUtil.find(types, "code", old.getCode()) == null || !old.getSort().equals(dataDictionaryType.getSort()))) {
            if (ObjectUtil.find(types, "code", old.getCode()) == null) {// 移动了节点的层级
                int i = 0;
                for (DataDictionaryType m : ObjectUtil.sort((old.getParent() == null || StringUtil.isBlank(old.getParent().getCode())) ? dataDictionaryTypeDao.find(Restrictions.isNull("parent")) : dataDictionaryTypeDao.findBy("parent.code", old.getParent().getCode()), "sort", "asc")) {
                    m.setSort(i++);
                    this.dataDictionaryTypeDao.update(m);
                }
                types.add(dataDictionaryType.getSort() - 1, dataDictionaryType);
            } else {
                DataDictionaryType t = ObjectUtil.remove(types, "code", old.getCode());
                if (types.size() >= dataDictionaryType.getSort()) {
                    types.add(dataDictionaryType.getSort() - 1, t);
                } else {
                    types.add(t);
                }
            }
            // 重新排序后更新新的位置
            for (int i = 0; i < types.size(); i++) {
                DataDictionaryType m = types.get(i);
                if (m.getCode().equals(dataDictionaryType.getCode())) {
                    continue;
                }
                m.setSort(i + 1);
                this.dataDictionaryTypeDao.update(m);
            }
        }
        old = BeanUtil.copyProperties(old, dataDictionaryType, "children", "id");
        if (root) {
            old.setParent(null);
        }
        return this.dataDictionaryTypeDao.update(old);
    }

    /**
     * 删除配置项
     *
     * @param keys 数据字段 key
     */
    public void delete(DataDictionaryKey... keys) {
        for (DataDictionaryKey key : keys) {
            DataDictionary dd = this.get(key);
            if (dd == null) {
                LOGGER.warn(" 数据字典项 key = " + key + " 不存在 , 请检查方法参数 !");
                continue;
            }
            this.dataDictionaryDao.delete(dd);
        }
    }

    public void delete(String... keys) {
        List<DataDictionaryKey> dataDictionaryKeys = new ArrayList<>();
        for (String key : keys) {
            dataDictionaryKeys.add(new DataDictionaryKey(key));
        }
        this.delete(dataDictionaryKeys.toArray(new DataDictionaryKey[dataDictionaryKeys.size()]));
    }

    public void deleteType(String... codes) {
        for (String code : codes) {
            DataDictionaryType dataDictionaryType = this.dataDictionaryTypeDao.get(code);
            if (dataDictionaryType == null) {
                continue;
            }
            for (DataDictionary dataDictionary : dataDictionaryType.getDataDictionaries()) {
                this.dataDictionaryDao.delete(dataDictionary.getKey());
            }
            this.dataDictionaryTypeDao.delete(dataDictionaryType.getCode());
        }
    }

    public List<DataDictionary> find(Criterion... criterions) {
        return this.dataDictionaryDao.find(criterions);
    }

}
