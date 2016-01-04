package org.jfantasy.framework.dao.mybatis.dialect;


import org.junit.Test;

public class TestMySQLDialect {

    @Test
    public void testGetLimitString(){
        MySQLDialect mySQLDialect = new MySQLDialect();

        String sql = "select GEN_NAME,GEN_VALUE FROM sys_sequence WHERE GEN_NAME = #{value}";

        System.out.println(mySQLDialect.getLimitString(sql,1,5));

        System.out.println(mySQLDialect.getCountString(sql));

        System.out.print(sql);
    }

}
