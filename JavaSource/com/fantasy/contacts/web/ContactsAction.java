package com.fantasy.contacts.web;

import com.fantasy.contacts.bean.Group;
import com.fantasy.contacts.bean.Linkman;
import com.fantasy.contacts.service.AddressBook;
import com.fantasy.contacts.service.AddressBookService;
import com.fantasy.contacts.service.handler.AddressBookLoginSuccessHandler;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.SpringSecurityUtils;
import com.fantasy.security.userdetails.SimpleUser;

import javax.annotation.Resource;
import java.util.List;

public class ContactsAction extends ActionSupport {
    /**
     *
     */
    private static final long serialVersionUID = 6209999459091063429L;

    @Resource(name = "ab.AddressBookService")
    private transient AddressBookService addressBookService;

    public String index() throws Exception {
        this.attrs.put("addressBook", AddressBook.current());
        return SUCCESS;
    }

    /**
     * 添加联系人分组
     *
     * @param group 分组
     * @return json
     */
    public String savegroup(Group group) {
        this.attrs.put(ROOT, AddressBook.current().addGroup(group));
        return JSONDATA;
    }

    /**
     * 联系人查询
     *
     * @param pager   分页对象
     * @param filters 筛选条件
     * @return json
     * @功能描述
     */
    public String search(Pager<Linkman> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_book.ownerType", "1"));
        filters.add(new PropertyFilter("EQS_book.owner", SpringSecurityUtils.getCurrentUserName()));
        this.attrs.put(ROOT, this.addressBookService.findPager(pager, filters));
        return JSONDATA;
    }

    /**
     * 查询联系人组
     *
     * @param pager   分页对象
     * @param filters 筛选条件
     * @return json
     */
    public String searchGruop(Pager<Linkman> pager, List<PropertyFilter> filters) {
        filters.add(new PropertyFilter("EQS_book.ownerType", "1"));
        filters.add(new PropertyFilter("EQS_book.owner", SpringSecurityUtils.getCurrentUserName()));
        this.attrs.put(ROOT, this.addressBookService.findPager(pager, filters));
        return JSONDATA;
    }

    public String group() {
        AddressBook mybook = ((AddressBook) SpringSecurityUtils.getCurrentUser(SimpleUser.class).data(AddressBookLoginSuccessHandler.CURRENT_USER_BOOK_KEY));
        this.attrs.put("groups", mybook.getGroups());
        return SUCCESS;
    }

    public String linkman(Pager<Linkman> pager, List<PropertyFilter> filters) {
        this.search(pager, filters);
        this.attrs.put("pager", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;

    }

    /**
     * editLinkman
     * 编辑联系人
     *
     * @param id id
     * @return succes
     */
    public String editlinkman(Long id) {
        this.attrs.put("linkman", this.addressBookService.get(id));
        return SUCCESS;
    }

    /**
     * 删除联系人
     *
     * @param ids ids
     * @return json
     */
    public String deleteLinkman(Long... ids) {
        AddressBook.current().removeLinkman(ids);
        return JSONDATA;
    }

    /**
     * 删除分组
     *
     * @param ids ids
     * @return json
     */
    public String deletegroup(Long... ids) {
        AddressBook.current().removeGroup(ids);
        return JSONDATA;
    }

    /**
     * 添加联系人
     *
     * @param linkman 联系人信息
     * @return json
     */
    public String savelinkman(Linkman linkman) {
        this.attrs.put(ROOT, AddressBook.current().addLinkman(linkman));
        return JSONDATA;
    }

    /**
     * 删除联系人
     *
     * @param ids 联系人ids
     * @return json
     */
    public String deletelinkman(Long... ids) {
        AddressBook.current().removeLinkman(ids);
        return JSONDATA;
    }

}
