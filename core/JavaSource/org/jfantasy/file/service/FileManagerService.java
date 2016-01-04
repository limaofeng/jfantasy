package org.jfantasy.file.service;

import org.jfantasy.file.bean.FileManagerConfig;
import org.jfantasy.file.bean.enums.FileManagerType;
import org.jfantasy.file.dao.FileManagerConfigDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
        return this.fileManagerConfigDao.save(fileManager);
    }

    public void save(FileManagerType type, String id, String name, String description, Map<String, String> params) {
        FileManagerConfig fileManagerConfig = new FileManagerConfig();
        fileManagerConfig.setId(id);
        fileManagerConfig.setName(name);
        fileManagerConfig.setDescription(description);
        fileManagerConfig.setType(type);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            fileManagerConfig.addConfigParam(entry.getKey(), entry.getValue());
        }
        this.save(fileManagerConfig);
    }

    public List<FileManagerConfig> getAll() {
        return fileManagerConfigDao.getAll();
    }

    public void delete(String[] ids) {
        for (String id : ids) {
            this.fileManagerConfigDao.delete(id);
        }
    }

    public List<FileManagerConfig> listFileManager() {
        return this.fileManagerConfigDao.find(new Criterion[0], "id", "asc");
    }

    public static List<FileManagerConfig> getFileManagers() {
        return SpringContextUtil.getBeanByType(FileManagerService.class).listFileManager();
    }

}
