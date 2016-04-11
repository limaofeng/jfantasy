package org.jfantasy.filestore.service;

import org.hibernate.Hibernate;
import org.jfantasy.filestore.bean.Directory;
import org.jfantasy.filestore.bean.FileManagerConfig;
import org.jfantasy.filestore.dao.DirectoryDao;
import org.jfantasy.filestore.dao.FileManagerConfigDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
