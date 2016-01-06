package org.jfantasy.framework.util.ognl;


import java.util.Arrays;
import java.util.List;

public class OgnlBean {

    private String name;

    private Long number;

    private String[] names;

    private List<String> listNames;

    private List<OgnlBean> list;

    private OgnlBean bean;

    private OgnlBean[] array;

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

    public OgnlBean[] getArray() {
        return array;
    }

    public void setArray(OgnlBean[] array) {
        this.array = array;
    }

    public List<OgnlBean> getList() {
        return list;
    }

    public void setList(List<OgnlBean> list) {
        this.list = list;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public OgnlBean getBean() {
        return bean;
    }

    public void setBean(OgnlBean bean) {
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
