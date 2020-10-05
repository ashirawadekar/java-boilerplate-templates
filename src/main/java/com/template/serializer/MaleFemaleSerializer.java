package com.template.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MaleFemaleSerializer extends JsonSerializer<Character> {

    public void serialize(Character value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value.equals("MALE")) {
            gen.writeString("M");
        } else if( value.equals("FEMALE")) {
            gen.writeString("F");
        } else {
            gen.writeString("-");
        }
    }
}
