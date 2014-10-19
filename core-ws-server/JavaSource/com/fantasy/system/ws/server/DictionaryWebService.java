package com.fantasy.system.ws.server;

import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.ws.util.WebServiceUtil;
import com.fantasy.system.service.DataDictionaryService;
import com.fantasy.system.ws.IDictionaryService;
import com.fantasy.system.ws.dto.ConfigDTO;
import com.fantasy.system.ws.dto.ConfigTypeDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DictionaryWebService implements IDictionaryService {

	@Resource
	private DataDictionaryService dataDictionaryService;

	@Override
	public ConfigTypeDTO[] allTypes() {
		return WebServiceUtil.toArray(dataDictionaryService.allTypes(), ConfigTypeDTO.class);
	}

	@Override
	public ConfigDTO getUnique(String type, String code) {
		return WebServiceUtil.toBean(dataDictionaryService.getUnique(type, code),ConfigDTO.class);
	}

	@Override
	public ConfigDTO[] list(String configKey) {
		String[] ck = StringUtil.tokenizeToStringArray(configKey, ":");
		return WebServiceUtil.toArray(dataDictionaryService.list(ck[0], ck.length > 1 ? ck[1] : null), ConfigDTO.class);
	}

}
