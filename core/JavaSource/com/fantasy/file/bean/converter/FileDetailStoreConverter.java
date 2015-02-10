package com.fantasy.file.bean.converter;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FileService;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.regexp.RegexpUtil;
import ognl.DefaultTypeConverter;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileDetailStoreConverter extends DefaultTypeConverter {

	@Resource
	private FileService fileService;
	private Class<? extends FileDetail> clazz;

	public FileDetailStoreConverter() {
		clazz = FileDetail.class;
	}

	public FileDetailStoreConverter(Class<? extends FileDetail> clazz) {
		this.clazz = clazz;
	}

	@SuppressWarnings("rawtypes")
	public Object convertValue(Map context, Object target, Member member, String propertyName, Object value, Class toType) {
		if (String.class.isAssignableFrom(toType)) {
			String files = StringUtil.nullValue(ClassUtil.isArray(value) ? Array.get(value, 0) : value);
			if (StringUtil.isBlank(files)) {
				return "[]";
			}
			String[] absolutePaths = RegexpUtil.split(files, ",");
			if (absolutePaths.length == 0) {
				return "[]";
			}
			List<FileDetail> fileDetails = new ArrayList<FileDetail>();
			for (String absolutePath : absolutePaths) {
				if (StringUtil.isBlank(absolutePath)){
                    continue;
                }
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
			return JSON.serialize(fileDetails);
		}
		return super.convertValue(context, target, member, propertyName, value, toType);
	}
}
