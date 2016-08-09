package org.jfantasy.member.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.member.bean.Address;
import org.jfantasy.member.dao.AddressDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressDao addressDao;

    @Transactional
    public Address save(Address address) {
        return addressDao.save(address);
    }

    @Transactional
    public void deltele(Long... ids) {
        this.addressDao.delete(ids);
    }

    public Address get(Long id) {
        return this.addressDao.get(id);
    }

    public Pager<Address> findPager(Pager<Address> pager, List<PropertyFilter> filters) {
        return this.addressDao.findPager(pager, filters);
    }

    @Transactional
    public Address update(Address address) {
        return this.addressDao.update(address);
    }

}
