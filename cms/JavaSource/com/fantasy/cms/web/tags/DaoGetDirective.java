package com.fantasy.cms.web.tags;

import com.fantasy.cms.web.tags.dao.HibernateUtil;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.framework.util.common.ClassUtil;
import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

public class DaoGetDirective implements TemplateDirectiveModel {

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String id = DirectiveUtil.getStringParameter("id", params);
		Class<?> clazz = ClassUtil.forName(DirectiveUtil.getStringParameter("class", params));

		HibernateDao dao = HibernateUtil.getHibernateDao(clazz, null);
		Field field = ClassUtil.getDeclaredField(clazz, dao.getIdName());
		Object bean = dao.get((Serializable) ClassUtil.newInstance(field.getType(), new Class[]{String.class},new Object[]{id}));
		if (body != null && bean != null) {
			if (loopVars.length > 0) {
				loopVars[0] = ObjectWrapper.BEANS_WRAPPER.wrap(bean);
			}
			body.render(env.getOut());
		}
	}

}
