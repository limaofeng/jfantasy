package com.fantasy.file.service;

import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.file.dao.FileManagerConfigDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
@Transactional
public class FileManagerService {

	@Autowired
	private FileManagerConfigDao fileManagerConfigDao;

	public Pager<FileManagerConfig> findPager(Pager<FileManagerConfig> pager, List<PropertyFilter> filters) {
		return fileManagerConfigDao.findPager(pager, filters);
	}

	public FileManagerConfig get(String id) {
		return this.fileManagerConfigDao.get(id);
	}

	public FileManagerConfig save(FileManagerConfig fileManager) {
		this.fileManagerConfigDao.save(fileManager);
		return fileManager;
	}

    public List<FileManagerConfig> getAll() {
        return fileManagerConfigDao.getAll();
    }
    
    public void delete(String[] ids){
    	for(String id:ids){
    		this.fileManagerConfigDao.delete(id);
    	}
    }

    public  List<FileManagerConfig> listFileManager(){
        return this.fileManagerConfigDao.find(new Criterion[0], "id", "asc");
    }
}
