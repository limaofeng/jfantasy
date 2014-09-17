package ${package};

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ${formBeanClass};
import ${serviceClass};
import ${beanClass};
import aia.cn.isp.core.dao.Pagination;

<#assign pk = table.primaryKey>
<#assign pkName = util.toJavaName(pk.columnName)>
@Controller
@RequestMapping("/${util.lowerCaseFirst(util.toSimpleJavaType(table.simpleName))}")
public class ${table.simpleName}Controller {

	@Resource
	private ${util.toSimpleJavaType(serviceClass)} ${util.lowerCaseFirst(util.toSimpleJavaType(serviceClass))};

	public ModelAndView list${table.simpleName}(Pagination<${util.toSimpleJavaType(beanClass)}> page,HttpServletRequest request){
		ModelAndView view = new ModelAndView();
		//TODO 功能待实现
		return view;
	}

	public ModelAndView add${table.simpleName}(${util.toSimpleJavaType(formBeanClass)} ${util.lowerCaseFirst(util.toSimpleJavaType(formBeanClass))}){
		ModelAndView view = new ModelAndView();
		//TODO 功能待实现
		return view;
	}
	
	public ModelAndView edit${table.simpleName}(${util.toSimpleJavaType(util.toJavaType(pk.getDataType()))} ${pkName}){
		ModelAndView view = new ModelAndView();
		//TODO 功能待实现
		return view;
	}
	
	public ModelAndView update${table.simpleName}(${util.toSimpleJavaType(formBeanClass)} ${util.lowerCaseFirst(util.toSimpleJavaType(formBeanClass))}){
		ModelAndView view = new ModelAndView();
		//TODO 功能待实现
		return view;
	}
	
	public ModelAndView del${table.simpleName}(${util.toSimpleJavaType(util.toJavaType(pk.getDataType()))} ${pkName}){
		ModelAndView view = new ModelAndView();
		//TODO 功能待实现
		return view;
	}

}
