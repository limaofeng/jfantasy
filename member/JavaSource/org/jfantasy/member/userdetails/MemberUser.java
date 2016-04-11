package org.jfantasy.member.userdetails;

import org.jfantasy.member.bean.Member;
import org.jfantasy.security.userdetails.SimpleUser;

public class MemberUser extends SimpleUser<Member> {

    private static final long serialVersionUID = 1031301459059227881L;

    public MemberUser(Member user) {
        super(user);
    }

}
