package com.fantasy.attr.service;

import com.fantasy.attr.bean.Converter;
import com.fantasy.attr.dao.ConverterDao;
import com.fantasy.attr.typeConverter.UserIdTypeConverter;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.security.bean.User;
import ognl.TypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
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

	public static void main(String[] args) {



        TypeConverter typeConverter = (TypeConverter)ClassUtil.newInstance(UserIdTypeConverter.class.getName());

        Object r = typeConverter.convertValue(new HashMap(),null,null,null,"123123",User.class);

        System.out.println(r.getClass());


        /*
		Object obj = ConvertUtils.convert("true", boolean.class);
		
		System.out.println(obj + "-" + obj.getClass());
		
		obj = ConvertUtils.convert("12", long.class);
		
		System.out.println(obj + "-" + obj.getClass());
        Converter converter = new Converter();
        converter.setTypeConverter("double");
        covertIn("15", converter);*/
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
