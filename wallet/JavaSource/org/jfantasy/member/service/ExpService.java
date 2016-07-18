package org.jfantasy.member.service;

import org.jfantasy.member.dao.ExpDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpService {

    @Autowired
    private ExpDao expDao;



}
