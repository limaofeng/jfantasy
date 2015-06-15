package com.fantasy.framework.dao.mybatis.keygen.util;

import com.fantasy.framework.dao.mybatis.keygen.service.SequenceService;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class DataBaseKeyGenerator {

	private static DataBaseKeyGenerator dataBaseKeyGenerator;

	private int poolSize = 2;

	public static DataBaseKeyGenerator getInstance() {
		if (ObjectUtil.isNull(dataBaseKeyGenerator)) {
			dataBaseKeyGenerator = SpringContextUtil.getBeanByType(DataBaseKeyGenerator.class);
		}
		return dataBaseKeyGenerator;
	}

	@Autowired
	private SequenceService sequenceService;

	public long nextValue(String key) {
		return SequenceInfo.retrieve(this.sequenceService,this.poolSize,key).nextValue();
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

}
