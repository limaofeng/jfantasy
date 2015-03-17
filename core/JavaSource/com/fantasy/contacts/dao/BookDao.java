package com.fantasy.contacts.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.contacts.bean.Book;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository
public class BookDao extends HibernateDao<Book, Long>{

}
