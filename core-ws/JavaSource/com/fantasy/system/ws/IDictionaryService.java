package com.fantasy.system.ws;

import com.fantasy.system.ws.dto.ConfigDTO;
import com.fantasy.system.ws.dto.ConfigTypeDTO;

/**
 * 数据字典
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2014-4-4 上午11:01:11
 * @version 1.0
 */
public interface IDictionaryService {

	/**
	 * 返回全部的字典分类
	 * 
	 * @功能描述
	 * @return
	 */
	public ConfigTypeDTO[] allTypes();

	/**
	 * 通过字典分类及字段编码返回数据项
	 * 
	 * @功能描述
	 * @param type
	 *            ConfigType.code 数据字段分类的编码
	 * @param code
	 *            Config.code 数据编码
	 * @return
	 */
	public ConfigDTO getUnique(String type, String code);

	/**
	 * 通过字典的 configKey 返回一组数据
	 * 
	 * @功能描述
	 * @param configKey
	 *            configKey的格式为:ConfigType.code+':'+Config.code <br/>
	 *            如果要获取ConfigType.code对应的数据直接传入 ConfigType.code<br/>
	 * @return
	 */
	public ConfigDTO[] list(String configKey);

}
