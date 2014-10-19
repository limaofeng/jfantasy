package com.fantasy.cms.web.tags;

import com.fantasy.cms.web.tags.dao.HibernateUtil;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.framework.util.common.ClassUtil;
import freemarker.core.Environment;
import freemarker.template.*;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DaoQueryDirective implements TemplateDirectiveModel {

	/**
	 * 
	 * @功能描述 <br/>
	 *       script : <br/>
	 *       type : 可选值:sql,hql,filter<br/>
	 *       clazz: <br/>
	 * @param env
	 * @param params
	 * @param loopVars
	 * @param body
	 * @throws freemarker.template.TemplateException
	 * @throws java.io.IOException
	 */
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		String script = DirectiveUtil.getStringParameter("script", params);//查询预计
		String type = DirectiveUtil.getStringParameter("type", params);//查询类型
		Integer start= DirectiveUtil.getIntegerParameter("start", params);//从第几条开始，查询
		Integer size = DirectiveUtil.getIntegerParameter("size", params);//返回结果条数
		Class<?> clazz = ClassUtil.forName(DirectiveUtil.getStringParameter("class", params));//查询对象
		List list = null;
		HibernateDao dao = HibernateUtil.getHibernateDao(clazz, null);
		if ("sql".equals(type) || "hql".equals(type)) {
			Query query = null;
			if ("sql".equals(type)) {
				query = dao.createSQLQuery(script);
				((SQLQuery) query).addEntity(clazz);
			} else{/* if ("hql".equals(type)) */
				query = dao.createQuery(script);
			}
			query.setMaxResults(size);
			if(start!=null){
				query.setFirstResult(start);
			}
			list = query.list();
		}
		if (body != null && list != null) {
			if (loopVars.length > 0) {
				loopVars[0] = ObjectWrapper.BEANS_WRAPPER.wrap(list);
			}
			body.render(env.getOut());
		}
	}

}
