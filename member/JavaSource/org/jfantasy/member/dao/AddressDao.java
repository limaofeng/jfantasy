package org.jfantasy.member.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.member.bean.Address;
import org.springframework.stereotype.Repository;

@Repository
public class AddressDao extends HibernateDao<Address,Long> {

}
