package com.fantasy.swp;

import java.util.Map;

/**
 * 生成地址接口
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-3-28 下午02:47:11
 * @version 1.0
 */
public interface PageUrl {

	public String getUrl(Map<String, Object> data);

	public void setUrl(String value);

}
