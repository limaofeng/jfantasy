package org.jfantasy.website.service;

import org.jfantasy.website.IData;
import org.jfantasy.website.exception.SwpException;
import org.jfantasy.website.runtime.GenerateImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@Component
public class PageItemBeanService {

    @Autowired
    private GenerateImpl generate;

    public void refash(Long id) throws SwpException, IOException {
        this.generate.reCreate(id);
    }

    public void refash(List<IData> datas){

    }
}
