package com.fantasy.swp.service;

import com.fantasy.swp.IData;
import com.fantasy.swp.bean.Data;
import com.fantasy.swp.exception.SwpException;
import com.fantasy.swp.runtime.GenerateImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
