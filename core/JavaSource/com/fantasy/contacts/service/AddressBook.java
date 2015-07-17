package com.fantasy.contacts.service;

import com.fantasy.contacts.bean.Book;
import com.fantasy.contacts.bean.Group;
import com.fantasy.contacts.bean.Linkman;
import com.fantasy.contacts.listener.AddressBookListener;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.userdetails.SimpleUser;

import java.util.ArrayList;
import java.util.List;

public class AddressBook extends Book {

	private static final long serialVersionUID = -2610674385661404087L;

	private static AddressBookService addressBookService;

	private synchronized static AddressBookService getAddressBookService() {
		if (addressBookService == null) {
			addressBookService = SpringContextUtil.getBeanByType(AddressBookService.class);
		}
		return addressBookService;
	}

	public AddressBook(Book book) {
        BeanUtil.copyProperties(this, book);
	}

	public Group addGroup(Group group) {
		group.setBook(new Book(this.getId()));
		getAddressBookService().save(group);
		int index = ObjectUtil.indexOf(this.getGroups(), "id", group.getId());
		if (index == -1) {
			this.getGroups().add(group);
		} else {
			this.getGroups().set(index, group);
		}
		return group;
	}

	public static AddressBook current() {
		return (AddressBook) SpringSecurityUtils.getCurrentUser(SimpleUser.class).data(AddressBookListener.CURRENT_USER_BOOK_KEY);
	}

	public void removeGroup(Long... ids) {
		List<Long> removeIds = new ArrayList<Long>();
		for (Long id : ids) {
			if (ObjectUtil.indexOf(this.getGroups(), "id", id) != -1) {
				removeIds.add(id);
			}
		}
		ids = removeIds.toArray(new Long[removeIds.size()]);
		getAddressBookService().deleteGroup(ids);
		for (Long id : ids) {
			ObjectUtil.remove(this.getGroups(), "id", id);
		}
	}

	public Object addLinkman(Linkman linkman) {
		linkman.setBook(new Book(this.getId()));
		linkman = getAddressBookService().save(linkman);
		int index = ObjectUtil.indexOf(this.getLinkmans(), "id", linkman.getId());
		if (index == -1) {
			this.getLinkmans().add(linkman);
		} else {
			this.getLinkmans().set(index, linkman);
		}
		return linkman;
	}

	public void removeLinkman(Long... ids) {
		List<Long> removeIds = new ArrayList<Long>();
		for (Long id : ids) {
			if (ObjectUtil.indexOf(this.getLinkmans(), "id", id) != -1) {
				removeIds.add(id);
			}
		}
		ids = removeIds.toArray(new Long[removeIds.size()]);
		getAddressBookService().deleteLinkman(ids);
		for (Long id : ids) {
			ObjectUtil.remove(this.getLinkmans(), "id", id);
		}
	}

}
