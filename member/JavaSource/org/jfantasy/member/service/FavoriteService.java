package org.jfantasy.member.service;

import org.hibernate.criterion.Restrictions;
import org.jfantasy.member.bean.Favorite;
import org.jfantasy.member.dao.FavoriteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteDao favoriteDao;

    public List<Favorite> findByMemberId(Long id, String type) {
        return favoriteDao.find(Restrictions.eq("member.id", id), Restrictions.eq("type", type));
    }

    @Transactional
    public Favorite save(Favorite favorite) {
        return this.favoriteDao.save(favorite);
    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            this.favoriteDao.delete(id);
        }
    }

}
