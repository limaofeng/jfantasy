package org.jfantasy.member.bean.databind;


import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.member.bean.Member;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class MemberDeserializer extends JsonDeserializer<Member> {

    @Override
    public Member deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Long value = jp.getValueAsLong();
        if (StringUtil.isBlank(value)) {
            return null;
        }
        return new Member(value);
    }

}
