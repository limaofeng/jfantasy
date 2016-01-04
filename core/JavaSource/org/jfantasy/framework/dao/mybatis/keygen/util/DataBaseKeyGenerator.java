package org.jfantasy.framework.dao.mybatis.keygen.util;

import org.jfantasy.framework.dao.mybatis.keygen.service.SequenceService;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
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
        return SequenceInfo.retrieve(this.sequenceService, this.poolSize, key).nextValue();
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

}
