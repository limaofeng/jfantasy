package org.jfantasy.archives.service;

import org.jfantasy.archives.bean.Person;
import org.jfantasy.archives.dao.PersonDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonDao personDao;

    public Pager<Person> findPager(Pager<Person> pager, List<PropertyFilter> filters) {
        return this.personDao.findPager(pager, filters);
    }

    public Person get(Long id) {
        return this.personDao.get(id);
    }

    public Person save(Person person) {
        return null;
    }

    public void deltele(Long id) {

    }

    public Person update(Person person, boolean patch) {
        return this.personDao.update(person, patch);
    }

}
