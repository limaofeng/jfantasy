package com.fantasy.framework.lucene.dao;

import java.util.List;

public interface LuceneDao<T> {

	long count();

	List<T> find(int start, int size);

	/**
	 * 不完善 待调整
	 * @功能描述 
	 * @param fieldName
	 * @param fieldIdValue
	 * @return
	 */
	@Deprecated
	List<T> findByField(String fieldName, String fieldIdValue);

	List<T> findByIds(String... id);

	T get(String id);

}
