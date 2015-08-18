package com.fantasy.website.service;

import com.fantasy.website.IData;
import com.fantasy.website.bean.Data;
import com.fantasy.website.exception.SwpException;
import com.fantasy.website.runtime.GenerateImpl;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
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
