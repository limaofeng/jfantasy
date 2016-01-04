package org.jfantasy.member.bean.databind;


import org.jfantasy.member.bean.Member;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MemberSerializer extends JsonSerializer<Member> {

    @Override
    public void serialize(Member member, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (member == null) {
            jgen.writeNull();
        } else {
            jgen.writeNumber(member.getId());
        }
    }
}
