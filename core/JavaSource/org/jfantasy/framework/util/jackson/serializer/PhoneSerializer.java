package org.jfantasy.framework.util.jackson.serializer;

public class PhoneSerializer extends StringReplaceSerializer{

    public PhoneSerializer() {
        super("([0-9]{3})[0-9]{1,5}([0-9X]{3})", "$1*****$2");
    }

}
