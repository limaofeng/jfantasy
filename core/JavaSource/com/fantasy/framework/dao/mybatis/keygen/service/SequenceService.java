package com.fantasy.framework.dao.mybatis.keygen.service;

import com.fantasy.framework.dao.mybatis.keygen.bean.Sequence;
import com.fantasy.framework.dao.mybatis.keygen.dao.SequenceDao;
import com.fantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SequenceService {

	@Autowired
	private SequenceDao sequenceDao;

	/**
	 * 判断序列是否存在
	 * 
	 * @param key
	 *            序列名称
	 * @return boolean
	 */
	public boolean exists(String key) {
		return ObjectUtil.isNotNull(this.sequenceDao.findUniqueByKey(key));
	}

	/**
	 * 获取序列的下一个值
	 * 
	 * @param key
	 *            序列名称
	 * @param poolSize
	 *            序列增长值
	 * @return long
	 */
	public long next(String key, long poolSize) {
		Sequence sequence = this.sequenceDao.findUniqueByKey(key);
		if (ObjectUtil.isNull(sequence)){
            return newkey(key, poolSize);
        }
		sequence.setOriginalValue(sequence.getValue());
		sequence.setValue(sequence.getValue() + poolSize);
		int opt = this.sequenceDao.update(sequence);
		if (opt == 0) {
			return next(key, poolSize);
		}
		return sequence.getValue();
	}

	/**
	 * 创建一个新的序列
	 * 
	 * @param key
	 *            序列名称
	 * @param poolSize
	 *            序列增长值
	 * @return long
	 */
	public long newkey(String key, long poolSize) {
		int opt = this.sequenceDao.insert(new Sequence(key, poolSize));
		if (opt == 0) {
			return this.sequenceDao.findUniqueByKey(key).getValue();
		}
		return poolSize;
	}

}
