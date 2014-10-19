package com.fantasy.framework.util.jackson;

import com.fantasy.member.bean.Member;

/**
 * Created by hebo on 2014/9/18.
 */
public class JacksonTest {

    public static  void main(String[] args){
        Member member= new Member();
        member.setUsername("aaaaaaaaaaaa");
        member.setPassword("123123");
        System.out.print( JSON.serialize(member));
    }
}
