package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Favorite;
import org.springframework.stereotype.Repository;

@Repository
public class FavoriteDao extends HibernateDao<Favorite, Long> {

}
