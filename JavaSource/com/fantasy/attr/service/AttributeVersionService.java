package com.fantasy.attr.service;

import com.fantasy.attr.bean.Attribute;
import com.fantasy.attr.bean.AttributeVersion;
import com.fantasy.attr.dao.AttributeVersionDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AttributeVersionService {

    @Resource
    private AttributeVersionDao attributeVersionDao;

    public List<AttributeVersion> search(List<PropertyFilter> filters, String orderBy, String order, int size) {
        return this.attributeVersionDao.find(filters, orderBy, order, 0, size);
    }

    public Pager<AttributeVersion> findPager(Pager<AttributeVersion> pager, List<PropertyFilter> filters) {
        return attributeVersionDao.findPager(pager, filters);
    }

    public AttributeVersion save(AttributeVersion version) {
        this.attributeVersionDao.save(version);
        return version;
    }

    public AttributeVersion get(Long id){
        AttributeVersion version = this.attributeVersionDao.get(id);
        ObjectUtil.sort(version.getAttributes(), "id", "asc");
        return version;
    }

    public void delete(Long[] ids){
        for(Long id:ids){
            this.attributeVersionDao.delete(id);
        }
    }

    public static AttributeVersion version(Long id){
        AttributeVersionService service = SpringContextUtil.getBeanByType(AttributeVersionService.class);
        return service.get(id);
    }


    /**
     * 获取class 原有属性及版本属性 名称
     * @param className
     * @return
     */
    public static List<String> classAllAttrs(Long id,String className){
        List<String> classAttrs= new ArrayList<String>();
        AttributeVersionService service = SpringContextUtil.getBeanByType(AttributeVersionService.class);
        //版本属性
        AttributeVersion version = service.get(id);
        if(version.getAttributes()!=null){
            for(Attribute attr:version.getAttributes()){
                classAttrs.add(attr.getCode());
            }
        }
       //class 原有属性
       String[] classAttrArray = ObjectUtil.toFieldArray(ClassUtil.getPropertys(ClassUtil.forName(className)),"name",String.class);
       if(classAttrArray!=null){
           for(String name:classAttrArray){
               classAttrs.add(name);
           }
       }
       return classAttrs;
    }



}
