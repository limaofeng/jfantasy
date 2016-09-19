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
    public Favorite save(Long memberId, String type, String targetType, String targetId, boolean watch) {
        Favorite favorite = this.findUnique(memberId, type, targetType, targetId);
        if (watch && favorite == null) {
            favorite = new Favorite();
            favorite.setMemberId(memberId);
            favorite.setType(type);
            favorite.setTargetType(targetType);
            favorite.setTargetId(targetId);
            return this.favoriteDao.save(favorite);
        } else if (favorite != null) {
            this.delete(favorite.getId());
        }
        return favorite;
    }

    @Transactional
    public void delete(Long... ids) {
        for (Long id : ids) {
            this.favoriteDao.delete(id);
        }
    }

    /**
     * 查询收藏信息
     *
     * @param memberId   用户ID
     * @param type       收藏类型
     * @param targetType 收藏对象类型
     * @param targetId   收藏对象ID
     * @return Favorite
     */
    public Favorite findUnique(Long memberId, String type, String targetType, String targetId) {
        return this.favoriteDao.findUnique(Restrictions.eq("member.id", memberId),
                Restrictions.eq("type", type),
                Restrictions.eq("targetType", targetType),
                Restrictions.eq("targetId", targetId));
    }
}
