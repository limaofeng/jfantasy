package org.jfantasy.pay.service;

import org.jfantasy.pay.bean.Project;
import org.jfantasy.pay.dao.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Transactional
    public Project get(String key) {
        return projectDao.get(key);
    }

    @Transactional
    public void save(Project project) {
        this.projectDao.save(project);
    }
}
