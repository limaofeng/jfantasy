package com.fantasy.member.userdetails;

import com.fantasy.member.bean.Member;
import com.fantasy.security.userdetails.SimpleUser;

public class MemberUser extends SimpleUser<Member> {

    private static final long serialVersionUID = 1031301459059227881L;

    public MemberUser(Member user) {
        super(user);
    }

}
