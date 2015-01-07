package com.fantasy.framework.dao;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DaoUtil {

	public static Connection getConnection(String dataSourceName) throws SQLException {
		DataSource dataSource = (DataSource) SpringContextUtil.getBean(dataSourceName);
		if (ObjectUtil.isNotNull(dataSource)){
            return dataSource.getConnection();
        }
		throw new SQLException("名为:" + dataSourceName + ",的数据源没有找到!");
	}

	/**
	 * 多数据分页
	 * 
	 * @功能描述
	 * @param <T>
	 * @param pager
	 * @param param
	 * @param callBacks
	 * @return
	 */
	public static <T> Pager<T> findPager(Pager<T> pager, Map<String, Object> param, FindPagerCallBack<T>... callBacks) {
		pager = pager == null ? new Pager<T>() : pager;
		int totalCount = 0;
		Map<Pager<T>, FindPagerCallBack<T>> pagers = new LinkedHashMap<Pager<T>, FindPagerCallBack<T>>();
		// 计算总条数
		for (FindPagerCallBack<T> callBack : callBacks) {
			Pager<T> page = callBack.call(new Pager<T>(1), param);
			totalCount += page.getTotalCount();
			pagers.put(page, callBack);
		}
		pager.setTotalCount(totalCount);
		pager.setPageItems(new ArrayList<T>());
		int first = pager.getFirst();
		int pageSize = pager.getPageSize();
		totalCount = 0;
		for (Map.Entry<Pager<T>, FindPagerCallBack<T>> entry : pagers.entrySet()) {
			totalCount += entry.getKey().getTotalCount();
			if (first < totalCount && entry.getKey().getTotalCount() > 0) {
				int c_first = first - (totalCount - entry.getKey().getTotalCount()) + pager.getPageItems().size();
				entry.getKey().setFirst(c_first);
				entry.getKey().setPageSize(pageSize - pager.getPageItems().size());
				Pager<T> page = entry.getValue().call(entry.getKey(), param);
				pager.getPageItems().addAll(page.getPageItems());
				if (pager.getPageItems().size() >= pageSize) {
					break;
				}
			}
		}
		return pager;
	}

	/**
	 * 多数据表分页接口
	 * 
	 * @功能描述
	 * @author 李茂峰
	 * @since 2012-10-31 下午09:01:21
	 * @version 1.0
	 * @param <T>
	 */
	public static interface FindPagerCallBack<T> {

		Pager<T> call(Pager<T> pager, Map<String, Object> param);

	}
}
