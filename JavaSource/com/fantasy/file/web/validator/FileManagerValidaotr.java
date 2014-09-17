package com.fantasy.file.web.validator;

import javax.annotation.Resource;
import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.file.service.FileManagerService;
import com.fantasy.framework.util.common.StringUtil;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

/**
 *@Author lsz
 *@Date 2013-12-19 下午3:16:53
 *
 */
public class FileManagerValidaotr extends FieldValidatorSupport {
	
	@Resource
	private FileManagerService managerService;
	
	@Override
	public void validate(Object obj) throws ValidationException {
		String fieldName = getFieldName();
		String id = StringUtil.nullValue(getFieldValue(fieldName, obj));
		FileManagerConfig config= this.managerService.get(id);
		if(config!=null){
			addFieldError(fieldName, obj);
		}
		
	}

}

