package org.jfantasy.contacts.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.contacts.bean.Book;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class BookDao extends HibernateDao<Book, Long>{

}
