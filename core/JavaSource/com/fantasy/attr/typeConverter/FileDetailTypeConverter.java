package com.fantasy.attr.typeConverter;


import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FileService;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import ognl.DefaultTypeConverter;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileDetailTypeConverter extends DefaultTypeConverter {

    @Resource
    private FileService fileService;


    @SuppressWarnings("rawtypes")
    public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
        if (toType == FileDetail.class) {
            value = StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value);
            if (StringUtil.isBlank(value) || !value.toString().contains(":")) {
                return null;
            }
            String[] arry = value.toString().split(":");
            return fileService.getFileDetail(arry[1], arry[0]);
        } else if (toType == FileDetail[].class) {
            String files = StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value);
            if (StringUtil.isBlank(files)) {
                return new FileDetail[0];
            }
            String[] absolutePaths = RegexpUtil.split(files, ",");
            if (absolutePaths.length == 0) {
                return new FileDetail[0];
            }
            List<FileDetail> fileDetails = new ArrayList<FileDetail>();
            for (String absolutePath : absolutePaths) {
                if (StringUtil.isBlank(absolutePath))
                    continue;
                String[] arry = absolutePath.split(":");
                if (arry.length != 2) {
                    continue;
                }
                FileDetail fileDetail = fileService.getFileDetail(arry[1], arry[0]);
                if (fileDetail == null) {
                    continue;
                }
                fileDetails.add(fileDetail.clone());
            }
            return fileDetails.toArray(new FileDetail[fileDetails.size()]);
        } else if (value instanceof FileDetail && toType == String.class) {
            FileDetail fileDetail = (FileDetail) value;
            return fileDetail.getFileManagerId() + ":" + fileDetail.getAbsolutePath();
        } else if (value instanceof FileDetail[] && toType == String.class) {
            StringBuilder stringBuilder = new StringBuilder();
            for (FileDetail fileDetail : (FileDetail[]) value) {
                stringBuilder.append(fileDetail.getFileManagerId()).append(":").append(fileDetail.getAbsolutePath()).append(",");
            }
            return stringBuilder.toString();
        }
        return super.convertValue(context, target, member, propertyName, value, toType);
    }
}
