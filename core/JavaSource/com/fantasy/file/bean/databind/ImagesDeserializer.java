package com.fantasy.file.bean.databind;


import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FileService;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImagesDeserializer extends JsonDeserializer<FileDetail[]> {

    private static FileService fileService;

    @Override
    public FileDetail[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String values = jp.getValueAsString();
        if (StringUtil.isBlank(values)) {
            return null;
        }
        List<FileDetail> fileDetails = new ArrayList<FileDetail>();
        for (String value : StringUtil.tokenizeToStringArray(values)) {
            String[] arry = value.split(":");
            FileDetail fileDetail = getFileService().getFileDetail(arry[1], arry[0]);
            if (fileDetail == null) {
                continue;
            }
            fileDetails.add(fileDetail);
        }
        return fileDetails.toArray(new FileDetail[fileDetails.size()]);
    }

    public FileService getFileService() {
        return fileService == null ? fileService = SpringContextUtil.getBeanByType(FileService.class) : fileService;
    }

}
