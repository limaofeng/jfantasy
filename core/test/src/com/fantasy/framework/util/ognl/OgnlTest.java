package com.fantasy.framework.util.ognl;


import java.util.Arrays;
import java.util.List;

public class OgnlTest {

    private String name;

    private Long number;

    private String[] names;

    private List<String> listNames;

    private List<OgnlTest> list;

    private OgnlTest bean;

    private OgnlTest[] array;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public OgnlTest[] getArray() {
        return array;
    }

    public void setArray(OgnlTest[] array) {
        this.array = array;
    }

    public List<OgnlTest> getList() {
        return list;
    }

    public void setList(List<OgnlTest> list) {
        this.list = list;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public OgnlTest getBean() {
        return bean;
    }

    public void setBean(OgnlTest bean) {
        this.bean = bean;
    }

    public List<String> getListNames() {
        return listNames;
    }

    public void setListNames(List<String> listNames) {
        this.listNames = listNames;
    }

    @Override
    public String toString() {
        return "OgnlTest{" +
                "name='" + name + '\'' +
                ", number=" + number +
                ", names=" + Arrays.toString(names) +
                ", listNames=" + listNames +
                ", list=" + list +
                ", bean=" + bean +
                ", array=" + Arrays.toString(array) +
                '}';
    }

}
