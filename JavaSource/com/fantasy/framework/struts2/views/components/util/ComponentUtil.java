package com.fantasy.framework.struts2.views.components.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.framework.util.regexp.RegexpUtil.Group;

public class ComponentUtil {

	public static String filterFindValueExpr(String expr) {
		return expr;
	}

	@Deprecated
	public static String oldFilterFindValueExpr(String expr) {
		if (RegexpUtil.find(expr.trim(), "^true$", "^false$", "^null$")) {// 不过滤关键字
			return expr;
		}
		expr = StringUtil.trim(expr);
		if (RegexpUtil.find(expr, "\\%\\{[^\\}]+\\}")) {
			expr = RegexpUtil.replace(expr, "[^\\%\\{^\\}]+", new RegexpUtil.AbstractReplaceCallBack() {
				@Override
				public String doReplace(String text, int index, Matcher matcher) {
					return ComponentUtil.oldFilterFindValueExpr(text);
				}
			});
		} else if (expr.startsWith("@")) {
			if (RegexpUtil.find(expr, "\\(([^\\(^\\)]+)\\)")) {
				expr = RegexpUtil.replace(expr, "\\(([^\\(^\\)]+)\\)", new RegexpUtil.AbstractReplaceCallBack() {
					@Override
					public String doReplace(String text, int index, Matcher matcher) {
						String[] params = $(1).split("\\,");
						for (int i = 0; i < params.length; i++) {
							String param = params[i];
							params[i] = ComponentUtil.oldFilterFindValueExpr(param);
						}
						return "(" + ObjectUtil.toString(params, ",") + ")";
					}
				});
			}
		} else if (RegexpUtil.find(expr, "(([?])|(>=)|(<=)|(==)|(\\!=)|(\\|\\|)|(\\&\\&)|[><])")) {// 支持运算符拆分
			String tempExpr = "";
			List<String> exps = new ArrayList<String>();
			for (Group group : RegexpUtil.parseGroups(expr, "(([?])|(>=)|(<=)|(==)|(\\!=)|(\\|\\|)|(\\&\\&)|[><])")) {
				exps.add(group.$(0));
			}
			String[] splits = RegexpUtil.split(expr, "(([?])|(>=)|(<=)|(==)|(\\!=)|(\\|\\|)|(\\&\\&)|[><])");
			for (int i = 0; i < splits.length; i++) {
				tempExpr += (oldFilterFindValueExpr(splits[i]) + (exps.size() > i ? exps.get(i) : ""));
			}
			expr = tempExpr;
		} else if (!expr.startsWith("#") && !(expr.startsWith("'") || expr.endsWith("'")) && !(expr.startsWith("\"") || expr.endsWith("\"")) && !RegexpUtil.isMatch(expr, "^\\d{1,}$")) {
			expr = "#request." + expr;
		}
		return expr;
	}

}
