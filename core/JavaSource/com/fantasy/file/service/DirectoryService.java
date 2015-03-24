package com.fantasy.file.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fantasy.file.bean.Directory;
import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.file.dao.DirectoryDao;
import com.fantasy.file.dao.FileManagerConfigDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;

@Service
@Transactional
public class DirectoryService {

	@Autowired
	private DirectoryDao directoryDao;
	@Autowired
	private FileManagerConfigDao fmcDao;

	public Pager<Directory> findPager(Pager<Directory> pager, List<PropertyFilter> filters) {
		return directoryDao.findPager(pager, filters);
	}

	public Pager<FileManagerConfig> findPagerfmc(Pager<FileManagerConfig> pager, List<PropertyFilter> filters) {
		return fmcDao.findPager(pager, filters);
	}

	public void delete(String... ids) {
		for (String id : ids) {
			this.directoryDao.delete(id);
		}
	}

	public Directory save(Directory directory) {
		directoryDao.save(directory);
		return directory;
	}

	public Directory get(String id) {
		Directory dty = directoryDao.get(id);
		FileManagerConfig fmc = dty.getFileManager();
		Hibernate.initialize(dty);
		Hibernate.initialize(fmc);
		return dty;
	}

	public boolean direcroryKeyUnique(String key) {
		return this.directoryDao.findUniqueBy("key", key) == null;
	}

}
