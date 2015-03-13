package com.fantasy.framework.dao.mybatis.keygen.service;

import com.fantasy.framework.dao.mybatis.keygen.bean.Sequence;
import com.fantasy.framework.dao.mybatis.keygen.dao.SequencesDao;
import com.fantasy.framework.util.common.ClassUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class SequenceServiceTest {

    @Autowired
    private SequencesDao sequencesDao;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void update(){
        List<Sequence> sequences = sequencesDao.selectAll();
        for(Sequence sequence :sequences){
            Class clazz = ClassUtil.forName(sequence.getKey());
            if(clazz != null) {
                Table table = (Table) clazz.getAnnotation(Table.class);
                Field[] fields = ClassUtil.getDeclaredFields(clazz, Id.class);
                if(fields.length == 1) {
                    String keyName = table.name()+":"+fields[0].getAnnotation(Column.class).name();
                    sequence.setOriginalKey(sequence.getKey());
                    sequence.setKey(keyName.toLowerCase());
                    sequencesDao.update(sequence);
                }
            }
        }
    }

}