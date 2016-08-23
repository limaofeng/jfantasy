package org.jfantasy.member.service;

import org.hibernate.criterion.Restrictions;
import org.jfantasy.framework.spring.mvc.error.ValidationException;
import org.jfantasy.member.bean.Tag;
import org.jfantasy.member.dao.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagDao tagDao;

    public List<Tag> find(String ownerType, String ownerId, String type) {
        return this.tagDao.find(Restrictions.eq("ownerType", ownerType), Restrictions.eq("ownerId", ownerId), Restrictions.eq("type", type));
    }

    @Transactional
    public Tag save(String ownerType, String ownerId, String name, String type) {
        Tag tag = new Tag();
        tag.setOwnerType(ownerType);
        tag.setOwnerId(ownerId);
        tag.setType(type);
        tag.setName(name);
        tag.setTotal(0);
        return this.tagDao.save(tag);
    }

    @Transactional
    public void delete(String ownerType, String ownerId, Long... ids) {
        for (Long id : ids) {
            Tag tag = this.tagDao.get(id);
            if (tag == null) {
                continue;
            }
            if (!tag.getOwnerType().equals(ownerType) || !tag.getOwnerId().equals(ownerId)) {
                continue;
            }
            this.tagDao.delete(id);
        }
    }

    @Transactional
    public Tag update(String ownerType, String ownerId, Long id, String name) {
        Tag tag = this.tagDao.get(id);
        if (!tag.getOwnerType().equals(ownerType) || !tag.getOwnerId().equals(ownerId)) {
            throw new ValidationException(000.0f, "所有者不匹配");
        }
        if (null != this.tagDao.findUnique(Restrictions.eq("name", name), Restrictions.eq("type", tag.getType()), Restrictions.ne("id", tag.getId()))) {
            throw new ValidationException(000.0f, "同名标签已经存在");
        }
        tag.setName(name);
        return this.tagDao.update(tag);
    }

}
