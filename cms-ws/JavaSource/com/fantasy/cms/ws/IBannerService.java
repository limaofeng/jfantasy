package com.fantasy.cms.ws;

import com.fantasy.cms.ws.dto.BannerItemDTO;

/**
 * 自定义广告位接口
 * 
 * @author 李茂峰
 * @since 2014-4-17 下午5:04:57
 * @version 1.0
 */
public interface IBannerService {

	/**
	 * 获取广告位对应的详细项列表
	 * 
	 * @param key
	 * @return
	 */
	public BannerItemDTO[] getBannerItemsByKey(String key);

}
