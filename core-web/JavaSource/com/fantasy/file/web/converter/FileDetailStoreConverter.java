package com.fantasy.file.web.converter;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.service.FileService;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.regexp.RegexpUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileDetailStoreConverter extends StrutsTypeConverter {

    private final static Log LOG = LogFactory.getLog(FileDetailStoreConverter.class);

	@Autowired
	private FileService fileService;

	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if(FileDetail.class.isAssignableFrom(toClass)){
			String[] absolutePaths = new String[0];
			for(String value : values){
				absolutePaths = ObjectUtil.join(absolutePaths,RegexpUtil.split(value, ","));
			}
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
				try {
					return fileDetail.clone();
				} catch (CloneNotSupportedException e) {
                    LOG.error(e.getMessage(),e);
					return null;
				}
			}
			return null;
		}else if(FileDetail[].class.isAssignableFrom(toClass)){
			String[] absolutePaths = new String[0];
			for(String value : values){
				absolutePaths = ObjectUtil.join(absolutePaths,RegexpUtil.split(value, ","));
			}
			List<FileDetail> fileDetails = new ArrayList<FileDetail>();
			for (String absolutePath : absolutePaths) {
				FileDetail fileDetail = (FileDetail)this.convertFromString(context, new String[]{absolutePath}, FileDetail.class);
				if (fileDetail == null){
					continue;
				}
				fileDetails.add(fileDetail);
			}
			return fileDetails.toArray(new FileDetail[fileDetails.size()]);
		}
		return null;
	}

	@Override
	public String convertToString(Map context, Object o) {
		return JSON.serialize(o);
	}

}
