package com.fantasy.attr.service;

import com.fantasy.attr.bean.Converter;
import com.fantasy.attr.dao.ConverterDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class ConverterService{

	private static final Log logger = LogFactory.getLog(ConverterService.class);

	@Resource
	private ConverterDao converterDao;

    /**
     * 分页查询转换器
     * @param pager
     * @param filters
     * @return
     */
    public Pager<Converter> findPager(Pager<Converter> pager, List<PropertyFilter> filters) {
        return this.converterDao.findPager(pager, filters);
    }

    public Converter save(Converter converter) {
        this.converterDao.save(converter);
        return converter;
    }

    public Object get(Long id) {
        return this.converterDao.get(id);
    }

    public void delete(Long[] ids) {
        for(long id : ids){
            this.converterDao.delete(id);
        }
    }

    public List<Converter> find(){
        return this.converterDao.find();
    }

    public static List<Converter> findAll() {
        ConverterService service = SpringContextUtil.getBeanByType(ConverterService.class);
        return service.find();
    }
}
