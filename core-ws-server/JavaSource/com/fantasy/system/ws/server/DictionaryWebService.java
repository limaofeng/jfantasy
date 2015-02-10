package com.fantasy.system.ws.server;

import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.system.bean.DataDictionary;
import com.fantasy.system.bean.DataDictionaryType;
import com.fantasy.system.service.DataDictionaryService;
import com.fantasy.system.ws.IDictionaryService;
import com.fantasy.system.ws.dto.DataDictionaryDTO;
import com.fantasy.system.ws.dto.DataDictionaryTypeDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DictionaryWebService implements IDictionaryService {

    @Resource
    private DataDictionaryService dataDictionaryService;

    @Override
    public DataDictionaryTypeDTO[] allTypes() {
        return asArray(dataDictionaryService.allTypes());
    }

    @Override
    public DataDictionaryDTO getUnique(String type, String code) {
        return asDto(dataDictionaryService.getUnique(type, code));
    }

    @Override
    public DataDictionaryDTO[] list(String configKey) {
        String[] ck = StringUtil.tokenizeToStringArray(configKey, ":");
        List<DataDictionary> dataDictionaries = dataDictionaryService.list(ck[0], ck.length > 1 ? ck[1] : null);
        return asArray(dataDictionaries.toArray(new DataDictionary[dataDictionaries.size()]));
    }

    public static DataDictionaryDTO[] asArray(DataDictionary[] dictionaries) {
        DataDictionaryDTO[] array = new DataDictionaryDTO[dictionaries.length];
        for (int i = 0; i < dictionaries.length; i++) {
            array[i] = asDto(dictionaries[i]);
        }
        return array;
    }

    public static DataDictionaryTypeDTO[] asArray(List<DataDictionaryType> dictionaryTypes) {
        DataDictionaryTypeDTO[] array = new DataDictionaryTypeDTO[dictionaryTypes.size()];
        for (int i = 0; i < dictionaryTypes.size(); i++) {
            array[i] = asDto(dictionaryTypes.get(i));
        }
        return array;
    }

    public static DataDictionaryDTO asDto(DataDictionary dataDictionary) {
        return BeanUtil.copyProperties(new DataDictionaryDTO(), dataDictionary);
    }

    public static DataDictionaryTypeDTO asDto(DataDictionaryType type) {
        return BeanUtil.copyProperties(new DataDictionaryTypeDTO(), type);
    }

}
