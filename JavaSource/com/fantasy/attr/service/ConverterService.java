package com.fantasy.attr.service;

import com.fantasy.attr.bean.AttributeType;
import com.fantasy.attr.bean.Converter;
import com.fantasy.attr.dao.ConverterDao;
import com.fantasy.attr.typeConverter.PrimitiveTypeConverter;
import com.fantasy.attr.typeConverter.UserIdTypeConverter;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.security.bean.User;
import ognl.TypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class ConverterService implements InitializingBean {

	private static final Log logger = LogFactory.getLog(ConverterService.class);

	@Resource
	private ConverterDao converterDao;

	@Override
	public void afterPropertiesSet() throws Exception {
		PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			// 初始化基础分类
			Class<?>[] defaultClass = new Class[] { PrimitiveTypeConverter.class };
			for (Class<?> clazz : defaultClass) {
				Converter converter = converterDao.findUniqueBy("typeConverter", clazz.getName());
				if (converter == null) {
					StringBuffer log = new StringBuffer("初始化基本数据类型转换器:" + clazz);
					converter = new Converter();
					converter.setName("int converter");
					converter.setTypeConverter(clazz.getName());
					converter.setDescription("基本数据类型转换器:" + clazz);
					converterDao.save(converter);
					logger.debug(log);
				}
			}
		} finally {
			transactionManager.commit(status);
		}
	}

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
