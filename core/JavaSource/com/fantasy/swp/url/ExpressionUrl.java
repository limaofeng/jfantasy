package com.fantasy.swp.url;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.swp.PageUrl;
import com.fantasy.swp.data.SimpleData;
import com.fantasy.swp.util.DataMap;

import java.util.Map;
import java.util.regex.Matcher;

/**
 * 可以通过表达式从数据中生成url
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-3-28 下午02:40:33
 * @version 1.0
 */
public class ExpressionUrl implements PageUrl {

	private String url;

	public ExpressionUrl(String url) {
		this.url = url;
	}

	public ExpressionUrl() {
	}

	public String getUrl(final Map<String, Object> data) {
		return RegexpUtil.replace(url, "\\$\\{([a-zA-z0-9_]+)\\}", new RegexpUtil.AbstractReplaceCallBack() {

			@Override
			public String doReplace(String text, int index, Matcher matcher) {
				Object val = OgnlUtil.getInstance().getValue($(1), data);
				return StringUtil.defaultValue(val, "");
			}

		});
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static void main(String[] args) {
		DataMap data = new DataMap();
		data.add(SimpleData.getData("contextPath", "/front"));
		data.add(SimpleData.getData("name", "lmf"));

		ExpressionUrl pageUrl = new ExpressionUrl();
		pageUrl.setUrl("/${name}.html");
		/*System.out.println(pageUrl.getUrl(data));*/
	}

}
