package com.fantasy.file.service;

import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.file.bean.enums.FileManagerType;
import com.fantasy.file.dao.FileManagerConfigDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.jackson.JSON;
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
        fileManager.setConfigParamStore(JSON.serialize(fileManager.getConfigParams()));
        this.fileManagerConfigDao.save(fileManager);
        return fileManager;
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
