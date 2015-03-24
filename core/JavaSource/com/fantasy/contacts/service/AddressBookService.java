package com.fantasy.contacts.service;

import com.fantasy.contacts.bean.Book;
import com.fantasy.contacts.bean.Group;
import com.fantasy.contacts.bean.Linkman;
import com.fantasy.contacts.dao.BookDao;
import com.fantasy.contacts.dao.GroupDao;
import com.fantasy.contacts.dao.LinkmanDao;
import com.fantasy.contacts.service.handler.AddressBookLoginSuccessHandler;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.userdetails.SimpleUser;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
@Transactional
public class AddressBookService {

	@Autowired
	private BookDao bookDao;
	@Autowired
	private GroupDao groupDao;
	@Autowired
	private LinkmanDao linkmanDao;

	public AddressBook getAddressBook(String owner, String ownerType) {
		Book book = bookDao.findUnique(Restrictions.eq("ownerType", ownerType), Restrictions.eq("owner", owner));
		if (book == null) {
			book = new Book(owner, ownerType);
			bookDao.save(book);
		}
		return new AddressBook(book);
	}

	public Pager<Linkman> findPager(Pager<Linkman> pager, List<PropertyFilter> filters) {
		return this.linkmanDao.findPager(pager, filters);
	}

	public AddressBook myBook(String username) {
		return getAddressBook(username, "1");
	}

	public Object save(Group group) {
		groupDao.save(group);
		return group;
	}

	public void deleteGroup(Long... ids) {
		for (Long id : ids) {
			groupDao.delete(id);
		}
	}

	public void save(Linkman linkman) {
		linkmanDao.save(linkman);
	}

	public void deleteLinkman(Long... ids) {
		for (Long id : ids) {
			linkmanDao.delete(id);
		}
	}

	public Linkman get(Long id) {
		return this.linkmanDao.get(id);
	}

	public static List<Group> getGroups() {
		return ((AddressBook) SpringSecurityUtils.getCurrentUser(SimpleUser.class).data(AddressBookLoginSuccessHandler.CURRENT_USER_BOOK_KEY)).getGroups();
	}

}
