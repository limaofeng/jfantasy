package org.jfantasy.file.bean.databind;


import org.jfantasy.file.bean.FileDetail;
import org.jfantasy.file.bean.Image;
import org.jfantasy.file.service.FileService;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.StringUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ImageDeserializer extends JsonDeserializer<Image> {

    private static FileService fileService;

    @Override
    public Image deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String value = jp.getValueAsString();
        if (StringUtil.isBlank(value)) {
            return null;
        }
        String[] arry = value.split(":");
        return new Image(getFileService().getFileDetail(arry[1], arry[0]));
    }

    public FileService getFileService() {
        return fileService == null ? fileService = SpringContextUtil.getBeanByType(FileService.class) : fileService;
    }

}
