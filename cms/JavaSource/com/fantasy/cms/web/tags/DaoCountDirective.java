package com.fantasy.cms.web.tags;

import com.fantasy.cms.web.tags.dao.HibernateUtil;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.framework.util.common.ClassUtil;
import freemarker.core.Environment;
import freemarker.template.*;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

import java.io.IOException;
import java.util.Map;

public class DaoCountDirective implements TemplateDirectiveModel {
	
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String script = "select count(*) " + DirectiveUtil.getStringParameter("script", params);//查询预计
		String type = DirectiveUtil.getStringParameter("type", params);//查询类型
		Class<?> clazz = ClassUtil.forName(DirectiveUtil.getStringParameter("class", params));//查询对象
		Long count = null;
		HibernateDao dao = HibernateUtil.getHibernateDao(clazz, null);
		if ("sql".equals(type) || "hql".equals(type)) {
			Query query = null;
			if ("sql".equals(type)) {
				query = dao.createSQLQuery(script);
				((SQLQuery) query).addEntity(clazz);
			} else {/*if ("hql".equals(type)) */
				query = dao.createQuery(script);
			}
			Object obj = query.uniqueResult();
			count = obj == null ? 0L : Long.valueOf(obj.toString());
		} else if ("filter".equals(type)) {

		}
		if (body != null && count != null) {
			if (loopVars.length > 0) {
				loopVars[0] = ObjectWrapper.BEANS_WRAPPER.wrap(count);
			}
			body.render(env.getOut());
		}
	}
}
