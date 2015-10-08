package com.fantasy.framework.util.jackson.serializer;

public class IdCardSerializer extends StringReplaceSerializer{

    public IdCardSerializer() {
        super("([0-9]{4})[0-9]{10}([0-9X]{4})", "$1********$2");
    }

}
