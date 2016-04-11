package org.jfantasy.contacts.dao;

import org.jfantasy.contacts.bean.Book;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class BookDao extends HibernateDao<Book, Long>{

}
