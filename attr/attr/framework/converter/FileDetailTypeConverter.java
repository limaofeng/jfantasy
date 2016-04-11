package org.jfantasy.attr.framework.converter;


import org.jfantasy.filestore.bean.FileDetail;
import org.jfantasy.filestore.service.FileService;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import ognl.DefaultTypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.Map;

public class FileDetailTypeConverter extends DefaultTypeConverter {

    private final static Log LOG = LogFactory.getLog(FileDetailTypeConverter.class);

    @Autowired
    private FileService fileService;

    @SuppressWarnings("rawtypes")
    public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
        if (toType == FileDetail.class) {
            String file = StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value);
            if (file.startsWith("{") && file.endsWith("}")) {
                FileDetail fileDetail = JSON.deserialize(file, FileDetail.class);
                if (fileDetail != null) {
                    return fileDetail;
                }
            }
            if (StringUtil.isBlank(file) || !file.contains(":")) {
                return null;
            }
            String[] arry = file.split(":");
            return fileService.getFileDetail(arry[1], arry[0]);
        } else if (toType == FileDetail[].class) {
            String files = StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value);
            FileDetail[] fileDetails;
            if (files.startsWith("[") && files.endsWith("]")) {
                fileDetails = JSON.deserialize(files, new TypeReference<FileDetail[]>() {
                });
                if (fileDetails != null) {
                    return fileDetails;
                }
            }
            if (StringUtil.isBlank(files)) {
                return new FileDetail[0];
            }
            String[] absolutePaths = files.split(",");
            if (absolutePaths.length == 0) {
                return new FileDetail[0];
            }
            fileDetails = new FileDetail[absolutePaths.length];
            for (int i = 0, len = absolutePaths.length; i < len; i++) {
                String[] arry = absolutePaths[i].split(":");
                FileDetail fileDetail = fileService.getFileDetail(arry[1], arry[0]);
                if (fileDetail == null) {
                    continue;
                }
                try {
                    fileDetails[i] = (FileDetail)fileDetail.clone();
                } catch (CloneNotSupportedException e) {
                    LOG.error(e.getMessage(),e);
                }
            }
            return fileDetails;
        } else if (value instanceof FileDetail && toType == String.class) {
            return JSON.serialize(value);
        } else if (value instanceof FileDetail[] && toType == String.class) {
            return JSON.serialize(value);
        }
        return super.convertValue(context, target, member, propertyName, value, toType);
    }
}
